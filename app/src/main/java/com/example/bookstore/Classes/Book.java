package com.example.bookstore.Classes;

import java.util.Date;

public class Book {

    private String key;
    private String name;
    private String author;
    private String genres;
    private String imageURL;
    private String published;


    public Book(){}

    public Book(String name, String author, String genres,String imageURL, String published) {
        this.name = name;
        this.author = author;
        this.genres = genres;
        this.published = published;
        this.imageURL = imageURL;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenres() {
        return genres;
    }

    public String getPublished() {
        return published;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
