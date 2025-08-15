package com.nicollasprado;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Socket clientSocket;
    private final RequestHandler requestHandler = new RequestHandler();

    public ConnectionHandler(Socket newClientSocket){
        this.clientSocket = newClientSocket;
    }

    public void run(){
        handleRequestReceive();
    }

    private void handleRequestReceive() {
        while(!clientSocket.isClosed()){
            BufferedReader request;
            try{
                request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            StringBuilder requestStrBuilder = new StringBuilder();
            String line;
            try {
                while (!clientSocket.isClosed() && (line = request.readLine()) != null && !line.isEmpty()) {
                    requestStrBuilder.append(line).append("\r\n");
                }
            }catch (IOException e) {
                throw new RuntimeException(e);
            }

            System.out.println(requestStrBuilder);
            String response = requestHandler.processRequest(requestStrBuilder.toString());

            LOGGER.info("HTTP response successfully sent to client: {}", clientSocket);
            sendResponse(response);

            try{
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void sendResponse(String response){
        BufferedWriter writer;

        try{
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try{
            writer.write(response);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
