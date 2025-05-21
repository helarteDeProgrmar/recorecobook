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
                                System.out.println("## GoogleBooksAPI | categoria: " + categoriesJSON.toString());
                                for (int j = 0; j < categoriesJSON.length(); j++) {
                                    String rawCategory = categoriesJSON.getString(j);
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

    private Map<String, Integer> normalizeCategories(List<String> inputCategories) {
        List<String> allGenres = Arrays.asList(
                "action", "fantasy", "romance", "horror",
                "science_fiction", "drama", "comedy");

        Map<String, Map<String, Integer>> similarityTable = new HashMap<>();

        similarityTable.put("action", Map.of(
                "action", 10, "fantasy", 4, "romance", 3, "horror", 5,
                "science_fiction", 5, "drama", 4, "comedy", 2));

        similarityTable.put("fantasy", Map.of(
                "action", 4, "fantasy", 10, "romance", 2, "horror", 1,
                "science_fiction", 7, "drama", 2, "comedy", 1));

        similarityTable.put("romance", Map.of(
                "action", 2, "fantasy", 3, "romance", 10, "horror", 1,
                "science_fiction", 2, "drama", 8, "comedy", 2));

        similarityTable.put("horror", Map.of(
                "action", 4, "fantasy", 3, "romance", 1, "horror", 10,
                "science_fiction", 6, "drama", 4, "comedy", 1));

        similarityTable.put("science_fiction", Map.of(
                "action", 6, "fantasy", 7, "romance", 2, "horror", 5,
                "science_fiction", 10, "drama", 3, "comedy", 1));

        similarityTable.put("drama", Map.of(
                "action", 3, "fantasy", 2, "romance", 6, "horror", 2,
                "science_fiction", 3, "drama", 10, "comedy", 4));

        similarityTable.put("comedy", Map.of(
                "action", 2, "fantasy", 1, "romance", 5, "horror", 1,
                "science_fiction", 2, "drama", 4, "comedy", 10));

        similarityTable.put("fiction", Map.of(
                "action", 6, "fantasy", 5, "romance", 3, "horror", 1,
                "science_fiction", 9, "drama", 4, "comedy", 1));

        similarityTable.put("mystery", Map.of(
                "action", 5, "fantasy", 2, "romance", 4, "horror", 6,
                "science_fiction", 2, "drama", 4, "comedy", 1));

        similarityTable.put("thriller", Map.of(
                "action", 5, "fantasy", 1, "romance", 4, "horror", 5,
                "science_fiction", 2, "drama", 4, "comedy", 1));

        Map<String, Integer> result = new HashMap<>();
        for (String genre : allGenres) {
            result.put(genre, 1);
        }

        for (String input : inputCategories) {
            Map<String, Integer> influence = similarityTable.get(input);
            if (influence != null) {
                for (String genre : allGenres) {
                    int current = result.get(genre);
                    int incoming = influence.getOrDefault(genre, 1);
                    result.put(genre, Math.max(current, incoming));
                }
            }
        }

        return result;
    }
}
