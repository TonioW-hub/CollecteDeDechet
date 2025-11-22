import Model.*;
import View.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        Interface interfaceVille = new Interface();

        //TOUTE LA PARTIE LOGIQUE
        GrapheVille grapheVille = new GrapheVille();
        HashMap<Integer, Habitation> listeHabitations = grapheVille.extraireHabitations("villes.txt");

        interfaceVille.lancerFenetre(grapheVille, listeHabitations);

        H01 h01 = new H01(grapheVille.listeHabitations, grapheVille.listeRues);

        ArrayList<Habitation> habitationParcours = h01.bfs1(grapheVille.listeHabitations.get(0), grapheVille.listeHabitations.get(450));
        //h01.testerConnectivite(grapheVille.listeHabitations.get(0), grapheVille.listeHabitations.get(2000));

        if(habitationParcours != null) {
            for(Habitation h : habitationParcours) {
                System.out.print("=> " + h.nomDeLaRue + " " + h.numeroMaison);
            }
            System.out.println();
        }

        System.out.println(grapheVille.listeHabitations.size());
    }
}