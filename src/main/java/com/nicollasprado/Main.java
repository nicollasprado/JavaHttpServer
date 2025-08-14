package com.nicollasprado;

public class Main {
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer("localhost", 3333);
        httpServer.start();
    }
}
