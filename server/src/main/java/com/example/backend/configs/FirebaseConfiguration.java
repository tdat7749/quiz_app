package com.example.backend.configs;


import com.example.backend.commons.PrivateAppConstant;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfiguration {

    @SneakyThrows
    public FirebaseConfiguration() {
        //    @Value("${firebase.config.path}")
        String path = PrivateAppConstant.path;
        FileInputStream serviceAccount =
                new FileInputStream(path);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(PrivateAppConstant.dbUrl)
                .build();

        if(FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
        }
    }
}
