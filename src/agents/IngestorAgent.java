package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.List;

import models.Book;
import utils.GoogleBooksAPI;
import utils.LocalBooks;

public class IngestorAgent extends Agent {

    @Override
    protected void setup() {

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    try {
                        Object[] container = (Object[]) msg.getContentObject();
                        List<String> authors = (List<String>) container[0];
                        List<Book> books = null;
                        if (authors.size() == 0) {
                            System.out.println("IngestorAgent | Local search: " + authors.toString());
                            books = LocalBooks.chargeLocalBooks("data/libros.csv");
                            System.out.println(books.get(0).toString());
                        } else {
                            System.out.println("IngestorAgent | Searching: " + authors.toString());
                            books = GoogleBooksAPI.fetchBooks(authors);
                        }

                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContentObject((java.io.Serializable) books);

                        myAgent.send(reply);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    block();
                }
            }
        });
    }
}
