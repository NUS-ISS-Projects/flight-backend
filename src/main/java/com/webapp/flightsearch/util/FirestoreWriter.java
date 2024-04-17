package com.webapp.flightsearch.util;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.entity.FlightBookmark;
import com.webapp.flightsearch.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FirestoreWriter {
    private final Firestore firestore;

    @Autowired
    public FirestoreWriter(Firestore firestore) {
        this.firestore = firestore;
    }

    public void saveUserToFirestore(User user) {
        if (user == null) {
            return;
        }
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

    public void saveBookMarkToFirestore(FlightBookmark flightBookmark, BookmarkDto bookmarkDto) {
        try {
            DocumentReference bookmarkRef = firestore.collection("bookmark").document(flightBookmark.getUserName());

            // Retrieve the existing data
            DocumentSnapshot snapshot = bookmarkRef.get().get();
            Map<String, Object> existingData = snapshot.exists() ? snapshot.getData() : new HashMap<>();

            // Get the list of existing bookmarks, or create a new list if it doesn't exist
            List<BookmarkDto> existingBookmarks = existingData.containsKey(flightBookmark.getUserName())
                    ? (List<BookmarkDto>) existingData.get(flightBookmark.getUserName())
                    : new ArrayList<>();

            // Append the new bookmark
            existingBookmarks.add(bookmarkDto);

            // Update the document with the combined data
            existingData.put(flightBookmark.getUserName(), existingBookmarks);
            ApiFuture<WriteResult> result = bookmarkRef.set(existingData);

            result.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
