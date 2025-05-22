package agents;

import jade.core.Agent;
import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.lang.acl.MessageTemplate;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.CyclicBehaviour;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import models.Book;

public class VisualizerAgent extends Agent {

    protected void setup() {
        System.out.println("VisualizerAgent started.");

        addBehaviour(new OneShotBehaviour() {
            public void action() {
                Scanner scanner = new Scanner(System.in);

                // Genres to rate
                String[] genres = {
                        "action", "fantasy", "romance", "horror", "science_fiction", "drama", "comedy"
                };

                Map<String, Integer> preferences = new HashMap<>();

                System.out.println("Rate your interest in the following genres (1 to 10):");

                for (String genre : genres) {
                    int value = -1;
                    while (value < 1 || value > 10) {
                        System.out.print(genre + ": ");
                        try {
                            value = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            value = -1;
                        }
                    }
                    preferences.put(genre, value);
                }

                System.out.print("Enter an author to search: ");
                String string_authors = scanner.nextLine();

                List<String> authors = Arrays.stream(string_authors.split(";")).map(String::trim)
                        .collect(Collectors.toList());
                System.out.println(authors.toString());
                // Send data to RecommenderAgent
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                msg.addReceiver(new AID("recommender", AID.ISLOCALNAME));
                try {
                    msg.setContentObject(new Object[] { preferences, authors });
                    send(msg);
                    System.out.println("Preferences and author sent to recommender.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Behavior to receive recommendations
        addBehaviour(new CyclicBehaviour() {
            public void action() {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage reply = receive(mt);

                if (reply != null) {
                    try {
                        Object content = reply.getContentObject();
                        if (content instanceof List<?>) {
                            System.out.println("\nRecommended books:");
                            for (Object obj : (List<?>) content) {
                                if (obj instanceof Book) {
                                    System.out.println(((Book) obj).toString());
                                }
                            }
                            System.out.println("\n--- End of recommendations ---\n");
                        }
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                } else {
                    block();
                }
            }
        });
    }
}
