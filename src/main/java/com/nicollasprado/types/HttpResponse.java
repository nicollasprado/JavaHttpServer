package com.nicollasprado.types;

import com.nicollasprado.enums.HttpStatusCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class HttpResponse {
    private final HttpStatusCode statusCode;
}
