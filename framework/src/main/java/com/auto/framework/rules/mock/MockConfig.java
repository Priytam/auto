package com.auto.framework.rules.mock;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class MockConfig {
    private final String responsePath;
    private final String url;
    private final RequestType type;
    private final String requestPath;
    private final int delay;

    public MockConfig(String responsePath, String url, RequestType type, String requestPath, int responseDelay) {
        this.responsePath = responsePath;
        this.url = url;
        this.type = type;
        this.requestPath = requestPath;
        this.delay = responseDelay;
    }

    public String getResponsePath() {
        return responsePath;
    }

    public String getUrl() {
        return url;
    }

    public RequestType getType() {
        return type;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public int getDelay() {
        return delay;
    }
}
