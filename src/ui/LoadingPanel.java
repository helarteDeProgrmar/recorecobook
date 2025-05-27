package ui;

import javax.swing.*;
import java.awt.*;
import utils.HeaderImage;


public class LoadingPanel extends JPanel {
    public LoadingPanel() {
        setLayout(new BorderLayout(10, 10));
        add(utils.HeaderImage.buildHeaderImage(), BorderLayout.NORTH);

        JLabel lbl = new JLabel("Cargando resultados...", SwingConstants.CENTER);
        add(lbl, BorderLayout.CENTER);
        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        add(bar, BorderLayout.SOUTH);
    }
}
