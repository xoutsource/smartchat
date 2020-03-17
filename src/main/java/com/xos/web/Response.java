package com.xos.web;

import lombok.Data;

@Data
public class Response {
    private boolean successful;
    private String error;
    private Object data;

    public static Response success(Object data) {
        Response response = new Response();
        response.setSuccessful(true);
        response.setData(data);
        return response;
    }

    public static Response fail(String error) {
        Response response = new Response();
        response.setSuccessful(false);
        response.setError(error);
        return response;
    }
}
