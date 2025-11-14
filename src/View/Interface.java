package View;

import Model.GrapheVille;
import Model.Habitation;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Interface {
    private JFrame fenetre = new JFrame("Collecteur de Déchets");

    public void lancerFenetre(GrapheVille grapheVille,HashMap<Integer, Habitation> listeHabitations){
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setSize(800, 600);
        fenetre.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(null);
        panelPrincipal.setPreferredSize(new Dimension(2000, 2000));

        panelPrincipal.setBackground(Color.WHITE);

        System.out.println("0");
        grapheVille.definirCoordGraphe();

        int i = 0;
        for(Habitation h : listeHabitations.values()){
            System.out.println(i);
            i++;
            panelPrincipal.add(new Cercle(h.coordX, h.coordY, 100, 100, h.nomDeLaRue));
        }

        JScrollPane scrollPane = new JScrollPane(panelPrincipal);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        fenetre.add(scrollPane);
        fenetre.setVisible(true);
    }
}
