package ui;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import models.Book;

public class ResultsPanel extends JPanel {
    private final VisualizerUI ui;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ResultsPanel(VisualizerUI ui) {
        this.ui = ui;
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = { "Título", "Autor", "Género", "Características" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

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
        tableModel.setRowCount(0);
        for (Book b : results) {
            String features = formatFeatures(b.getFeatures());
            Object[] row = { b.getTitle(), b.getAuthor(), b.getGenre(), features };
            tableModel.addRow(row);
        }
    }

    private String formatFeatures(Map<String, Integer> features) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : features.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("  ");
        }
        return sb.toString().trim();
    }
}
