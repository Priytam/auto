package com.auto.framework.auto;

import java.util.List;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class AutoConf {
    private List<Application> applications;
    private String resourcePath;

    public static class Application {
        private String name;
        private String host;
        private int port;
        private String server;
        private String logDir;
        private String installationDir;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getServer() {
            return server;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public String getLogDir() {
            return logDir;
        }

        public void setLogDir(String logDir) {
            this.logDir = logDir;
        }

        public String getInstallationDir() {
            return installationDir;
        }

        public void setInstallationDir(String installationDir) {
            this.installationDir = installationDir;
        }
    }

    public List<Application> getApplications() {
        return applications;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }
}

