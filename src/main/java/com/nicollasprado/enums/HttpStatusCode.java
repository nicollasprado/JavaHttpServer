package com.nicollasprado.enums;

import com.nicollasprado.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    NOT_FOUND(404),
    HTTP_VERSION_NOT_SUPPORTED(505),
    METHOD_NOT_ALLOWED(405);

    private final int code;

    public String toString(){
        String refinedName = this.name().replace("_", " ");

        if(!refinedName.equals("OK")){
            refinedName = StringUtils.toTitleCase(refinedName);
        }

        if(refinedName.toLowerCase().contains("http")){
            refinedName = refinedName.replace("Http", "HTTP");
        }

        return this.code + " " + refinedName;
    }

}
