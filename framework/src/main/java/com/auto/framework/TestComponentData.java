package com.auto.framework;


import com.auto.framework.auto.AutoConf;
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
    private final String installationDir;
    private final String resourcePath;

    private TestComponentData(Builder builder) {
        host = builder.host;
        logDir = builder.logDir;
        port = builder.port;
        server = builder.server;
        installationDir = builder.installationDir;
        resourcePath = builder.resourcePath;
    }

    public static class Builder {
        private int port = 0;
        private String host = StringUtils.EMPTY;
        private String server = StringUtils.EMPTY;
        private String installationDir = StringUtils.EMPTY;
        private String resourcePath = StringUtils.EMPTY;
        private String logDir = StringUtils.EMPTY;

        public Builder() {
        }

        /**
         * @param installationDir String
         * @return TestComponentData
         */
        @Deprecated
        public TestComponentData build(String installationDir) {
            this.host = installationDir;
            return build();
        }

        /**
         * @param server String
         * @param logDir String
         * @return TestComponentData
         */
        @Deprecated
        public TestComponentData build(String server, String logDir) {
            this.logDir = logDir;
            this.server = server;
            return build();
        }

        /**
         * @param host   String
         * @param port   int
         * @param logDir String
         * @return TestComponentData
         */
        @Deprecated
        public TestComponentData build(String host, int port, String logDir) {
            this.host = host;
            this.logDir = logDir;
            this.port = port;
            return build();
        }

        /**
         * @param host   String
         * @param port   int
         * @param server String
         * @param logDir String
         * @return TestComponentData
         */
        @Deprecated
        public TestComponentData build(String host, int port, String server, String logDir) {
            this.logDir = logDir;
            this.host = host;
            this.port = port;
            this.server = server;
            return build();
        }

        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        public Builder withAppConfig(AutoConf.Application appConfig) {
            this.host = appConfig.getHost();
            this.port = appConfig.getPort();
            this.server = appConfig.getServer();
            this.logDir = appConfig.getLogDir();
            this.installationDir = appConfig.getInstallationDir();
            return this;
        }

        public Builder withPort(int port) {
            this.port = port;
            return this;
        }

        public Builder withSerVer(String server) {
            this.server = server;
            return this;
        }

        public Builder withLogDir(String logDir) {
            this.logDir = logDir;
            return this;
        }

        public Builder withInstallationDir(String installationDir) {
            this.installationDir = installationDir;
            return this;
        }

        public Builder withResourcePath(String resourcePath) {
            this.resourcePath = resourcePath;
            return this;
        }

        public TestComponentData build() {
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

    public String getResourcePath() {
        return resourcePath;
    }
}
