package com.nicollasprado.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HttpStatusCode {
    OK(200),
    CREATED(201);

    private final int code;

    public String toString(){
        return this.code + " " + this.name();
    }

}
