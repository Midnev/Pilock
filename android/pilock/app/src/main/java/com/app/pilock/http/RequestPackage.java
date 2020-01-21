package com.app.pilock.http;

import java.util.HashMap;
import java.util.Map;

public class RequestPackage {
    private String url;
    private String method= "GET";
    private Map<String,String> params;

    public RequestPackage() {
        params = new HashMap<>();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParam(Map<String, String> params) {
        this.params = params;
    }



}
