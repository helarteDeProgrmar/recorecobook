package models;

import java.io.Serializable;
import java.util.Map;

public class Book implements Serializable {
    private String title;
    private String author;
    private String genre;
    private Map<String, Integer> features; // Map of genre to score (1–10)

    public Book(String title, String author, String genre, Map<String, Integer> features) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.features = features;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Map<String, Integer> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        return String.format(
                "Title: %s | Author: %s | Genre: %s | Features: %s",
                title, author, genre, features.toString());
    }
}
