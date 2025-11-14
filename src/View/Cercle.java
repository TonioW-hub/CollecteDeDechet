package View;

import javax.swing.*;
import java.awt.*;

public class Cercle extends JPanel {
    private int x, y, largeur, hauteur;
    String nomSommet;

    public Cercle(int x, int y, int largeur, int hauteur, String nomSommet){
        this.x = x;
        this.y = y;
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.nomSommet = nomSommet;

        setBounds(x, y, largeur*2, hauteur*2);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawOval(x, y, largeur, hauteur);

        FontMetrics fm = g.getFontMetrics();
        int texteLargeur = fm.stringWidth(nomSommet);

        g.setColor(Color.BLACK);
        g.drawString(nomSommet, x + largeur/2 - texteLargeur/2, y + hauteur/2);
    }
}
