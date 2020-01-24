package com.app.pilock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.app.pilock.http.DoorLogAdapter;
import com.app.pilock.http.HttpManager;

import java.util.ArrayList;
import java.util.Arrays;

public class ListActivity extends AppCompatActivity {

    ArrayList<String> list;

    private ListView lv;
    private DoorLogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        HttpManager httpManager = new HttpManager();
        stringToArr(httpManager.sendHttpRequest());


        lv.findViewById(R.id.dlog_list);
        lv.setAdapter(adapter = new DoorLogAdapter(this,list));

        //adapter.notifyDataSetChanged();
    }

    private void stringToArr(String str){
        list = new ArrayList();
        String[] strArr = str.split(",");
        int listSize=strArr.length;
        list.addAll(Arrays.asList(strArr));

    }


}
