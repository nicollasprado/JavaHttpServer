package com.nicollasprado.api;

import com.nicollasprado.annotations.MapEndpoint;

public class Controller {

    @MapEndpoint(path = "/user/all", method = "GET")
    public void getAllUsers(){
        System.out.println("TODOS USUÁRIOS");
    }

}
