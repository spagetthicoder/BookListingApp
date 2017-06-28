package com.example.android.booklistingapp;

public class Book {
    private String mTitle;

    private String mAuthor;

    public Book(String title, String author) {
        mTitle = title;
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }
}
