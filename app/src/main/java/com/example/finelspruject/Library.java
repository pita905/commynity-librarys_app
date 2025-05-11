package com.example.finelspruject;

import java.util.ArrayList;
import java.util.List;

public class Library {
    private String id; // Changed from int to String for Firestore compatibility
    private String name;
    private String location;
    private List<Book> books;

    // No-argument constructor required for Firestore
    public Library() {
        // Initialize books to prevent null pointer exceptions
        this.books = new ArrayList<>();
    }

    public Library(String id, String name, String location, List<Book> books) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.books = books != null ? books : new ArrayList<>();
    }
    
    // For backward compatibility
    public Library(int id, String name, String location, List<Book> books) {
        this.id = String.valueOf(id);
        this.name = name;
        this.location = location;
        this.books = books != null ? books : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
