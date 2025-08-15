package com.nicollasprado.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    NOT_FOUND(404);

    private final int code;

    public String toString(){
        String refinedName = this.name().replace("_", " ");

        return this.code + " " + refinedName;
    }

}
