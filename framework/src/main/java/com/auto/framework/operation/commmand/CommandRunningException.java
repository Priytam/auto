package com.auto.framework.operation.commmand;

import java.io.IOException;
import java.util.concurrent.Executors;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class CommandRunningException extends RuntimeException {
    private static final long serialVersionUID = -1170466989781746231L;

    public CommandRunningException(final Exception cause) {
        super(cause);
    }

    public CommandRunningException(final String message) {
        super(message);
    }

    @Override
    public IOException getCause() {
        return (IOException) super.getCause();
    }
}
