package com.nicollasprado.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class HttpRequest {
    private final String method;
    private final String endpoint;
    private final String protocolVersion;
    private HttpHeader[] extraHeaders;
}
