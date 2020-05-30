package com.auto.framework.reporter;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestFailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TestFailException() {
        super();
    }

    public TestFailException(String message) {
        super(message);
    }

    public TestFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public TestFailException(Throwable cause) {
        super(cause);
    }
}
