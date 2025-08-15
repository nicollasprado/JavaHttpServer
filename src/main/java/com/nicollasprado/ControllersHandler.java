package com.nicollasprado;

import com.nicollasprado.annotations.MapEndpoint;
import com.nicollasprado.types.Endpoint;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ControllersHandler {
    private static ControllersHandler INSTANCE = new ControllersHandler();
    private List<Class<?>> controllers = new ArrayList<>();

    private ControllersHandler(){
    }

    public static ControllersHandler getHandler(){
        return INSTANCE;
    };

    public void registerController(Class<?> controller){
        controllers.add(controller);
    }

    public void registerController(List<Class<?>> controller){
        controllers.addAll(controller);
    }

    public List<Class<?>> getControllers() {
        return controllers;
    }

    public List<Endpoint> getEndpoints() {
        List<Endpoint> endpoints = new ArrayList<>();

        for(Class<?> controller : controllers){
            Method[] methods = controller.getDeclaredMethods();

            for(Method method : methods){
                MapEndpoint endpointAnnotation = method.getAnnotation(MapEndpoint.class);

                if(endpointAnnotation != null) endpoints.add(new Endpoint(endpointAnnotation.path(), method));
            }
        }

        return endpoints;
    }

}
