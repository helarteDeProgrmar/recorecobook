package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import models.Book;

public class ResultsPanel extends JPanel {
    private final VisualizerUI ui;
    private final DefaultListModel<Book> listModel;
    private final JList<Book> list;

    public ResultsPanel(VisualizerUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout(10, 10));

        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> lst,
                    Object value,
                    int idx,
                    boolean sel,
                    boolean focus) {
                super.getListCellRendererComponent(lst, value, idx, sel, focus);
                if (value instanceof Book) {
                    setText(((Book) value).toString());
                }
                return this;
            }
        });
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton again = new JButton("Otra consulta");
        again.addActionListener(e -> ui.restart());
        JButton close = new JButton("Cerrar");
        close.addActionListener(e -> ui.close());
        JPanel btn = new JPanel();
        btn.add(again);
        btn.add(close);
        add(btn, BorderLayout.SOUTH);
    }

    public void updateResults(List<Book> results) {
        listModel.clear();
        for (Book b : results) {
            listModel.addElement(b);
        }
    }
}
