package com.example.finelspruject;

import java.util.List;

public class Library {
    private int id;
    private String name;
    private String location;
    private List<Book> books;

    public Library(int id, String name, String location, List<Book> books) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.books = books;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<Book> getBooks() {
        return books;
    }
}
