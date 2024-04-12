package com.webapp.flightsearch.util;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.webapp.flightsearch.entity.FlightBookmark;
import com.webapp.flightsearch.entity.User;

import java.util.HashMap;
import java.util.Map;

public class FirestoreWriter {

    public void saveUserToFirestore(Firestore firestore, User user) {
        try {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", user.getName());
            userMap.put("userName", user.getUserName());
            userMap.put("email", user.getEmail());
            userMap.put("password", user.getPassword());

            DocumentReference userRef = firestore.collection("users").document(user.getUserName());

            ApiFuture<WriteResult> result = userRef.set(userMap);

            result.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveBookMarkToFirestore(Firestore firestore, FlightBookmark flightBookmark) {
        try {
            Map<String, Object> bookmarkMap = new HashMap<>();
            bookmarkMap.put("userName", flightBookmark.getUserName());

            DocumentReference bookmarkRef = firestore.collection("bookmark").document(flightBookmark.getUserName());

            ApiFuture<WriteResult> result = bookmarkRef.set(bookmarkMap);

            result.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
