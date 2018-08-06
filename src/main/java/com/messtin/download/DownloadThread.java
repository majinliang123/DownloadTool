package com.messtin.download;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.concurrent.CountDownLatch;

public class DownloadThread implements Runnable {

    private static final int threshold = 4096;

    private HttpURLConnection httpURLConnection;
    private long startPos;
    private long endPos;
    private String fileName;
    private int id;
    private CountDownLatch latch;

    public DownloadThread(URLConnection urlConnection, long startPos, long endPos, String fileName, int id, CountDownLatch latch) {
        this.httpURLConnection = (HttpURLConnection) urlConnection;
        this.startPos = startPos;
        this.endPos = endPos;
        this.fileName = fileName;
        this.id = id;
        this.latch = latch;
    }

    public void run() {
        setHttpHeader(httpURLConnection);
        try (InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
            OutputStream out= new BufferedOutputStream(new FileOutputStream(fileName + "-" + id, true))) {

            byte[] b = new byte[1024];
            int len = 0;
            int count = 0;
            while ((len = in.read(b)) != -1){
                out.write(b, 0, len);
                count ++;
                if(count < threshold){
                    out.flush();
                }
            }
            latch.countDown();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }

    private void setHttpHeader(HttpURLConnection httpURLConnection) {
        httpURLConnection.setConnectTimeout(60 * 1000);
        httpURLConnection.setAllowUserInteraction(true);
        httpURLConnection.setRequestProperty("Range",
                "bytes=" + startPos + "-" + endPos);
    }
}
