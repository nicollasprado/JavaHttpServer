package com.nicollasprado;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

public class HttpServer {
    private final ServerSocket serverSocket;
    private ConnectionReceiver connReceiver;
    private static final Logger LOGGER = LogManager.getLogger();
    private final ControllersHandler controllersHandler = ControllersHandler.getHandler();

    public HttpServer(String host, int port){
        try{
            this.serverSocket = new ServerSocket(port, 5, InetAddress.getByName(host));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        if(serverSocket == null){
            throw new RuntimeException("Server socket isnt initialized");
        }

        if(connReceiver != null && connReceiver.isAlive()){
            LOGGER.error("Attempted to start ConnectionReceiver while another instance is running. Existing thread: {}", connReceiver.getName());
            throw new RuntimeException("ConnectionReceiver is already running");
        }

        this.connReceiver = new ConnectionReceiver(serverSocket);
        this.connReceiver.start();
    }

    public void registerController(Class<?> controller){
        controllersHandler.registerController(controller);
    }

}