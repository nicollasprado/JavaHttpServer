package com.nicollasprado;

import com.nicollasprado.enums.HttpMethod;
import com.nicollasprado.enums.HttpStatusCode;
import com.nicollasprado.exceptions.InvalidHttpMethodException;
import com.nicollasprado.exceptions.InvalidHttpProtocolVersionException;
import com.nicollasprado.types.Endpoint;
import com.nicollasprado.types.HttpRequest;
import com.nicollasprado.types.HttpResponse;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@NoArgsConstructor
public class RequestHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private final ControllersHandler controllersHandler = ControllersHandler.getHandler();

    public String processRequest(String request){
        HttpRequest parsedRequest = parseRequest(request);
        HttpResponse response = generateResponse(parsedRequest);
        return buildResponseStr(response);
    }

    private HttpRequest parseRequest(String request){
        int iterIndex = 0;

        // Extract http method
        StringBuilder method = new StringBuilder();
        for(; iterIndex < request.length(); iterIndex++){
            char iterChar = request.charAt(iterIndex);

            if(iterChar == '/'){
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

        StringBuilder endpoint = new StringBuilder();
        for(; iterIndex < request.length(); iterIndex++){
            char iterChar = request.charAt(iterIndex);

            if(iterChar == ' '){
                break;
            }

            endpoint.append(iterChar);
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

        return new HttpRequest(parsedMethod, endpoint.toString());
    }

    private HttpResponse generateResponse(HttpRequest request){
        String requestedEndpoint = request.getEndpoint();

        List<Endpoint> endpoints = controllersHandler.getEndpoints();
        boolean validEndpoint = false;
        for(Endpoint endpoint : endpoints){
            if(endpoint.getPath().equals(requestedEndpoint)){
                validEndpoint = true;
                break;
            }
        }

        if(!validEndpoint){
            LOGGER.info("Invalid endpoint requested: {}", requestedEndpoint);
            return new HttpResponse(HttpStatusCode.NOT_FOUND);
        }

        return new HttpResponse(HttpStatusCode.OK);
    }

    private String buildResponseStr(HttpResponse response){
        ZoneId SaoPauloZoneId = ZoneId.of("America/Sao_Paulo");
        LocalDateTime serverDatetime = LocalDateTime.now(SaoPauloZoneId);
        String responseStatus = response.getStatusCode().toString();

        StringBuilder responseStr = new StringBuilder();

        responseStr.append("HTTP/1.1 ").append(responseStatus).append("\r\n");
        responseStr.append("DATE: ").append(serverDatetime).append("\r\n");
        responseStr.append("\r\n\r\n");

        return responseStr.toString();
    }

}
