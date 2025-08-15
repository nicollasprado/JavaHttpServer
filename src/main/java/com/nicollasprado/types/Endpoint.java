package com.nicollasprado.types;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

@AllArgsConstructor
@Data
public class Endpoint {
    private final String path;
    private final Method method;
}
