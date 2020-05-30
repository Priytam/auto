package com.auto.framework.operation.http;

import com.auto.framework.operation.OpResult;
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
            HttpOpRequest build = getHttpRequestBuilder().build();
            build.sendRequest();
            long endTime = System.currentTimeMillis();
            if (build.getStatusCode() == HttpURLConnection.HTTP_OK || build.getStatusCode() == HttpURLConnection.HTTP_CREATED || build.getStatusCode() == HttpURLConnection.HTTP_ACCEPTED) {
                response = new HttpOpResponse(build.getStatusCode(), Lists.newArrayList(build.getOutput().toString()), Collections.emptyList(), endTime - startTime);
            } else {
                response = new HttpOpResponse(build.getStatusCode(), Collections.emptyList(), Lists.newArrayList(build.getOutput().toString()), endTime - startTime);
            }
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            response = new HttpOpResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, Collections.emptyList(), Collections.emptyList(), endTime - startTime);
        }
    }

    @Override
    public OpResult getResult() {
        return response;
    }

    protected abstract HttpRequestBuilder getHttpRequestBuilder();
}
