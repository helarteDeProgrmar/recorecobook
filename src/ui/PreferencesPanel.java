package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import utils.HeaderImage;

public class PreferencesPanel extends JPanel {
    private final VisualizerUI ui;
    private final Map<String, JSlider> sliders;
    private final JSpinner numBooksSpinner;
    private static final String[] GENRES = {
        "Action", "Fantasy", "Romance", "Horror",
        "Science_fiction", "Drama", "Comedy"
    };

    public PreferencesPanel(VisualizerUI ui) {
        this.ui = ui;

        // Layout general: imagen arriba + contenido debajo
        setLayout(new BorderLayout());

        add(HeaderImage.buildHeaderImage(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        sliders = new LinkedHashMap<>();

        JPanel sliderPanel = new JPanel(new GridLayout(GENRES.length, 2, 5, 5));
        for (String genre : GENRES) {
            JLabel label = new JLabel(genre);
            label.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 10));
            sliderPanel.add(label);

            JSlider s = new JSlider(1, 10, 5);
            s.setMajorTickSpacing(1);
            s.setPaintTicks(true);
            s.setPaintLabels(true);
            JPanel sliderWrapper = new JPanel(new BorderLayout());
            sliderWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));
            sliderWrapper.add(s, BorderLayout.CENTER);
            sliders.put(genre, s);
            sliderPanel.add(sliderWrapper);
        }

        content.add(sliderPanel, BorderLayout.CENTER);

        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        spinnerPanel.add(new JLabel("Número de libros a mostrar:"));
        numBooksSpinner = new JSpinner(new SpinnerNumberModel(4, 1, 100, 1));
        spinnerPanel.add(numBooksSpinner);
        content.add(spinnerPanel, BorderLayout.NORTH);

        JButton next = new JButton("Siguiente");
        next.addActionListener(e -> {
            Map<String, Integer> prefs = new LinkedHashMap<>();
            for (String g : GENRES) {
                prefs.put(g, sliders.get(g).getValue());
            }
            int numberBooks = (Integer) numBooksSpinner.getValue();
            ui.onPreferencesSubmitted(prefs, numberBooks);
        });

        JPanel btn = new JPanel();
        btn.add(next);
        content.add(btn, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    public void reset() {
        for (JSlider s : sliders.values()) {
            s.setValue(5);
        }
    }
}
