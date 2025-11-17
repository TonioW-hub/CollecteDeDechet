package View;

import javax.swing.*;
import java.awt.*;

public class Cercle extends JPanel {
    private String nomSommet;

    public Cercle(int x, int y, int largeur, int hauteur, String nomSommet){
        this.nomSommet = nomSommet;

        // Positionner le composant
        setBounds(x, y, largeur, hauteur);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner RELATIVEMENT au composant (0,0)
        g.drawOval(0, 0, getWidth(), getHeight());

        FontMetrics fm = g.getFontMetrics();
        int texteLargeur = fm.stringWidth(nomSommet);

        g.setColor(Color.BLACK);
        // Positionner le texte relativement au composant
        g.drawString(nomSommet, getWidth()/2 - texteLargeur/2, getHeight()/2);
    }
}