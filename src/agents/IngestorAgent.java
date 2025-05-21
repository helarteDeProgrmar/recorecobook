package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.List;

import models.Book;
import utils.GoogleBooksAPI;

public class IngestorAgent extends Agent {

    private GoogleBooksAPI api;

    @Override
    protected void setup() {
        api = new GoogleBooksAPI();

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    try {
                        String author = (String) msg.getContent();
                        System.out.println("** IngestorAgent | el autor es: " + author);

                        List<Book> books = api.searchBooksByAuthor(author);
                        for (Book book : books) {
                            System.out.println(book.toString());
                            System.out.println("One book more!");
                        }

                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContentObject((java.io.Serializable) books);

                        myAgent.send(reply);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    block();
                }
            }
        });
    }
}
