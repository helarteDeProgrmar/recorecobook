package models;

import java.io.Serializable;
import java.util.Map;

public class Book implements Serializable {
    private String title;
    private String author;
    private String genre;
    private Map<String, Integer> features; // Map of genre to score (1–10)
    private String description;

    public Book(String title, String author, String genre, Map<String, Integer> features, String description) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.features = features;
        this.description = description;
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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int distance(Map<String, Integer> preferences) {
        int sum = 0;
        for (String genre : preferences.keySet()) {
            int preference = preferences.getOrDefault(genre, 0);
            int bookFeature = features.getOrDefault(genre, 0);
            sum += Math.abs(preference - bookFeature);
        }
        return sum;
    }

    @Override
    public String toString() {
        return String.format(
                "Title: %s | Author: %s | Genre: %s | Features: %s | Description: %s",
                title, author, genre, features.toString(), description);
    }
}
