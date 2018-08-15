package com.messtin.download;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.concurrent.CountDownLatch;

public class DownloadThread implements Runnable {

    private HttpURLConnection httpURLConnection;
    private long startPos;
    private long endPos;
    private RandomAccessFile file;
    private int id;
    private CountDownLatch latch;

    public DownloadThread(URLConnection urlConnection, long startPos, long endPos, RandomAccessFile file, int id, CountDownLatch latch) {
        this.httpURLConnection = (HttpURLConnection) urlConnection;
        this.startPos = startPos;
        this.endPos = endPos;
        this.file = file;
        this.id = id;
        this.latch = latch;
    }

    @Override
    public void run() {
        setRangeHeader();
        try (InputStream in = new BufferedInputStream(httpURLConnection.getInputStream())) {

            byte[] b = new byte[1024];
            int len = 0;
            long count = startPos;
            file.seek(startPos);
//            in.skip(startPos);
            while ((len = in.read(b)) != -1) {
                file.write(b, 0, len);
            }
            latch.countDown();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

    private void setRangeHeader() {
        httpURLConnection.setRequestProperty("RANGE",
                "bytes=" + startPos + "-" + endPos);
    }

}
