package com.messtin.download;

public class App {
    public static void main(String[] args){
        String url = "https://download.jetbrains.8686c.com/idea/ideaIC-2018.2.tar.gz";
        DownloadTask.download(url);
    }
}
