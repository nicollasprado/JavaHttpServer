package com.nicollasprado;

import com.nicollasprado.enums.HttpMethod;
import com.nicollasprado.exceptions.InvalidHttpMethodException;
import com.nicollasprado.exceptions.InvalidHttpProtocolVersionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private final Socket clientSocket;
    private static final Logger LOGGER = LogManager.getLogger();

    public ConnectionHandler(Socket newClientSocket){
        this.clientSocket = newClientSocket;
    }

    public void run(){
        try {
            handleRequestReceive();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleRequestReceive() throws IOException {
        while(clientSocket.isConnected()){
            BufferedReader request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            char[] buffer = new char[256];
            while(request.read(buffer) != -1){
                processRequest(buffer);
                System.out.println(buffer);
            }
        }
    }

    private void processRequest(char[] request){
        int iterIndex = 0;

        // Extract http method
        StringBuilder method = new StringBuilder();
        for(; iterIndex < request.length; iterIndex++){
            char iterChar = request[iterIndex];

            if(iterChar == '/'){
                iterIndex++;
                break;
            }

            method.append(iterChar);
        }

        try {
            HttpMethod.valueOf(method.toString().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid HTTP method requested, method: {}", method);
            throw new InvalidHttpMethodException();
        }

        // Extract http protocol version
        StringBuilder httpVersion = new StringBuilder();
        for(; iterIndex < request.length; iterIndex++){
            char iterChar = request[iterIndex];

            if(iterChar == '\n') break;
            if(iterChar == ' ') continue;

            httpVersion.append(iterChar);
        }

        if(!httpVersion.toString().equalsIgnoreCase("HTTP/1.1")){
            LOGGER.error("Invalid HTTP Protocol version, version: {}", httpVersion.toString());
            throw new InvalidHttpProtocolVersionException();
        }

    }

}
