package com.example.finelspruject;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Firebase implementation to replace the SQLite LibraryDatabaseHelper
 */
public class FirebaseLibraryHelper {
    private static final String TAG = "FirebaseLibraryHelper";
    
    // Firebase instances
    private final FirebaseFirestore mFirestore;
    private final FirebaseStorage mStorage;
    
    // Collection names
    private static final String LIBRARIES_COLLECTION = "libraries";
    private static final String BOOKS_COLLECTION = "books";
    
    // Singleton instance
    private static FirebaseLibraryHelper instance;
    
    /**
     * Get the singleton instance of FirebaseLibraryHelper
     */
    public static FirebaseLibraryHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseLibraryHelper();
        }
        return instance;
    }
    
    /**
     * Private constructor
     */
    private FirebaseLibraryHelper() {
        mFirestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
    }
    
    /**
     * Add a new library to Firestore
     * @param name The library's name
     * @param location The library's location
     * @param listener A listener to handle the result
     */
    public void addLibrary(String name, String location, final OnLibraryAddedListener listener) {
        Map<String, Object> library = new HashMap<>();
        library.put("name", name);
        library.put("location", location);
        library.put("timestamp", System.currentTimeMillis());
        
        mFirestore.collection(LIBRARIES_COLLECTION)
            .add(library)
            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        String libraryId = task.getResult().getId();
                        listener.onLibraryAdded(true, "Library added successfully", libraryId);
                    } else {
                        listener.onLibraryAdded(false, 
                            "Failed to add library: " + task.getException().getMessage(), null);
                    }
                }
            });
    }
    
    /**
     * Add a book with an image to a library
     * @param libraryId The ID of the library
     * @param title The book's title
     * @param author The book's author
     * @param imageBase64 The book cover image as a Base64 string
     * @param listener A listener to handle the result
     */
    public void addBookToLibraryWithImage(String libraryId, String title, String author, 
                                         String imageBase64, final OnBookAddedListener listener) {
        // Log for debugging
        Log.d(TAG, "Adding book to library ID: " + libraryId);
        
        // Check if the library ID is valid
        if (libraryId == null || libraryId.isEmpty()) {
            listener.onBookAdded(false, "Invalid library ID");
            return;
        }
        
        // Check if image is provided
        if (imageBase64 == null || imageBase64.isEmpty()) {
            // No image provided, just add the book without an image
            addBookWithoutImage(libraryId, title, author, listener);
            return;
        }
        
        // Since Firebase Storage is causing issues, let's store the Base64 image string directly in Firestore
        // This will allow us to display the image without Firebase Storage
        Log.d(TAG, "Storing image as Base64 string directly in Firestore");
        
        // Create book document in Firestore with Base64 image
        Map<String, Object> book = new HashMap<>();
        book.put("title", title);
        book.put("author", author);
        book.put("libraryId", libraryId);
        book.put("timestamp", System.currentTimeMillis());
        
        // Store the image as Base64 in isbn field (this is the field BookAdapter uses)
        if (imageBase64 != null && !imageBase64.isEmpty()) {
            book.put("isbn", imageBase64);
        } else {
            book.put("isbn", "");
        }
        
        mFirestore.collection(BOOKS_COLLECTION)
            .add(book)
            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        listener.onBookAdded(true, "Book added successfully with image");
                    } else {
                        Log.e(TAG, "Error adding book with image: " + task.getException());
                        listener.onBookAdded(false, "Failed to add book: " + task.getException().getMessage());
                    }
                }
            });
        
        /* Commented out Firebase Storage code until proper setup is complete
        try {
            // First, upload the image to Firebase Storage
            String imagePath = "books/" + libraryId + "/" + System.currentTimeMillis() + ".jpg";
            StorageReference imageRef = mStorage.getReference().child(imagePath);
            
            byte[] imageData = android.util.Base64.decode(imageBase64, android.util.Base64.DEFAULT);
            
            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Get the image URL
                    imageRef.getDownloadUrl().addOnCompleteListener(urlTask -> {
                        if (urlTask.isSuccessful()) {
                            String imageUrl = urlTask.getResult().toString();
                            
                            // Create book document in Firestore
                            Map<String, Object> book = new HashMap<>();
                            book.put("title", title);
                            book.put("author", author);
                            book.put("imageUrl", imageUrl);
                            book.put("imagePath", imagePath);
                            book.put("libraryId", libraryId);
                            book.put("timestamp", System.currentTimeMillis());
                            
                            mFirestore.collection(BOOKS_COLLECTION)
                                .add(book)
                                .addOnCompleteListener(docTask -> {
                                    if (docTask.isSuccessful()) {
                                        listener.onBookAdded(true, "Book added successfully");
                                    } else {
                                        Log.e(TAG, "Error adding book: " + docTask.getException());
                                        listener.onBookAdded(false, "Failed to add book: " + docTask.getException().getMessage());
                                    }
                                });
                        } else {
                            Log.e(TAG, "Error getting download URL: " + urlTask.getException());
                            // Fall back to adding without the image
                            addBookWithoutImage(libraryId, title, author, listener);
                        }
                    });
                } else {
                    Log.e(TAG, "Error uploading image: " + task.getException());
                    // Fall back to adding without the image
                    addBookWithoutImage(libraryId, title, author, listener);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception in addBookToLibraryWithImage: " + e.getMessage(), e);
            // Fall back to adding without the image
            addBookWithoutImage(libraryId, title, author, listener);
        }
        */
    }
    
    /**
     * Add a book without an image to a library
     * @param libraryId The ID of the library
     * @param title The book's title
     * @param author The book's author
     * @param listener A listener to handle the result
     */
    private void addBookWithoutImage(String libraryId, String title, String author, final OnBookAddedListener listener) {
        // Create book document in Firestore without image
        Map<String, Object> book = new HashMap<>();
        book.put("title", title);
        book.put("author", author);
        book.put("libraryId", libraryId);
        book.put("timestamp", System.currentTimeMillis());
        // Set empty string for isbn to avoid null issues
        book.put("isbn", "");
        
        mFirestore.collection(BOOKS_COLLECTION)
            .add(book)
            .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        listener.onBookAdded(true, "Book added successfully");
                    } else {
                        Log.e(TAG, "Error adding book without image: " + task.getException());
                        listener.onBookAdded(false, 
                            "Failed to add book: " + task.getException().getMessage());
                    }
                }
            });
    }
    
    /**
     * Get all libraries from Firestore
     * @param listener A listener to handle the result
     */
    public void getAllLibraries(final OnLibrariesRetrievedListener listener) {
        mFirestore.collection(LIBRARIES_COLLECTION)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Library> libraries = new ArrayList<>();
                        
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String name = document.getString("name");
                            String location = document.getString("location");
                            
                            // Create Library object with the string ID
                            Library library = new Library(id, name, location, new ArrayList<>());
                            libraries.add(library);
                        }
                        
                        // Now get books for each library
                        if (libraries.size() > 0) {
                            fetchBooksForLibraries(libraries, listener);
                        } else {
                            listener.onLibrariesRetrieved(true, libraries);
                        }
                    } else {
                        listener.onLibrariesRetrieved(false, null);
                    }
                }
            });
    }
    
    /**
     * Helper method to fetch books for each library
     */
    private void fetchBooksForLibraries(List<Library> libraries, final OnLibrariesRetrievedListener listener) {
        final int[] completedCount = {0};
        
        for (Library library : libraries) {
            getBooksByLibraryId(String.valueOf(library.getId()), new OnBooksRetrievedListener() {
                @Override
                public void onBooksRetrieved(boolean success, List<Book> books) {
                    if (success) {
                        library.setBooks(books);
                    }
                    
                    completedCount[0]++;
                    if (completedCount[0] == libraries.size()) {
                        listener.onLibrariesRetrieved(true, libraries);
                    }
                }
            });
        }
    }
    
    /**
     * Get books for a specific library
     * @param libraryId The ID of the library
     * @param listener A listener to handle the result
     */
    public void getBooksByLibraryId(String libraryId, final OnBooksRetrievedListener listener) {
        mFirestore.collection(BOOKS_COLLECTION)
            .whereEqualTo("libraryId", libraryId)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<Book> books = new ArrayList<>();
                        
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String title = document.getString("title");
                            String author = document.getString("author");
                            
                            // Get image data from isbn field where we store the Base64 image
                            String imageData = document.getString("isbn");
                            
                            // Create full book object with all fields
                            Book book = new Book(id, title, author, imageData, document.getString("libraryId"), 
                                              document.getLong("timestamp") != null ? document.getLong("timestamp") : 0);
                            
                            books.add(book);
                        }
                        
                        listener.onBooksRetrieved(true, books);
                    } else {
                        listener.onBooksRetrieved(false, null);
                    }
                }
            });
    }
    
    // Interfaces for callbacks
    public interface OnLibraryAddedListener {
        void onLibraryAdded(boolean success, String message, String libraryId);
    }
    
    public interface OnBookAddedListener {
        void onBookAdded(boolean success, String message);
    }
    
    public interface OnLibrariesRetrievedListener {
        void onLibrariesRetrieved(boolean success, List<Library> libraries);
    }
    
    public interface OnBooksRetrievedListener {
        void onBooksRetrieved(boolean success, List<Book> books);
    }
}
