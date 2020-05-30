package com.auto.framework.operation.http;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public enum MimeTypes {

    WILDCARD("*/*"),
    APPLICATION_XML("application/xml"),
    APPLICATION_ATOM_XML("application/atom+xml"),
    APPLICATION_XHTML_XML("application/xhtml+xml"),
    APPLICATION_SVG_XML("application/svg+xml"),
    APPLICATION_JSON("application/json"),
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded"),
    MULTIPART_FORM_DATA("multipart/form-data"),
    APPLICATION_OCTET_STREAM("application/octet-stream"),
    TEXT_PLAIN("text/plain"),
    TEXT_XML("text/xml"),
    TEXT_HTML("text/html");

    private final String mimeType;

    private MimeTypes(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public static MimeTypes fromString(String type) {
        MimeTypes result = null;
        MimeTypes[] values = values();
        for (MimeTypes value : values) {
            if (value.getMimeType().equals(type)) {
                result = value;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return mimeType;
    }
}
