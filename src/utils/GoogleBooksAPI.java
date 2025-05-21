package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import models.Book;

public class GoogleBooksAPI {

    private static final String GOOGLE_BOOKS_URL = "https://www.googleapis.com/books/v1/volumes";

    public List<Book> searchBooksByAuthor(String author) {
        System.out.println("** GoogleBooksAPI | Method called");
        return fetchBooks(Collections.singletonList(author));
    }

    private List<Book> fetchBooks(List<String> authors) {
        List<Book> books = new ArrayList<>();

        for (String author : authors) {
            try {
                String query = URLEncoder.encode("inauthor:" + author, StandardCharsets.UTF_8);
                String urlStr = GOOGLE_BOOKS_URL + "?q=" + query
                        + "&langRestrict=es&printType=books&maxResults=40&orderBy=relevance";

                URL url = new URL(urlStr);
                System.out.println("** GoogleBooksAPI | the url is");
                System.out.println(url.toString());
                System.out.println(author);
                System.out.println("################################");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }

                    JSONObject json = new JSONObject(content.toString());
                    JSONArray items = json.optJSONArray("items");

                    if (items != null) {
                        for (int i = 0; i < items.length(); i++) {
                            JSONObject item = items.getJSONObject(i);
                            JSONObject info = item.getJSONObject("volumeInfo");

                            String title = info.optString("title", "Untitled");
                            JSONArray authorsJSON = info.optJSONArray("authors");
                            String mainAuthor = (authorsJSON != null && authorsJSON.length() > 0)
                                    ? authorsJSON.getString(0)
                                    : "Unknown Author";

                            String description = info.optString("description", "No description");

                            List<String> mappedCategories = new ArrayList<>();
                            JSONArray categoriesJSON = info.optJSONArray("categories");
                            if (categoriesJSON != null) {
                                for (int j = 0; j < categoriesJSON.length(); j++) {
                                    String rawCategory = categoriesJSON.getString(j);
                                    System.out.println("## GoogleBooksAPI | categoria: " + rawCategory);
                                    String translated = translateCategory(rawCategory);
                                    if (translated != null) {
                                        mappedCategories.add(translated);
                                    }
                                }
                            }

                            Map<String, Integer> features = normalizeCategories(mappedCategories);

                            books.add(new Book(title, mainAuthor, "implement", features));
                        }
                    }
                }

            } catch (Exception e) {
                System.err.println("Error processing author: " + author + " -> " + e.getMessage());
            }
        }

        return books;
    }

    private String translateCategory(String rawCategory) {
        if (rawCategory == null)
            return null;

        String lower = rawCategory.toLowerCase(Locale.ROOT);

        Map<String, List<String>> map = new LinkedHashMap<>();
        map.put("fiction", Arrays.asList("fiction", "ficción", "novel"));
        map.put("fantasy", Arrays.asList("fantasy", "fantasía"));
        map.put("science_fiction", Arrays.asList("science fiction", "sci-fi", "ciencia ficción"));
        map.put("thriller", Arrays.asList("thriller", "suspense", "suspenso"));
        map.put("mystery", Arrays.asList("mystery", "misterio"));
        map.put("horror", Arrays.asList("horror", "terror"));
        map.put("romance", Arrays.asList("romance", "romántico", "amor"));
        map.put("action", Arrays.asList("action", "acción"));
        map.put("drama", Arrays.asList("drama"));
        map.put("comedy", Arrays.asList("comedy", "comedia"));

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (lower.contains(keyword)) {
                    return entry.getKey();
                }
            }
        }

        return null;
    }

    private Map<String, Integer> normalizeCategories(List<String> mappedCategories) {
        List<String> allGenres = Arrays.asList(
                "accion", "fantasia", "romance", "terror",
                "ciencia ficcion", "drama", "comedia");

        Map<String, Integer> features = new HashMap<>();
        for (String genre : allGenres) {
            features.put(genre, mappedCategories.contains(genre) ? 10 : 0);
        }

        return features;
    }
}
