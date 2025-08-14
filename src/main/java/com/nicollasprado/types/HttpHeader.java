package com.nicollasprado.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HttpHeader {
    private String key;
    private String value;
}
