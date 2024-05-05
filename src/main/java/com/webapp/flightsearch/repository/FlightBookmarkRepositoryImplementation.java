package com.webapp.flightsearch.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.entity.FlightBookmark;
import com.webapp.flightsearch.util.FirestoreRetriever;
import com.webapp.flightsearch.util.FirestoreWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class FlightBookmarkRepositoryImplementation implements CustomFlightBookmarkRepository {

    private final FirestoreWriter firestoreWriter;
    private final FirestoreRetriever firestoreRetriever;

    @Autowired
    public FlightBookmarkRepositoryImplementation(FirestoreWriter firestoreWriter,
                                                  FirestoreRetriever firestoreRetriever) {
        this.firestoreWriter = firestoreWriter;
        this.firestoreRetriever = firestoreRetriever;
    }

    @Override
    public FlightBookmark saveBookmarkToFirebase(FlightBookmark bookmark,
                                                 BookmarkDto savedBookmark) {
        firestoreWriter.saveBookMarkToFirestore(bookmark, savedBookmark);
        return bookmark;
    }

    public List<BookmarkDto> getFlightBookmarks(String userName) {
        try {
            DocumentSnapshot documentSnapshot = firestoreRetriever.getUserByUsernameCheck(userName).get();
            if (documentSnapshot.exists()) {
                List<BookmarkDto> bookmarks = firestoreRetriever.getBookmarks(userName);
                return bookmarks;
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to fetch user from Firestore", e);
        }
    }

}
