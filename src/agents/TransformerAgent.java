package agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import models.Book;
import utils.KeywordLoader;

public class TransformerAgent extends Agent {

     private Map<String, List<String>> genreKeywords = new HashMap<>();

    // private static final Map<String, List<String>> genreKeywords = Map.of(
    //     "action", List.of("battle", "fight", "explosion", "war", "gun", "soldier"),
    //     "fantasy", List.of("dragon", "magic", "wizard", "kingdom", "spell", "elf"),
    //     "romance", List.of("love", "kiss", "romantic", "heart", "relationship"),
    //     "horror", List.of("ghost", "haunted", "blood", "scream", "terror", "monster"),
    //     "science_fiction", List.of("robot", "alien", "space", "future", "technology"),
    //     "drama", List.of("family", "tragedy", "life", "conflict", "emotional"),
    //     "comedy", List.of("funny", "joke", "laugh", "humor", "comedic")
    // );

    @Override
    protected void setup() {
        System.out.println("** TransformerAgent | Started");

        try {
            genreKeywords = KeywordLoader.loadGenreKeywords("data/genre_keywords.json");
            System.out.println("** TransformerAgent | Loaded keywords from JSON");
        } catch (Exception e) {
            System.err.println("** TransformerAgent | Error loading keywords JSON");
            e.printStackTrace();
        }

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    try {
                        Object content = msg.getContentObject();
                        if (!(content instanceof List<?>)) return;

                        @SuppressWarnings("unchecked")
                        List<Book> books = (List<Book>) content;

                        System.out.println("** TransformerAgent | Received " + books.size() + " books");

                        for (Book book : books) {
                            transformBookFeatures(book);
                        }

                        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                        reply.addReceiver(new AID("recommender", AID.ISLOCALNAME));
                        reply.setContentObject((Serializable) books);
                        myAgent.send(reply);

                        System.out.println("** TransformerAgent | Sent transformed books");

                    } catch (UnreadableException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void transformBookFeatures(Book book) {
        String description = book.getDescription();
        if (description == null) return;

        Map<String, Integer> features = book.getFeatures();
        if (features == null) return;

        String[] words = description.toLowerCase().replaceAll("[^a-zA-Z ]", "").split("\\s+");

        for (String word : words) {
            for (Map.Entry<String, List<String>> entry : genreKeywords.entrySet()) {
                String genre = entry.getKey();
                List<String> keywords = entry.getValue();

                if (keywords.contains(word)) {
                    int currentScore = features.getOrDefault(genre, 0);
                    features.put(genre, Math.min(10, currentScore + 1));
                }
            }
        }
    }
}

