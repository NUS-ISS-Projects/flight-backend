package com.webapp.flightsearch.util;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.webapp.flightsearch.entity.User;

import java.util.HashMap;
import java.util.Map;

public class FirestoreUserWriter {

    public void saveUserToFirestore(Firestore firestore, User user) {
        try {
            // Convert User object to a Map
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", user.getName());
            userMap.put("userName", user.getUserName());
            userMap.put("email", user.getEmail());
            userMap.put("password", user.getPassword());
            // Add other user properties as needed

            // Get reference to the "users" collection and document reference
            DocumentReference userRef = firestore.collection("users").document(user.getUserName());

            // Set the data for the document
            ApiFuture<WriteResult> result = userRef.set(userMap);

            result.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
