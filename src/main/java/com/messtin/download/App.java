package com.messtin.download;

import java.io.IOException;

public class App {
    private static final int processors = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException {
        String url = "https://codeload.github.com/majinliang123/interview/zip/master";
        int threadNum = 2;
        DownloadTask.download(url, threadNum);
    }
}
