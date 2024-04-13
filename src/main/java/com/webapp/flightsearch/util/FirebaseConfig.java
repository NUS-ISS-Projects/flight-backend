package com.webapp.flightsearch.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Bean
    public Firestore firestore() throws IOException {
        // Path to your Firebase Admin SDK JSON file
        // Get the current working directory
        String currentDirectory = System.getProperty("user.dir");

        // Construct the file path relative to the current working directory
        String filePath = currentDirectory + File.separator + "app" + File.separator + "google-services.json";

        try {
            FileInputStream serviceAccount = new FileInputStream(
                    filePath);
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
