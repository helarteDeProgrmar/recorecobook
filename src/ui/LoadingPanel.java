package ui;

import javax.swing.*;
import java.awt.*;

public class LoadingPanel extends JPanel {
    public LoadingPanel() {
        setLayout(new BorderLayout(10, 10));
        JLabel lbl = new JLabel("Cargando resultados...", SwingConstants.CENTER);
        add(lbl, BorderLayout.CENTER);
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        add(bar, BorderLayout.SOUTH);
    }
}
