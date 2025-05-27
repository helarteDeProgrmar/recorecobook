package utils;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class HeaderImage {
    public static JLabel buildHeaderImage() {
        URL imageUrl = HeaderImage.class.getClassLoader().getResource("Media/Cabecera.png");
        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            // Escalar la imagen a alto 80px manteniendo proporción
            Image scaled = icon.getImage().getScaledInstance(-1, 80, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaled), SwingConstants.CENTER);
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            return label;
        } else {
            JLabel fallback = new JLabel("Cabecera no disponible", SwingConstants.CENTER);
            fallback.setFont(new Font("SansSerif", Font.BOLD, 16));
            return fallback;
        }
    }
}
