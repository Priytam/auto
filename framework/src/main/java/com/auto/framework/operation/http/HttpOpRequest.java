package com.auto.framework.operation.http;

import com.auto.framework.reporter.TestReporter;
import com.auto.framework.operation.OpRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * User: Priytam Jee Pandey
 * Date: 28/05/20
 * Time: 1:17 pm
 * email: mrpjpandey@gmail.com
 */
public class HttpOpRequest implements OpRequest {

    private final String name;
    private final HttpURLConnection connection;
    private final CharSequence url;
    private int statusCode;
    private StringBuffer output;
    private String requestBody;

    public HttpOpRequest(final CharSequence url, final HttpMethods method, String sName, int timeOut) throws HttpRequestException {
        this.name = sName;
        try {
            this.url = url;
            System.setProperty("http.keepAlive", "false");
            connection = (HttpURLConnection) new URL(url.toString()).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            connection.setRequestMethod(method.toString());
            connection.setConnectTimeout(timeOut);
            connection.setReadTimeout(timeOut);
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public void setContentType(String sContentType) {
        setHeader("Content-Type", sContentType);
    }

    public void setHeader(String sKey, String sValue) {
        connection.setRequestProperty(sKey, sValue);
    }

    public HttpOpRequest sendRequest() {
        if (requestBody != null) {
            writeToOutputStream();
        }
        statusCode = getResponseCode();
        output = readInput();
        TestReporter.traceExecution(this);
        return this;
    }

    private void writeToOutputStream() {
        connection.setDoOutput(true);
        try {
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            wr.writeBytes(requestBody);
            wr.flush();
            wr.close();
        } catch (Exception e) {
            TestReporter.TRACE(e.getMessage());
        }
    }

    private StringBuffer readInput() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response;
        } catch (Exception e) {
            TestReporter.TRACE(e.getMessage());
        }

        return null;
    }

    private int getResponseCode() {
        try {
            return connection.getResponseCode();
        } catch (IOException e) {
            return HttpURLConnection.HTTP_CONFLICT;
        }
    }


    public HttpURLConnection getConnection() {
        return connection;
    }

    public CharSequence getUrl() {
        return url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public StringBuffer getOutput() {
        return output;
    }

    public void setContent(String requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public String getCommandName() {
        return name;
    }

    public String getContent() {
        return requestBody;
    }
}