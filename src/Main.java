import Model.GrapheVille;
import Model.Habitation;
import View.*;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        Interface interfaceVille = new Interface();

        //TOUTE LA PARTIE LOGIQUE
        GrapheVille grapheVille = new GrapheVille();
        HashMap<Integer, Habitation> listeHabitations = grapheVille.extraireHabitations("villes.txt");
        grapheVille.afficherHabitations();

        interfaceVille.lancerFenetre(grapheVille, listeHabitations);
    }
}