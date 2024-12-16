package com.example.finelspruject;

import java.util.List;

public class Library {
    private String name;
    private String location;
    private List<Book> books;
    private int id;
    // Constructor
    public Library(String name, String location, List<Book> books) {
        this.name = name;
        this.location = location;
        this.books = books;
    }

    // Getters and Setters
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public int getId() { return id; }
    public void setLocation(String location) { this.location = location; }

    public List<Book> getBooks() { return books; }
    public void setBooks(List<Book> books) { this.books = books; }
    public void setId(int id) { this.id = id; }
}
