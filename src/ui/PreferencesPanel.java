package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PreferencesPanel extends JPanel {
    private final VisualizerUI ui;
    private final Map<String, JSlider> sliders;
    private static final String[] GENRES = {
            "action", "fantasy", "romance", "horror",
            "science_fiction", "drama", "comedy"
    };

    public PreferencesPanel(VisualizerUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout(10, 10));

        JPanel sliderPanel = new JPanel(new GridLayout(GENRES.length, 2, 5, 5));
        sliders = new LinkedHashMap<>();
        for (String genre : GENRES) {
            sliderPanel.add(new JLabel(genre));
            JSlider s = new JSlider(1, 10, 5);
            s.setMajorTickSpacing(1);
            s.setPaintTicks(true);
            s.setPaintLabels(true);
            sliders.put(genre, s);
            sliderPanel.add(s);
        }
        add(sliderPanel, BorderLayout.CENTER);

        JButton next = new JButton("Next");
        next.addActionListener(e -> {
            Map<String, Integer> prefs = new LinkedHashMap<>();
            for (String g : GENRES) {
                prefs.put(g, sliders.get(g).getValue());
            }
            ui.onPreferencesSubmitted(prefs);
        });
        JPanel btn = new JPanel();
        btn.add(next);
        add(btn, BorderLayout.SOUTH);
    }

    public void reset() {
        for (JSlider s : sliders.values()) {
            s.setValue(5);
        }
    }
}
