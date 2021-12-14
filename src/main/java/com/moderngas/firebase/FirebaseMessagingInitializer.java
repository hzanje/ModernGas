package com.moderngas.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class FirebaseMessagingInitializer {

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigFile;

    @PostConstruct
    public void initialize() throws IOException {

        FirebaseOptions build = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigFile).getInputStream()))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(build);
        }

        FirebaseApp.initializeApp(build);
    }
}
