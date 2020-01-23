package com.app.pilock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.pilock.http.HttpManager;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        HttpManager httpManager = new HttpManager();

    }


}
