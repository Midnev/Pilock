package com.pilock.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;

@Service
public class ServersideFireBaseService {

    //@Value("${firebase.url}")
    private String dbUrl = "https://pilock-a4732.firebaseio.com";
    //@Value("${firebase.path}")
    private String configPath ="src/main/resources/pilock-firebase-adminsdk.json";

    public ServersideFireBaseService() {
        try {
            FileInputStream serviceAccount =
                    new FileInputStream(configPath);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(dbUrl)
                    .build();

            FirebaseApp.initializeApp(options);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    private static final String ANDROID_NEWS_ICON_RESOURCE = "news_alert_icon";
/*    private static final int APNS_NEWS_BADGE_RESOURCE = 42;
    private static final String WEBPUSH_NEWS_ICON_URL = "https://auto.news.url/alert.png";*/

    public String sendAutoDoorNotification(String title, String body,String topic) throws Exception {
        Message message = Message.builder()
                .setNotification(new Notification(title, body))
                .setAndroidConfig(AndroidConfig.builder()
                        .setNotification(AndroidNotification.builder()
                                .setIcon(ANDROID_NEWS_ICON_RESOURCE)
                                .build())
                        .build())
                /*.setApnsConfig(ApnsConfig.builder() //ios
                        .setAps(Aps.builder()
                                .setBadge(APNS_NEWS_BADGE_RESOURCE)
                                .build())
                        .build())
                .setWebpushConfig(WebpushConfig.builder() //web
                        .setNotification(new WebpushNotification("title", title, WEBPUSH_NEWS_ICON_URL))
                        .build())*/
                .setTopic(topic)
                .build();
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }


}
