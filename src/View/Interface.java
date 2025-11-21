package View;

import Model.GrapheVille;
import Model.Habitation;
import Model.Arrete;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Interface {
    private JFrame fenetre = new JFrame("Collecteur de Déchets");

    public void lancerFenetre(GrapheVille grapheVille, HashMap<Integer, Habitation> listeHabitations) {
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setSize(800, 600);
        fenetre.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Utiliser un JPanel personnalisé pour dessiner les arrêtes
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Dessiner les arrêtes entre les voisins
                dessinerArretes(g, listeHabitations);
            }
        };

        panelPrincipal.setLayout(null);
        panelPrincipal.setPreferredSize(new Dimension(4000, 4000));
        panelPrincipal.setBackground(Color.WHITE);

        Habitation initiale = listeHabitations.get(0);

        for (Habitation h : listeHabitations.values()) {
            panelPrincipal.add(new Cercle((int) (h.x - initiale.x) + 2000, (int) (h.y - initiale.y) + 2000, 10, 10, h.numeroMaison + h.nomDeLaRue));
            System.out.println("x : " + (h.x - initiale.x) + " y : " + (h.y - initiale.y));
        }

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        fenetre.add(scrollPane);
        fenetre.setVisible(true);
    }

    private void dessinerArretes(Graphics g, HashMap<Integer, Habitation> listeHabitations) {
        if (listeHabitations == null || listeHabitations.isEmpty()) return;

        Habitation initiale = listeHabitations.get(0);
        Graphics2D g2d = (Graphics2D) g;

        // Améliorer la qualité du dessin
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Parcourir toutes les habitations et leurs voisins
        for (Habitation h : listeHabitations.values()) {
            int x1 = (int) (h.x - initiale.x) + 2000 + 5; // +5 pour centrer (demi-taille du cercle)
            int y1 = (int) (h.y - initiale.y) + 2000 + 5;

            for (Arrete arrete : h.listeVoisins) {
                Habitation voisin = arrete.arrivee;
                int x2 = (int) (voisin.x - initiale.x) + 2000 + 5;
                int y2 = (int) (voisin.y - initiale.y) + 2000 + 5;

                // Choisir la couleur selon le type de voisin
                if (h.nomDeLaRue.equals(voisin.nomDeLaRue)) {
                    g2d.setColor(Color.BLUE); // Voisins de rue
                    g2d.setStroke(new BasicStroke(1.5f));
                } else {
                    g2d.setColor(Color.RED); // Voisins par distance
                    g2d.setStroke(new BasicStroke(1.0f));
                }

                // Dessiner la ligne entre l'habitation et son voisin
                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }
}