package utils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileReader;
import java.util.*;

public class KeywordLoader {

    public static Map<String, List<String>> loadGenreKeywords(String filePath) {
        Map<String, List<String>> keywordsMap = new HashMap<>();
        try {
            FileReader reader = new FileReader(filePath);
            StringBuilder sb = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                sb.append((char) c);
            }
            reader.close();

            JSONObject json = new JSONObject(sb.toString());

            for (String genre : json.keySet()) {
                JSONArray wordsArray = json.getJSONArray(genre);
                List<String> words = new ArrayList<>();
                for (int i = 0; i < wordsArray.length(); i++) {
                    words.add(wordsArray.getString(i));
                }
                keywordsMap.put(genre, words);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return keywordsMap;
    }
}

