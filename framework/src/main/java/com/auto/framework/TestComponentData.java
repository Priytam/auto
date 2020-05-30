package com.auto.framework;


/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestComponentData {
    private String host;
    private String logDir;
    private int port;

    private TestComponentData(Builder builder) {
        host = builder.host;
        logDir = builder.logDir;
        port = builder.port;
    }

    public static class Builder {
        private String host = null;
        private String logDir = null;
        private int port;

        public Builder() {
        }

        public TestComponentData build(String host, int port, String logDir) {
            this.host = host;
            this.logDir = logDir;
            this.port = port;
            return build();
        }

        private TestComponentData build() {
            return new TestComponentData(this);
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getLogDir() {
        return logDir;
    }
}
