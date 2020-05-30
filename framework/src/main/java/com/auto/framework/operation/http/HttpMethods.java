package com.auto.framework.operation.http;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public enum HttpMethods {
    POST("POST"),
    GET("GET"),
    DELETE("DELETE"),
    PUT("PUT"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    ;
    private final String HttpMethods;

    private HttpMethods(String HttpMethods) {
        this.HttpMethods = HttpMethods;
    }

    @Override
    public String toString() {
        return HttpMethods;
    }
}