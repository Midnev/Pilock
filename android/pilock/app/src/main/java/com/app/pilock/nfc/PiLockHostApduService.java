package com.app.pilock.nfc;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

public class PiLockHostApduService extends HostApduService {

	//initialize data to prevent data leak
	private String data = "f";
	private String data2 = "";
	private String header = "n";

	private String ssid = "";
	private String sspwd = "";

	@Override
	public byte[] processCommandApdu(byte[] apdu, Bundle extras) {
		if (selectAidApdu(apdu)) {
			return getWelcomeMessage();
		}
		else {
			return getNextMessage();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {//data initialization
		try {
			header = intent.getStringExtra("header");// type fing, pswd, init
			data = intent.getStringExtra("data");
			if (header.equals("pswd")) {

				data2 = intent.getStringExtra("data2");
				Log.i("main",data2);
			}else if (header.equals("init")){//when in initialize mode fetch extra data to send in csv

				ssid = intent.getStringExtra("ssid");
				sspwd = intent.getStringExtra("sspwd");
				Log.i("pilock","data: "+ ssid+sspwd);
			}
		}catch (Exception e){}//logs if needed

		return super.onStartCommand(intent, flags, startId);
	}

	private byte[] getWelcomeMessage() {
		return header.getBytes();
	}

	private byte[] getNextMessage() {//delete data after sending
		String holder = "f";
		if (header.equals("init")){//initialize type header
			// send ssid and pwd
			if (ssid.equals("")){
				holder = sspwd;
				sspwd = "";
				header = "f";
			}else {
				holder = ssid;
				ssid = "";
			}
		}else if (header.equals("fing")){//authentication type header
			if (!data.equals("")){
				holder = data;
				header = "f";
				data = "";
			}
		}else if(header.equals("pswd")){
			if (data.equals("")){
				holder = data2;
				header = "f";
				data2 = "";
			}else{
				holder = data;
				data = "";
			}

		}
		Log.i("apdu",holder);
		return holder.getBytes();
	}

	private boolean selectAidApdu(byte[] apdu) {
		return apdu.length >= 2 && apdu[0] == (byte)0 && apdu[1] == (byte)0xa4;
	}

	@Override
	public void onDeactivated(int reason) {

	}
}