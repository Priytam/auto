package com.auto.reqres.operation;

import com.auto.framework.operation.http.AbstractHttpOperation;
import com.auto.framework.operation.http.HttpMethods;
import com.auto.framework.operation.http.HttpRequestBuilder;
import com.auto.framework.operation.http.MimeTypes;
import org.apache.commons.lang3.StringUtils;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 9:25 pm
 * email: mrpjpandey@gmail.com
 */
public class GetUserOp extends AbstractHttpOperation {

    private final String baseUrl;
    private final int userId;
    private final static String USER_ENDPOINT = "/api/users/";

    public GetUserOp(String server, String host, int port, int userId) {
        this.baseUrl = StringUtils.isEmpty(server) ? "http://" + host +  ":" + port : server ;
        this.userId = userId;
    }

    @Override
    protected HttpRequestBuilder getHttpRequestBuilder() {
        return new HttpRequestBuilder()
                .withBaseUrl(baseUrl + USER_ENDPOINT + userId)
                .withApiName("getUser")
                .withMimeType(MimeTypes.APPLICATION_JSON)
                .withRequestType(HttpMethods.GET);
    }

    @Override
    public boolean shouldRunInBackground() {
        return false;
    }

    @Override
    public String getName() {
        return "GetUserOp";
    }
}
