package com.app.pilock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InitActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        getSupportActionBar().hide();//hide title bar

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

   public void onDataReadyClick(View v){
        //fetch
       EditText es = findViewById(R.id.edit_ssid);
       EditText ep = findViewById(R.id.edit_pwd);
       String ssid = es.getText().toString();
       String sspwd = ep.getText().toString();

       if (ssid.equals("")){
           Toast.makeText(this, "data is needed to connect",Toast.LENGTH_SHORT).show();
           return ;
       }


       if(nfcAdapter != null) {
           if (running){
               running = false;
               Intent intent = new Intent(getApplicationContext(),NFCActivity.class);
                intent.putExtra("header","init");
                intent.putExtra("ssid",ssid);
                intent.putExtra("sspwd",sspwd);
                //nfcAdapter.disableForegroundDispatch(this);

               startActivityForResult(intent,0);
           }
       }else {
           Toast.makeText(this, "NFC not available on this device",Toast.LENGTH_SHORT).show();
       }

   }

}
