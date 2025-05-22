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
                        Object[] container = (Object[]) msg.getContentObject();
                        List<String> authors = (List<String>) container[0];
                        System.out.println(authors.toString());

                        List<Book> books = api.fetchBooks(authors);

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
