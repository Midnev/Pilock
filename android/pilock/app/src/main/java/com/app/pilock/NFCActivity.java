package com.app.pilock;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.pilock.nfc.PiLockHostApduService;


public class NFCActivity extends AppCompatActivity {//implements IsoDepTransceiver.OnMessageReceived, NfcAdapter.ReaderCallback {

    private Intent intent;
    private TextView txt;

    private NfcAdapter nfcAdapter;
    private Intent apduIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        getSupportActionBar().hide();
        intent = getIntent();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        apduIntent = new Intent(this, PiLockHostApduService.class);
        apduIntent.putExtra("header",intent.getStringExtra("header"));//header for types
        String data = intent.getStringExtra("data");
        if (data == null){
            apduIntent.putExtra("ssid",intent.getStringExtra("ssid"));//need to change sending data
            apduIntent.putExtra("sspwd",intent.getStringExtra("sspwd"));//need to change sending data
        }else if ( !data.equals("") ){
            apduIntent.putExtra("data",data);//need to change sending data
        }else {}

        txt = findViewById(R.id.txtNfcState);

    }

    @Override
    protected void onResume() {//send data to Service
        super.onResume();
        nfcAdapter.disableReaderMode(this);
        startService(apduIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
       finish();
    }



}
