package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import utils.HeaderImage;

public class AuthorsPanel extends JPanel {
    private final VisualizerUI ui;
    private final DefaultListModel<String> listModel;
    private final JList<String> list;
    private final JTextField inputField;

    public AuthorsPanel(VisualizerUI ui) {
        this.ui = ui;

        // Estructura: imagen arriba + contenido normal debajo
        setLayout(new BorderLayout());

        // Cabecera con imagen
        add(HeaderImage.buildHeaderImage(), BorderLayout.NORTH);

        // Contenido principal en panel anidado
        JPanel content = new JPanel(new BorderLayout(10, 10));

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        content.add(new JScrollPane(list), BorderLayout.CENTER);

        // Panel superior: campo + botón + mensaje
        JPanel top = new JPanel(new BorderLayout(5, 5));
        inputField = new JTextField();
        JButton add = new JButton("Añadir Autor");

        add.addActionListener(e -> {
            String t = inputField.getText().trim();
            if (!t.isEmpty()) {
                listModel.addElement(t);
                inputField.setText("");
            }
        });

        top.add(inputField, BorderLayout.CENTER);
        top.add(add, BorderLayout.EAST);

        // Mensaje informativo
        JLabel infoLabel = new JLabel(
            "<html><i>💡 Si no introduces autores, se realizará una búsqueda local con el archivo CSV.</i></html>");
        infoLabel.setForeground(new Color(70, 70, 70));
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(top, BorderLayout.NORTH);
        headerPanel.add(infoLabel, BorderLayout.SOUTH);

        content.add(headerPanel, BorderLayout.NORTH);

        // Botón inferior
        JButton next = new JButton("Siguiente");
        next.addActionListener(e -> {
            java.util.List<String> authors = Collections.list(listModel.elements());
            ui.onAuthorsSubmitted(authors);
        });
        JPanel btn = new JPanel();
        btn.add(next);
        content.add(btn, BorderLayout.SOUTH);

        add(content, BorderLayout.CENTER);
    }

    public void reset() {
        listModel.clear();
        inputField.setText("");
    }
}
