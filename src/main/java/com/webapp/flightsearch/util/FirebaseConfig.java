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

    private static boolean firebaseInitialized = false;

    @Bean
    public Firestore firestore() throws IOException {
        if (!firebaseInitialized) {
            // Initialize FirebaseApp only if it hasn't been initialized before
            initializeFirebaseApp();
            firebaseInitialized = true;
        }
        return FirestoreClient.getFirestore();
    }

    private void initializeFirebaseApp() throws IOException {
        // Get the current working directory
        String currentDirectory = System.getProperty("user.dir");

        // Construct the file path relative to the current working directory
        String filePath = currentDirectory + File.separator + "app" + File.separator + "google-services.json";

        try {
            System.out.println(filePath);
            FileInputStream serviceAccount = new FileInputStream(filePath);
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
    }
}
