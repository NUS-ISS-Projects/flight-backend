package com.webapp.flightsearch.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Bean
    public Firestore firestore() throws IOException {
        // Path to your Firebase Admin SDK JSON file
        try {
            FileInputStream serviceAccount = new FileInputStream(
                    "C:\\Users\\princ\\NUS_ISS\\flight-backend\\firebase-cred.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
            System.out.println("Firebase application initialized");
        } catch (FileNotFoundException error) {
            // Handle file not found error
            error.printStackTrace();
        } catch (IOException e) {
            // Handle IO exception
            e.printStackTrace();
        }

        return FirestoreClient.getFirestore();
    }
}
