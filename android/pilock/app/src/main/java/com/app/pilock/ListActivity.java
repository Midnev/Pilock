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
        list = new ArrayList<>();
        adapter = new DoorLogAdapter(this,list);

        HttpManager httpManager = new HttpManager(adapter);
        httpManager.sendHttpRequest();

        lv=findViewById(R.id.dlog_list);
        lv.setAdapter(adapter);


    }




}
