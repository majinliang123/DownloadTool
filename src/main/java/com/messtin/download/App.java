package com.messtin.download;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        String url = "https://download.jetbrains.8686c.com/idea/ideaIC-2018.2.tar.gz";
        int threadNum = 4;
        DownloadTask.download(url, threadNum);
    }
}
