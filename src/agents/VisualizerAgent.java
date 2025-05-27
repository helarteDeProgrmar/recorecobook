package agents;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.lang.acl.MessageTemplate;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.CyclicBehaviour;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import models.Book;
import ui.VisualizerUI;
import javax.swing.SwingUtilities;

public class VisualizerAgent extends Agent {

    private VisualizerUI ui;

    protected void setup() {
        System.out.println("&& VisualizerAgent | Started");
        SwingUtilities.invokeLater(() -> {
            ui = new VisualizerUI(this);
            ui.setVisible(true);
        });

        addBehaviour(new CyclicBehaviour() {
            private MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);

            @Override
            public void action() {
                ACLMessage msg = receive(mt);
                if (msg != null) {
                    try {
                        Object content = msg.getContentObject();
                        if (content instanceof List) {
                            @SuppressWarnings("unchecked")
                            List<Book> recs = (List<Book>) content;
                            // Actualizamos la UI en el EDT
                            SwingUtilities.invokeLater(() -> ui.showResults(recs));
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

    public void sendPreferencesAndAuthors(Map<String, Integer> prefs, List<String> authors, int numberBooks) {
        System.out.println("&& VisualizerAgent |  Hi from sendPreferences!");
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID("recommender", AID.ISLOCALNAME));
        try {
            msg.setContentObject(new Object[] { prefs, authors, numberBooks });
            send(msg);
            System.out.println("&& VisualizerAgent | Preferences and authors sent to recommender.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
