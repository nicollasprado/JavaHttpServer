package com.nicollasprado;

import com.nicollasprado.enums.HttpMethod;
import com.nicollasprado.enums.HttpStatusCode;
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
        int firstLineFinalIndex = request.indexOf('\r');

        String firstLine = request.substring(0, firstLineFinalIndex);
        String[] firstLineParts = firstLine.trim().split("\\s+");

        String method = firstLineParts[0];
        String path = firstLineParts[1];
        String protocolVersion = firstLineParts[2];

        return new HttpRequest(method, path, protocolVersion);
    }

    private HttpResponse generateResponse(HttpRequest request){
        // Validate http version
        String requestedProtocolVersion = request.getProtocolVersion();

        if(!requestedProtocolVersion.equalsIgnoreCase("HTTP/1.1")){
            LOGGER.error("Invalid HTTP protocol version requested, version: {}", requestedProtocolVersion);
            return new HttpResponse(HttpStatusCode.HTTP_VERSION_NOT_SUPPORTED);
        }

        // Validate Method
        String requestedMethod = request.getMethod();

        try {
            HttpMethod parsedMethod = HttpMethod.valueOf(requestedMethod);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid HTTP method requested, method: {}", requestedMethod);
            return new HttpResponse(HttpStatusCode.METHOD_NOT_ALLOWED);
        }

        // Validate endpoint
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
