package com.auto.framework.operation.http;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class HttpRequestBuilder {
    private String m_sCookies;
    private String baseUrl;
    private String requestBody = null;
    private MimeTypes eMimeType;
    private HttpMethods eRequestType;
    private List<Header> header = new ArrayList<>();
    private String apiName;
    private long timeOut = 60 * 1000;

    public class Header {
        public String key;
        public String value;
    }

    public HttpRequestBuilder withBaseUrl(String sBaseUrl) {
        this.baseUrl = sBaseUrl;
        return this;
    }

    public HttpRequestBuilder addHeader(String key, String value) {
        Header header = new Header();
        header.key = key;
        header.value = value;
        this.header.add(header);
        return this;
    }

    public HttpRequestBuilder withApiName(String apiName) {
        this.apiName = apiName;
        return this;
    }

    public HttpRequestBuilder withRequestBody(String sRequestBody) {
        this.requestBody = sRequestBody;
        return this;
    }

    public HttpRequestBuilder withMimeType(MimeTypes eMimeType) {
        this.eMimeType = eMimeType;
        return this;
    }

    public HttpRequestBuilder withTimeout(long timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public HttpRequestBuilder withRequestType(HttpMethods eRequestType) {
        this.eRequestType = eRequestType;
        return this;
    }

    public HttpOpRequest build() {
        HttpOpRequest hRequest = new HttpOpRequest(baseUrl, eRequestType, apiName, (int)timeOut);
        hRequest.setContentType(eMimeType.toString());
        if (CollectionUtils.isNotEmpty(header)) {
            for (Header $ : header)
                hRequest.setHeader($.key, $.value);
        }
        hRequest.setContent(requestBody);
        return hRequest;
    }
}
