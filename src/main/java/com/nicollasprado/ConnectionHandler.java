package com.nicollasprado;

import com.nicollasprado.enums.HttpMethod;
import com.nicollasprado.enums.HttpStatusCode;
import com.nicollasprado.exceptions.InvalidHttpMethodException;
import com.nicollasprado.exceptions.InvalidHttpProtocolVersionException;
import com.nicollasprado.types.HttpRequest;
import com.nicollasprado.types.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ConnectionHandler extends Thread {
    private final Socket clientSocket;
    private static final Logger LOGGER = LogManager.getLogger();

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
            HttpRequest parsedRequest = parseRequest(requestStrBuilder.toString());
            HttpResponse response = processRequest(parsedRequest);

            LOGGER.info("HTTP response successfully sent to client: {}", clientSocket);
            sendResponse(response);

            try{
                clientSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private HttpRequest parseRequest(String request){
        int iterIndex = 0;

        // Extract http method
        StringBuilder method = new StringBuilder();
        for(; iterIndex < request.length(); iterIndex++){
            char iterChar = request.charAt(iterIndex);

            if(iterChar == '/'){
                iterIndex++;
                break;
            }

            method.append(iterChar);
        }

        HttpMethod parsedMethod;
        try {
            parsedMethod = HttpMethod.valueOf(method.toString().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid HTTP method requested, method: {}", method);
            throw new InvalidHttpMethodException();
        }

        // Extract http protocol version
        StringBuilder httpVersion = new StringBuilder();
        for(; iterIndex < request.length(); iterIndex++){
            char iterChar = request.charAt(iterIndex);

            if(iterChar == '\n') break;
            if(iterChar == ' ') continue;

            httpVersion.append(iterChar);
        }

        if(!httpVersion.toString().trim().equals("HTTP/1.1")){
            LOGGER.error("Invalid HTTP Protocol version, version: {}", httpVersion.toString());
            throw new InvalidHttpProtocolVersionException();
        }

        return new HttpRequest(parsedMethod);
    }

    private HttpResponse processRequest(HttpRequest request){
        return new HttpResponse(HttpStatusCode.OK);
    }

    private void sendResponse(HttpResponse response){
        BufferedWriter writer;

        try{
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ZoneId SaoPauloZoneId = ZoneId.of("America/Sao_Paulo");
        LocalDateTime serverDatetime = LocalDateTime.now(SaoPauloZoneId);
        String responseStatus = response.getStatusCode().toString();

        try{
            writer.write("HTTP/1.1 " + responseStatus + "\r\n");
            writer.write("DATE: " + serverDatetime + "\r\n");
            writer.write("\r\n\r\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
