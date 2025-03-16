package com.tyza66.nettyweb;

public class Main{
    public static void main(String[] args) throws Exception {
        new NettyHttpServer(8080).start();
    }
}
