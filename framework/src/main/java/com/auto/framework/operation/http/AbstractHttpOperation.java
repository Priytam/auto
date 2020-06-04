package com.auto.framework.operation.http;

import com.auto.framework.operation.Operation;
import com.google.common.collect.Lists;

import java.net.HttpURLConnection;
import java.util.Collections;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public abstract class AbstractHttpOperation implements Operation {
    private HttpOpResponse response;
    private HttpOpRequest request;

    @Override
    public void execute() {
        if (shouldRunInBackground()) {
            new Thread(this::executeHttp).start();
        } else {
            executeHttp();
        }
    }

    private void executeHttp() {
        long startTime = System.currentTimeMillis();
        try {
            request = getHttpRequestBuilder().build();
            request.sendRequest();
            long endTime = System.currentTimeMillis();
            String output = request.getOutput() == null ? "null" : request.getOutput().toString();
            if (request.getStatusCode() == HttpURLConnection.HTTP_OK || request.getStatusCode() == HttpURLConnection.HTTP_CREATED || request.getStatusCode() == HttpURLConnection.HTTP_ACCEPTED) {
                response = new HttpOpResponse(request.getStatusCode(), Lists.newArrayList(output), Collections.emptyList(), endTime - startTime);
            } else {
                response = new HttpOpResponse(request.getStatusCode(), Collections.emptyList(), Lists.newArrayList(output), endTime - startTime);
            }
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            response = new HttpOpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, Collections.emptyList(), Collections.emptyList(), endTime - startTime);
        }
    }

    @Override
    public HttpOpResponse getResult() {
        return response;
    }

    @Override
    public HttpOpRequest getRequest() {
        return request;
    }

    protected abstract HttpRequestBuilder getHttpRequestBuilder();
}
