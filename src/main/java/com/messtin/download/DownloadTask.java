package com.messtin.download;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadTask {

    public static void download(String urlStr, int threadNum) throws IOException, InterruptedException {
        CountDownLatch latch = new CountDownLatch(threadNum);
        String fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1,
                urlStr.contains("?") ? urlStr.indexOf("?") : urlStr.length());

        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();
        setHeaders(urlConnection);

        long contentLength = urlConnection.getContentLength();
        long threadLength = contentLength / threadNum;
        ExecutorService workers= Executors.newCachedThreadPool();
        try(RandomAccessFile file = new RandomAccessFile(fileName, "rw")){
            for(int i=0; i < threadNum; i++){
                long startPos = threadLength * i;
                long endPos = (threadLength * ( i + 1) - 1)> contentLength ? contentLength :threadLength * ( i + 1) -1;
                workers.submit(new DownloadThread(urlConnection, startPos, endPos, file, i,latch));
            }
            latch.await();
            workers.shutdown();
        } catch (Exception ex){
            ex.printStackTrace();
        }

        System.out.println("Complete download, start combine files");
    }

    public static void setHeaders(URLConnection urlConnection) {
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linu…) Gecko/20100101 Firefox/61.0");
        urlConnection.setRequestProperty("Accept-Language", "en-GB,en;q=0.5");
        urlConnection.setConnectTimeout(60 * 1000);
        urlConnection.setRequestProperty("Connection", "Keep-Alive");
    }
}
