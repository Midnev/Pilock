package com.app.pilock.http;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class PusherCLient {//goes background
    String CHANNEL_ID = "";

    public void writeNotigfication(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                //.setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {/*
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);*/
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "door alarm", importance);
            channel.setDescription("door alarm");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}