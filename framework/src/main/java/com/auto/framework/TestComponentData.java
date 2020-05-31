package com.auto.framework;


import org.apache.commons.lang3.StringUtils;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class TestComponentData {
    private final String server;
    private final String host;
    private final String logDir;
    private final int port;
    private String installationDir;

    private TestComponentData(Builder builder) {
        host = builder.host;
        logDir = builder.logDir;
        port = builder.port;
        server = builder.server;
        installationDir = builder.installationDir;
    }

    public static class Builder {
        private int port = 0;
        private String host = StringUtils.EMPTY;
        private String server = StringUtils.EMPTY;
        public String installationDir = StringUtils.EMPTY;
        private String logDir = null;

        public Builder() {
        }

        public TestComponentData build(String installationDir) {
            this.host = installationDir;
            return build();
        }

        public TestComponentData build(String server, String logDir) {
            this.logDir = logDir;
            this.server = server;
            return build();
        }

        public TestComponentData build(String host, int port, String logDir) {
            this.host = host;
            this.logDir = logDir;
            this.port = port;
            return build();
        }

        public TestComponentData build(String host, int port, String server, String logDir) {
            this.logDir = logDir;
            this.host = host;
            this.port = port;
            this.server = server;
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

    public String getServer() {
        return server;
    }

    public String getInstallationDir() {
        return installationDir;
    }
}
