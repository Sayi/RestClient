package com.deepoove.restclient.model;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sayi
 */

public class RestTest implements Serializable {

    private String id;
    private String method = "GET";

    private String url = "";

    private List<KeyValue> headers = new ArrayList<>();
    private List<KeyValue> queryParameters = new ArrayList<>();
    private String bodyType = "Text";
    private List<KeyValue> formBodys = new ArrayList<>();
    private String body;

    private BasicAuth basicAuth = new BasicAuth();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<KeyValue> getHeaders() {
        return headers;
    }

    public void setHeaders(List<KeyValue> headers) {
        this.headers = headers;
    }

    public List<KeyValue> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(List<KeyValue> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public List<KeyValue> getFormBodys() {
        return formBodys;
    }

    public void setFormBodys(List<KeyValue> formBodys) {
        this.formBodys = formBodys;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void addHeader(KeyValue keyvaue) {
        headers.add(keyvaue);
    }

    public BasicAuth getBasicAuth() {
        return basicAuth;
    }

    public void setBasicAuth(BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }

    public String getQueryUrl(){
        final StringBuilder newQuery = new StringBuilder();
        if (getUrl().indexOf("?") == -1){
            newQuery.append("?");
        }
        for (RestTest.KeyValue item : queryParameters) {
            if (newQuery.length() > 1) {
                newQuery.append("&");
            }
            StringBuffer sb = new StringBuffer();
            try {
                sb.append(URLEncoder.encode(item.getKey(), "UTF-8")).append("=")
                        .append(URLEncoder.encode(item.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                sb.append(item.getKey()).append("=").append(item.getValue());
            }
            newQuery.append(sb.toString());
        }
        return getUrl() + (newQuery.length() > 1 ? newQuery.toString() : "");
    }

    public static class KeyValue implements Serializable{
        private String key;
        private String value;

        public KeyValue() {
        }
        public KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
