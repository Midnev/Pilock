package com.app.pilock.http;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class HttpManager {

    public HttpManager() {

    }

    String url = "http://192.168.0.7:8085/";
    String getUrl = "test";
    String postUrl = "data";
    String key = "";

    String line = "";

    public String sendHttpRequest() {

        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get( url+getUrl , new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                    Log.i("PiLock","START ============================");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    Log.i("PiLock",new String(response));
                    line = new String(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.i("PiLock","Fail: "+statusCode);
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });

        }catch (Exception e){
            Log.i("HCE",e.getMessage());
        }

        return line;
    }

    public String postHttpRequest(String data){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("state", data);


        client.post(url + postUrl, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("HCEDEMO",new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return "";
    }

}
