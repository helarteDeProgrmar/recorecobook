package agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import models.Book;

public class RecommenderAgent extends Agent {

    private Map<String, Integer> userPreferences = null;
    private String visualizerAID = null;

    protected void setup() {
        System.out.println("RecommenderAgent started.");

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
                    visualizerAID = msg.getSender().getLocalName();

                    // Send author to IngestorAgent
                    ACLMessage toIngestor = new ACLMessage(ACLMessage.REQUEST);
                    toIngestor.addReceiver(new AID("ingestor", AID.ISLOCALNAME));
                    toIngestor.setContent(authors.get(0));
                    send(toIngestor);

                    System.out.println("Received user preferences and author. Request sent to IngestorAgent.");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void handleIngestorResponse(ACLMessage msg) throws UnreadableException, IOException {
                if (userPreferences == null) {
                    System.out.println("User preferences not initialized.");
                    return;
                }

                Object content = msg.getContentObject();
                if (!(content instanceof List<?>))
                    return;

                List<Book> books = (List<Book>) content;
                List<Book> topBooks = getTopRecommendations(books, userPreferences, 5);

                // Send results to VisualizerAgent
                ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                reply.addReceiver(new AID(visualizerAID, AID.ISLOCALNAME));
                reply.setContentObject((Serializable) topBooks);
                send(reply);

                System.out.println("Top 5 recommendations sent to VisualizerAgent.");
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
