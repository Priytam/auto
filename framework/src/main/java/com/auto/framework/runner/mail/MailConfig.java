package com.auto.framework.runner.mail;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * User: Priytam Jee Pandey
 * Date: 30/05/20
 * Time: 7:03 am
 * email: mrpjpandey@gmail.com
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
        "enabled",
        "to",
        "from",
        "fromName",
        "host",
        "port",
        "user",
        "password"
})
public class MailConfig {
    @JsonProperty("enabled")
    private boolean enabled;
    @JsonProperty("to")
    private String to;
    @JsonProperty("from")
    private String from;
    @JsonProperty("fromName")
    private String fromName;
    @JsonProperty("host")
    private String host;
    @JsonProperty("port")
    private String port;
    @JsonProperty("user")
    private String user;
    @JsonProperty("password")
    private String password;

    @JsonProperty("enabled")
    public boolean getEnabled() {
        return enabled;
    }

    @JsonProperty("enabled")
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonProperty("to")
    public String getTo() {
        return to;
    }

    @JsonProperty("to")
    public void setTo(String to) {
        this.to = to;
    }

    @JsonProperty("from")
    public String getFrom() {
        return from;
    }

    @JsonProperty("from")
    public void setFrom(String from) {
        this.from = from;
    }

    @JsonProperty("fromName")
    public String getFromName() {
        return fromName;
    }

    @JsonProperty("fromName")
    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    @JsonProperty("host")
    public String getHost() {
        return host;
    }

    @JsonProperty("host")
    public void setHost(String host) {
        this.host = host;
    }

    @JsonProperty("port")
    public String getPort() {
        return port;
    }

    @JsonProperty("port")
    public void setPort(String port) {
        this.port = port;
    }

    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }
}
