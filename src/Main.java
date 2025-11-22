import Model.*;
import View.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Interface interfaceVille = new Interface();

        //TOUTE LA PARTIE LOGIQUE
        GrapheVille grapheVille = new GrapheVille();
        HashMap<String, Habitation> listeHabitations = grapheVille.extraireHabitations("villes.txt");

        interfaceVille.lancerFenetre();
        interfaceVille.afficherGraphe(listeHabitations);

        H01 h01 = new H01(grapheVille.listeHabitations, grapheVille.listeRues);


        ArrayList<Habitation> habitationTemporaires = new ArrayList<>();
        for(Habitation h : grapheVille.listeHabitations.values()) {
            habitationTemporaires.add(h);
        }

        int nbrRandom = (int) (Math.random() * 11) + 1;
        ArrayList<Habitation> listeHabitationArrivee = new ArrayList<>();

        for(int i = 0; i < nbrRandom; i++) {
            int nbrRandom2 = (int) (Math.random() * listeHabitations.size());
            listeHabitationArrivee.add(habitationTemporaires.get(nbrRandom2));
            habitationTemporaires.remove(nbrRandom2);
        }
        Habitation habitationDepart = listeHabitationArrivee.removeFirst();
        System.out.println("Depart : " + habitationDepart.nomDeLaRue + " " + habitationDepart.numeroMaison);
        System.out.println("Arrivee : ");
        for(Habitation h : listeHabitationArrivee) {
            System.out.println(h.nomDeLaRue + " " + h.numeroMaison);
        }

        ArrayList<Habitation> habitationParcours = h01.bfs10(habitationDepart, listeHabitationArrivee, interfaceVille);
        //ArrayList<Habitation> habitationParcours = h01.bfs1(grapheVille.listeHabitations.get(habitationDepart), grapheVille.listeHabitations.get(habitationArrivee), interfaceVille);
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

/* grapheVille.afficherHabitations();
        String habitationDepart, habitationArrivee;
        habitationDepart = "169.0Avenue Henri Ginoux";
        if(!listeHabitations.containsKey(habitationDepart)) {
            System.out.println("Maison inconnue retente ta chance : ");
            habitationDepart = sc.nextLine();
        }

        System.out.println("Choisir destination arrivee : ");
        habitationArrivee = sc.nextLine();
        if(!listeHabitations.containsKey(habitationArrivee)) {
            System.out.println("Maison inconnue retente ta chance : ");
            habitationArrivee = sc.nextLine();
        }
*/