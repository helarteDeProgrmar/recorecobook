package utils;

import java.io.*;
import java.util.*;
import models.Book;

public class LocalBooks {

    public static List<Book> chargeLocalBooks(String path) {
        System.out.println("%% LocalBooks | Searching in path: " + path);

        List<Book> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line = br.readLine();
            if (line == null)
                return list;

            String[] heads = line.split(",");

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length < 4)
                    continue;

                String title = fields[0].trim();
                String author = fields[1].trim();
                String mainGenre = fields[2].trim();
                Map<String, Integer> features = new LinkedHashMap<>();

                for (int i = 3; i < fields.length; i++) {
                    features.put(heads[i], Integer.parseInt(fields[i]));
                }

                list.add(new Book(title, author, mainGenre, features));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
