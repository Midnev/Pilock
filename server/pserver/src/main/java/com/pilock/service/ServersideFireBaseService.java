package com.pilock.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

@Service
public class ServersideFireBaseService {

    @Value("${firebase.server-key}")
    private String serverKey;

    @Value("${firebase.sender-id}")
    private String senderId;

    public ServersideFireBaseService() {
        System.out.println(serverKey);
    }

    private void initFireBase() {

        try {
            FileInputStream serviceAccount =
                    new FileInputStream("path/to/serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://pilock-a4732.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
}
