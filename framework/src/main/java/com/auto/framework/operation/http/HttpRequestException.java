package com.auto.framework.operation.http;

import java.io.IOException;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class HttpRequestException extends RuntimeException {
    private static final long serialVersionUID = -1170466989781746231L;

    protected HttpRequestException(final IOException cause) {
        super(cause);
    }

    @Override
    public IOException getCause() {
        return (IOException) super.getCause();
    }
}
