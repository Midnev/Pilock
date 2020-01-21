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

    public String sendHttpRequest() {
        String line = "";
        try {
            AsyncHttpClient client = new AsyncHttpClient();
            client.get( url+getUrl , new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.i("HCEDEMO","START ============================");
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    // called when response HTTP status is "200 OK"
                    Log.i("HCEDEMO",new String(response));


                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    Log.i("HCEDEMO","Fail: "+statusCode);
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }
            });




            /*URL url = new URL();
            line = url.getHost();
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("GET");

            InputStream iStream = httpCon.getInputStream();
            InputStreamReader isReader = new InputStreamReader(iStream);
            BufferedReader bfReader = new BufferedReader(isReader);
             line = bfReader.readLine();

            httpCon.disconnect();*/


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
