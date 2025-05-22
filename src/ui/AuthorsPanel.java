package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class AuthorsPanel extends JPanel {
    private final VisualizerUI ui;
    private final DefaultListModel<String> listModel;
    private final JList<String> list;
    private final JTextField inputField;

    public AuthorsPanel(VisualizerUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout(10, 10));

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        add(new JScrollPane(list), BorderLayout.CENTER);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        inputField = new JTextField();
        JButton add = new JButton("Add Author");
        add.addActionListener(e -> {
            String t = inputField.getText().trim();
            if (!t.isEmpty()) {
                listModel.addElement(t);
                inputField.setText("");
            }
        });
        top.add(inputField, BorderLayout.CENTER);
        top.add(add, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JButton next = new JButton("Next");
        next.addActionListener(e -> {
            java.util.List<String> authors = Collections.list(listModel.elements());
            ui.onAuthorsSubmitted(authors);
        });
        JPanel btn = new JPanel();
        btn.add(next);
        add(btn, BorderLayout.SOUTH);
    }

    public void reset() {
        listModel.clear();
        inputField.setText("");
    }
}
