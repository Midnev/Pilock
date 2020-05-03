package com.app.pilock;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback{

    private ArrayList<Button> btns;
    private TextView pwdView;

    private NfcAdapter nfcAdapter;

    private String header="";
    private String data = "";


    private boolean running = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//hide title bar

        btns = new ArrayList<>();
        pwdView = findViewById(R.id.pwd);
        buttonCreate();//set buttons
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //getFireBaseToken();

        FirebaseMessaging.getInstance().subscribeToTopic("and0001")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getFireBaseToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("pilockERR", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        Log.d("pilockERR", token+"");
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);//check if disable mode is needed to get message, because get message when reading
    }

    @Override
    public void onTagDiscovered(Tag tag) {//do nothing
        }


    private void buttonCreate(){//in case need for random numbering
        btns.add((Button) findViewById(R.id.btn_0));
        btns.add((Button) findViewById(R.id.btn_1));
        btns.add((Button) findViewById(R.id.btn_2));
        btns.add((Button) findViewById(R.id.btn_3));
        btns.add((Button) findViewById(R.id.btn_4));
        btns.add((Button) findViewById(R.id.btn_5));
        btns.add((Button) findViewById(R.id.btn_6));
        btns.add((Button) findViewById(R.id.btn_7));
        btns.add((Button) findViewById(R.id.btn_8));
        btns.add((Button) findViewById(R.id.btn_9));

        int i = 0;
        for (Button btn:btns){
            btn.setText(i+"");
            i++;
        }
    }


    String pwdStr = "";//only for stars
    public void numberOnClick(View v){
        Button btn = findViewById(v.getId());
        data += btn.getText();
        pwdStr += "*";
        pwdView.setText(pwdStr);
    }
    public void cancelOnClick(View v){
        clearData();
    }

    public void confirmOnclick(View v){
        header = "pswd";
        openNfcActivity();
    }
    public void fingerOnCLick(View v){
        runFingerprint();
    }

    private void clearData(){
        pwdStr = "";
        try {
            pwdView.setText(pwdStr);
        }catch (Exception e){}

        header = "";
        data = "";
    }


    //----------------------------------------------------------------
    //nfc activity controll
    private void openNfcActivity(){
        if(nfcAdapter != null) {
            if (running){
                running = false;
                Intent intent = new Intent(getApplicationContext(),NFCActivity.class);
                intent.putExtra("header",header);
                intent.putExtra("data",data);
                if (header.equals("pswd")){
                    String data2 = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                    intent.putExtra("data2",data2);
                }

                //nfcAdapter.disableForegroundDispatch(this);
                clearData();
                startActivityForResult(intent,0);
            }
        }else {
            Toast.makeText(this, "NFC not available on this device",Toast.LENGTH_SHORT).show();
        }
    }
    //initialize activity
    public void openInitDoorLock(View v){
        Intent intent = new Intent(getApplicationContext(),InitActivity.class);

        startActivity(intent);
    }
    //list activity
    public void openList(View v){
        Intent intent = new Intent(getApplicationContext(),ListActivity.class);

        startActivity(intent);
    }
    //----------------------------------------------------------------

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        running =true;
    }


    private void runFingerprint(){
        BiometricPrompt.PromptInfo promptInfo;
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("지문을 인식시켜주세요")
                .setDescription("")
                .setSubtitle("")
                .setNegativeButtonText("취소")
                .build();

        BiometricPrompt prompt = new BiometricPrompt(this, Executors.newSingleThreadExecutor(), getAuthenticationCallBack());
        prompt.authenticate(promptInfo);
    }

    private BiometricPrompt.AuthenticationCallback getAuthenticationCallBack(){
        return new authenticationCallBackInner();
    }

    private class authenticationCallBackInner extends BiometricPrompt.AuthenticationCallback{

        @Override
        public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
        }

        @Override
        public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            header = "fing";
            data = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            openNfcActivity();
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
        }
    }

}
