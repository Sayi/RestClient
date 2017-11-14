package com.deepoove.restclient.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sayi
 */

public class RemoteResponse implements Serializable{

    long interval;

    int statusCode;
    String responseMessage;

    String response;

    Map<String, String> headers = new HashMap<>();

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
