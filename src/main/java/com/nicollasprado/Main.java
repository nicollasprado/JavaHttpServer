package com.nicollasprado;

import com.nicollasprado.api.Controller;

public class Main {
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer("localhost", 3333);
        httpServer.registerController(Controller.class);
        httpServer.start();
    }
}
