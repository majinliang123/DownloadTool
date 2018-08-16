package com.messtin.download;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DownloadThread implements Runnable {

    private URL url;
    private long startPos;
    private long endPos;
    private String fileName;

    public DownloadThread(URL url, long startPos, long endPos, String fileName) {
        this.url = url;
        this.startPos = startPos;
        this.endPos = endPos;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try (RandomAccessFile file = new RandomAccessFile(fileName, "rw")) {
            URLConnection urlConnection = url.openConnection();
            setRangeHeader(urlConnection);
            try (InputStream in = new BufferedInputStream(urlConnection.getInputStream())) {
                byte[] b = new byte[4096];
                file.seek(startPos);
                for (int len = 0; (len = in.read(b)) != -1; ) {
                    file.write(b, 0, len);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private void setRangeHeader(URLConnection urlConnection) {
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linu…) Gecko/20100101 Firefox/61.0");
        urlConnection.setRequestProperty("Accept-Language", "en-GB,en;q=0.5");
        urlConnection.setConnectTimeout(60 * 1000);
        urlConnection.setRequestProperty("Connection", "Keep-Alive");
        urlConnection.setAllowUserInteraction(true);
        urlConnection.setRequestProperty("Range",
                "bytes=" + startPos + "-" + endPos);
    }

}
