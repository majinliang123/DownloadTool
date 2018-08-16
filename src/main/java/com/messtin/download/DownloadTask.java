package com.messtin.download;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadTask {

    public static void download(String urlStr, int threadNum) throws IOException, InterruptedException {
        // generate file name according to url
        String fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1,
                urlStr.contains("?") ? urlStr.indexOf("?") : urlStr.length());

        // use thread pool
        ExecutorService workers = Executors.newCachedThreadPool();

        URL url = new URL(urlStr);
        URLConnection urlConnection = url.openConnection();

        long contentLength = urlConnection.getContentLength();
        long threadLength = (int) Math.ceil(contentLength * 1.0 / threadNum);

        System.out.println("Starting download, content length is: " + contentLength + " bytes");
        for (int i = 0; i < threadNum; i++) {
            long startPos = threadLength * i;
            long endPos = threadLength * (i + 1) > contentLength ? contentLength : threadLength * (i + 1) - 1;
            workers.submit(new DownloadThread(url, startPos, endPos, fileName));
        }

        // close thread pool
        workers.shutdown();
    }

}
