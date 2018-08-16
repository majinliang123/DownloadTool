package com.messtin.download;

import java.io.IOException;

public class App {
    private static final int processors = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws IOException, InterruptedException {
        String url = "http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg";
        int threadNum = Runtime.getRuntime().availableProcessors();
        DownloadTask.download(url, threadNum);
    }
}
