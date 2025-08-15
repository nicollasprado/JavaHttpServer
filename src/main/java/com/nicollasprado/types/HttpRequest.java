package com.nicollasprado.types;

import com.nicollasprado.enums.HttpMethod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class HttpRequest {
    private final HttpMethod method;
    private final String endpoint;
    private HttpHeader[] headers;
}
