package util;

import java.io.*;
import java.util.*;
import models.Book;

public class LocalBooks {

    public static List<Book> chargeLocalBooks(String path) {
        List<Book> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Separar por comas
                String[] partes = line.split(",");
                if (partes.length >= 3) {
                    String title = partes[0].trim();
                    String author = partes[1].trim();
                    String genre = partes[2].trim();
                    Map<String, Integer> features = null;
                    list.add(new Book(title, author, genre, features));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
