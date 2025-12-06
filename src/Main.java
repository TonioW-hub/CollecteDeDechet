import Model.*;
import View.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFileChooser;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Interface interfaceVille = new Interface();

        JFileChooser choixFichier = new JFileChooser(new File("."));
        int returnValue = choixFichier.showOpenDialog(null);
        String choixPath = "ville.txt";

        //Créer entrepot et le depot
        Habitation depot = new Habitation(1, "Depot");
        Habitation entrepot = new Habitation(1, "Entrepot");

        switch (returnValue) {
            case JFileChooser.APPROVE_OPTION:
                File files = choixFichier.getSelectedFile();
                choixPath = files.getAbsolutePath();
                break;
            case JFileChooser.CANCEL_OPTION:
                System.out.println("Opération annulée");
                break;

            case JFileChooser.ERROR_OPTION:
                System.out.println("Erreur lors de la sélection");
                break;
        }

        JFileChooser choixFichier2 = new JFileChooser(new File("."));
        int returnValue2 = choixFichier.showOpenDialog(null);
        String choixPath2 = "intersections.txt";

        switch (returnValue2) {
            case JFileChooser.APPROVE_OPTION:
                File files = choixFichier.getSelectedFile();
                choixPath2 = files.getAbsolutePath();
                break;
            case JFileChooser.CANCEL_OPTION:
                System.out.println("Opération annulée");
                break;

            case JFileChooser.ERROR_OPTION:
                System.out.println("Erreur lors de la sélection");
                break;
        }


        GrapheVille grapheVille = new GrapheVille();

        System.out.println("Chargement des intersections...");
        grapheVille.chargerIntersections(choixPath2);

        grapheVille.intersections.values().stream()
                .limit(500)
                .forEach(inter -> {
                    System.out.println(inter.lon + " / " + inter.lat + " → " + inter.rues);
                });


        HashMap<String, Habitation> listeHabitations = grapheVille.extraireHabitations(choixPath);

        // On enrichit maintenant le graphe avec les connexions issues des intersections
        grapheVille.connecterHabitationsParIntersections();

        System.out.println("=== DEBUG : quelques habitations et leurs voisins ===");
        grapheVille.listeHabitations.values().stream()
                .limit(10)
                .forEach(h -> {
                    System.out.println(h.numeroMaison + " " + h.nomDeLaRue + " :");
                    h.listeVoisinsHabitations.stream()
                            .limit(5)
                            .forEach(v -> System.out.println("   -> " + v.numeroMaison + " " + v.nomDeLaRue));
                });

        //On trouve le voisin de depot et entrepot
        int xmin = 100000000, ymin = 1000000000;
        Habitation voisinDeDepotEtEntrepot = new Habitation();
        voisinDeDepotEtEntrepot.x = xmin;
        voisinDeDepotEtEntrepot.y = ymin;
        for(Habitation h : listeHabitations.values()) {
            if(h.x < voisinDeDepotEtEntrepot.x && h.y < voisinDeDepotEtEntrepot.y) {
                voisinDeDepotEtEntrepot = h;

            }
        }
        depot.listeVoisinsHabitations.add(voisinDeDepotEtEntrepot);
        entrepot.listeVoisinsHabitations.add(voisinDeDepotEtEntrepot);
        depot.listeVoisins.add(new Arrete(depot, voisinDeDepotEtEntrepot));
        entrepot.listeVoisins.add(new Arrete(entrepot, voisinDeDepotEtEntrepot));

        interfaceVille.lancerFenetre();
        interfaceVille.afficherGraphe(listeHabitations);

        H01 h01 = new H01(grapheVille.listeHabitations, grapheVille.listeRues);


        ArrayList<Habitation> habitationTemporaires = new ArrayList<>();
        for(Habitation h : grapheVille.listeHabitations.values()) {
            habitationTemporaires.add(h);
        }

        int nbrRandom = (int) (Math.random() * 8) + 3;
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

        ArrayList<Habitation> habitationParcours = h01.touteVille(entrepot, interfaceVille);
        //ArrayList<Habitation> habitationParcours = h01.dijkstra10(habitationDepart, listeHabitationArrivee, interfaceVille);
        //ArrayList<Habitation> habitationParcours = h01.bfs10(habitationDepart, listeHabitationArrivee, interfaceVille);
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