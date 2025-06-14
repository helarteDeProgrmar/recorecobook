package ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import agents.VisualizerAgent;
import models.Book;
import utils.HeaderImage;


public class VisualizerUI extends JFrame {
    private final VisualizerAgent agent;
    private final CardLayout cardLayout;
    private final JPanel cards;
    private final PreferencesPanel preferencesPanel;
    private final AuthorsPanel authorsPanel;
    private final LoadingPanel loadingPanel;
    private final ResultsPanel resultsPanel;

    private Map<String, Integer> preferences;
    private java.util.List<String> authors;
    private int numberBooks;

    public VisualizerUI(VisualizerAgent agent) {
        super("Book Recommender");
        this.agent = agent;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        preferencesPanel = new PreferencesPanel(this);
        authorsPanel = new AuthorsPanel(this);
        loadingPanel = new LoadingPanel();
        resultsPanel = new ResultsPanel(this);

        cards.add(preferencesPanel, "PREFERENCES");
        cards.add(authorsPanel, "AUTHORS");
        cards.add(loadingPanel, "LOADING");
        cards.add(resultsPanel, "RESULTS");

        getContentPane().add(cards);
        showPreferences();
    }

    // Llamado por PreferencesPanel
    public void onPreferencesSubmitted(Map<String, Integer> prefs, int numberBooks) {
        this.preferences = prefs;
        this.numberBooks = numberBooks;
        showAuthors();
    }

    // Llamado por AuthorsPanel
    public void onAuthorsSubmitted(java.util.List<String> authors) {
        this.authors = authors;
        showLoading();
        agent.sendPreferencesAndAuthors(preferences, authors, numberBooks);
    }

    // Llamado por el agente cuando llegan los resultados
    public void showResults(List<Book> results) {
        resultsPanel.updateResults(results);
        cardLayout.show(cards, "RESULTS");
    }

    // Acciones de navegación
    public void showPreferences() {
        cardLayout.show(cards, "PREFERENCES");
    }

    public void showAuthors() {
        cardLayout.show(cards, "AUTHORS");
    }

    public void showLoading() {
        cardLayout.show(cards, "LOADING");
    }

    public void restart() {
        preferencesPanel.reset();
        authorsPanel.reset();
        showPreferences();
    }

    public void close() {
        System.exit(0);
    }
}
