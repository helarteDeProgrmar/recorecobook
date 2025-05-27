package agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import models.Book;
import utils.GoogleBooksAPI;

public class RecommenderAgent extends Agent {

    private Map<String, Integer> userPreferences = null;
    private int userNumberBooks = 5;
    private String visualizerAID = null;

    protected void setup() {
        System.out.println("## RecommenderAgent | Started");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    try {
                        switch (msg.getPerformative()) {
                            case ACLMessage.REQUEST:
                                handleVisualizerRequest(msg);
                                break;
                            case ACLMessage.INFORM:
                                handleIngestorResponse(msg);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    block();
                }
            }

            private void handleVisualizerRequest(ACLMessage msg) throws IOException, ClassNotFoundException {
                try {
                    Object[] content = (Object[]) msg.getContentObject();
                    userPreferences = (Map<String, Integer>) content[0];
                    List<String> authors = (List<String>) content[1];
                    userNumberBooks = (Integer) content[2];
                    visualizerAID = msg.getSender().getLocalName();

                    // Send author to IngestorAgent
                    ACLMessage toIngestor = new ACLMessage(ACLMessage.REQUEST);
                    toIngestor.addReceiver(new AID("ingestor", AID.ISLOCALNAME));
                    toIngestor.setContentObject(new Object[] { authors });
                    System.out.println("## RecommenderAgent | Receive the authors: " + authors.toString());
                    System.out.println("## RecommenderAgent | Receive the userPreferences: " + userPreferences.toString());
                    send(toIngestor);
                    System.out.println("## RecommenderAgent | Send authors to ingestor");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void handleIngestorResponse(ACLMessage msg) throws UnreadableException, IOException {
                if (userPreferences == null) {
                    System.out.println("## RecommenderAgent | User preferences not initialized:" + userPreferences.toString());
                    return;
                }

                System.out.println("## RecommenderAgent | Received the content");
                Object content = msg.getContentObject();
                if (!(content instanceof List<?>))
                    return;

                List<Book> books = (List<Book>) content;
                // List<Book> topBooks = getTopRecommendations(books, userPreferences, userNumberBooks);
                List<Book> topBooks = books.stream()
                .sorted(Comparator.comparingInt(b -> b.distance(userPreferences)))
                .limit(userNumberBooks)
                .toList();
                System.out.println("## RecommenderAgent | Top  recommendations sent to VisualizerAgent:"+ topBooks.toString());
                for(Book b: topBooks) {
                    if (b.getDescription().equals("-"))
                        b.setDescription(GoogleBooksAPI.getBookDescriptionByTitle(b.getTitle()));
                        System.out.println("## RecommenderAgent | Distance beetwen preference and tops:"+ b.distance(userPreferences));
                }

                // Send results to VisualizerAgent
                ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                reply.addReceiver(new AID(visualizerAID, AID.ISLOCALNAME));
                reply.setContentObject((Serializable) topBooks);
                send(reply);
            }
        });
    }

    private List<Book> getTopRecommendations(List<Book> books, Map<String, Integer> userPrefs, int topN) {
        // List of books with distances
        List<Map.Entry<Book, Integer>> scored = new ArrayList<>();

        for (Book book : books) {
            Map<String, Integer> features = book.getFeatures();
            int distance = 0;

            for (String genre : userPrefs.keySet()) {
                int userScore = userPrefs.getOrDefault(genre, 0);
                int bookScore = features.getOrDefault(genre, 0);
                distance += Math.abs(userScore - bookScore);
            }

            scored.add(new AbstractMap.SimpleEntry<>(book, distance));
        }

        // Sort by ascending distance (lower = more similar)
        scored.sort(Comparator.comparingInt(Map.Entry::getValue));

        List<Book> topBooks = new ArrayList<>();
        for (int i = 0; i < Math.min(topN, scored.size()); i++) {
            topBooks.add(scored.get(i).getKey());
        }

        return topBooks;
    }
}
