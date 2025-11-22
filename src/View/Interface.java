package View;

import Model.GrapheVille;
import Model.Habitation;
import Model.Arrete;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Interface {
    private JFrame fenetre = new JFrame("Collecteur de Déchets");
    private JScrollPane scrollPane;
    private JPanel panelPrincipal;
    int largeurPanel, hauteurPanel , marge, minX , minY;

    public void lancerFenetre() {
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setSize(800, 600);
        fenetre.setExtendedState(JFrame.MAXIMIZED_BOTH);
        fenetre.setVisible(true);
    }

    public void afficherGraphe(HashMap<String, Habitation> listeHabitations){
        if (fenetre.getContentPane().getComponentCount() > 0) {
            fenetre.getContentPane().removeAll();
        }

        // CALCUL DYNAMIQUE de la taille nécessaire
        int[] dimensions = calculerDimensions(listeHabitations);
        this.largeurPanel = dimensions[0];
        this.hauteurPanel = dimensions[1];
        marge = dimensions[2];
        minX = dimensions[3];
        minY = dimensions[4];

        panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dessinerArretesBFS(g, listeHabitations, marge, minX, minY);
            }
        };


        panelPrincipal.setLayout(null);
        panelPrincipal.setPreferredSize(new Dimension(this.largeurPanel, this.hauteurPanel));
        panelPrincipal.setBackground(Color.WHITE);

        // Positionner les cercles avec ajustement par rapport au minimum
        for (Habitation h : listeHabitations.values()) {
            int x = (int) (h.x - minX) + marge;
            int y = (int) (h.y - minY) + marge;
            panelPrincipal.add(new Cercle(x, y, 10, 10, h.numeroMaison + h.nomDeLaRue));
        }

        scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        fenetre.add(scrollPane);
        fenetre.revalidate();
        fenetre.repaint();
    }


    private int[] calculerDimensions(HashMap<String, Habitation> listeHabitations) {
        if (listeHabitations == null || listeHabitations.isEmpty()) {
            return new int[]{1000, 1000, 100, 0, 0};
        }

        // Trouver les coordonnées min et max
        float minX = Float.MAX_VALUE;
        float maxX = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = Float.MIN_VALUE;

        for (Habitation h : listeHabitations.values()) {
            if (h.x < minX) minX = h.x;
            if (h.x > maxX) maxX = h.x;
            if (h.y < minY) minY = h.y;
            if (h.y > maxY) maxY = h.y;
        }

        int marge = 100;
        int largeur = (int) (maxX - minX) + (2 * marge);
        int hauteur = (int) (maxY - minY) + (2 * marge);

        // Dimensions minimales
        largeur = Math.max(largeur, 1000);
        hauteur = Math.max(hauteur, 1000);

        System.out.println("Dimensions calculées: " + largeur + " x " + hauteur);
        System.out.println("Plage X: " + minX + " à " + maxX);
        System.out.println("Plage Y: " + minY + " à " + maxY);

        return new int[]{largeur, hauteur, marge, (int)minX, (int)minY};
    }

    private void dessinerArretesBFS(Graphics g, HashMap<String, Habitation> listeHabitations, int marge, int minX, int minY) {



        if (listeHabitations == null || listeHabitations.isEmpty()) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Habitation h : listeHabitations.values()) {
            int x1 = (int) (h.x - minX) + marge + 5;
            int y1 = (int) (h.y - minY) + marge + 5;

            for (Arrete arrete : h.listeVoisins) {
                Habitation voisin = arrete.arrivee;
                int x2 = (int) (voisin.x - minX) + marge + 5;
                int y2 = (int) (voisin.y - minY) + marge + 5;

                if(arrete.parcourue){
                    //on met en vert
                    g2d.setColor(Color.GREEN);
                    g2d.setStroke(new BasicStroke(3.0f));
                } else {
                    if (h.nomDeLaRue.equals(voisin.nomDeLaRue)) {
                        g2d.setColor(Color.BLUE);
                        g2d.setStroke(new BasicStroke(1.5f));
                    } else {
                        g2d.setColor(Color.RED);
                        g2d.setStroke(new BasicStroke(1.0f));
                    }
                }

                g2d.drawLine(x1, y1, x2, y2);
            }
        }
    }

    public void repaintOnly() {
        if (scrollPane != null) {
            scrollPane.repaint();
        }
    }

}