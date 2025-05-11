package com.example.finelspruject;

public class Book {
    private String id; // Added for Firestore document ID
    private String title;
    private String author;
    private String isbn; // We'll use this for the image URL in Firebase
    private String libraryId; // Reference to parent library
    private long timestamp; // For tracking when the book was added
    
    // No-argument constructor required for Firestore
    public Book() {
        // Required empty constructor
    }

    // Constructor
    public Book(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.timestamp = System.currentTimeMillis();
    }
    
    // Full constructor
    public Book(String id, String title, String author, String isbn, String libraryId, long timestamp) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.libraryId = libraryId;
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return title; // or any field of the Book class to display
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getLibraryId() { return libraryId; }
    public void setLibraryId(String libraryId) { this.libraryId = libraryId; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
