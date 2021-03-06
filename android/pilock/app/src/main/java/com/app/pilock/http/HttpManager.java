package com.app.pilock.http;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class HttpManager {

    private DoorLogAdapter adapter;

    public HttpManager(DoorLogAdapter adapter) {
        this.adapter = adapter;
    }

    private ArrayList<String> stringToArr(String str){
        ArrayList<String> list = new ArrayList();
        String[] strArr = str.split(",");

        list.addAll(Arrays.asList(strArr));

        return list;
    }

    String url = "http://35.208.214.3:8080";
    String getUrl = "/android/csv";
    String postUrl = "data";
    String key = "";

    String line = "";

    public String sendHttpRequest() {

        try {
            AsyncHttpClient client = new AsyncHttpClient();
           //CustomAsyncHttpHandler ahandler = new CustomAsyncHttpHandler();
            //client.get( url+getUrl , ahandler);
            //line = ahandler.getResult();
            client.get(url + getUrl, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    // called before request is started
                    Log.i("PiLock","START ============================");
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                    Log.i("PiLock",new String(response));
                    line = new String(response);
                    adapter.setList(stringToArr(line));
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) { }
            });


        }catch (Exception e){
            Log.i("HCE",e.getMessage());
        }
        Log.i("PiLock","result"+line);
        return line;
    }

    public String postHttpRequest(String data){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("state", data);


        client.post(url + postUrl+key, params, new AsyncHttpResponseHandler() {
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
