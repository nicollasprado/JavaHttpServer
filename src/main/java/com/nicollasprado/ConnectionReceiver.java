package com.nicollasprado;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionReceiver extends Thread {
    private final ServerSocket serverSocket;
    private int connectedClientsQuantity = 0;

    public ConnectionReceiver(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void run(){
        try {
            Socket newClient = serverSocket.accept();
            this.connectedClientsQuantity++;
            new ConnectionHandler(newClient).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
