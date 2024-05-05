package com.webapp.flightsearch.repository;

import com.google.cloud.firestore.Firestore;
import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.entity.FlightBookmark;
import com.webapp.flightsearch.util.FirestoreWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FlightBookmarkRepositoryImplementation implements CustomFlightBookmarkRepository {

    private final FirestoreWriter firestoreWriter;

    @Autowired
    public FlightBookmarkRepositoryImplementation(FirestoreWriter firestoreWriter) {
        this.firestoreWriter = firestoreWriter;
    }

    @Override
    public FlightBookmark saveBookmarkToFirebase(FlightBookmark bookmark,
            BookmarkDto savedBookmark) {
        firestoreWriter.saveBookMarkToFirestore(bookmark, savedBookmark);
        return bookmark;
    }

}
