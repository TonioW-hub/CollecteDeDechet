package Model;

import java.util.*;

public class H01 {
    HashMap<Integer, Habitation> listeHabitations;
    HashMap<String, Rue> listeRues;

    public H01(HashMap<Integer, Habitation> listeHabitations, HashMap<String, Rue> listeRues) {
        this.listeHabitations = listeHabitations;
        this.listeRues = listeRues;
    }

    public ArrayList<Habitation> bfs1(Habitation depart, Habitation arrivee) {
        Queue<Parcours> fileAttente = new LinkedList<>();
        HashSet<Habitation> listeVisitees = new HashSet<>();

        Parcours parcoursInitiale = new Parcours();
        parcoursInitiale.id = "0";
        parcoursInitiale.listeHabitations.add(depart);
        fileAttente.add(parcoursInitiale);
        listeVisitees.add(depart);

        while(!fileAttente.isEmpty()) {
            Parcours parcours = fileAttente.poll();
            Habitation habitationActuelle = parcours.listeHabitations.get(parcours.listeHabitations.size() - 1);

            if(habitationActuelle.equals(arrivee)) {
                System.out.println("Chemin trouvé avec " + parcours.listeHabitations.size() + " étapes");
                return parcours.listeHabitations;
            }

            int i = 0;
            for(Arrete arrete : habitationActuelle.listeVoisins) {
                Habitation voisin = arrete.arrivee;

                if(!listeVisitees.contains(voisin)) {
                    Parcours nouveauParcours = new Parcours();
                    nouveauParcours.id = parcours.id + "." + i;

                    nouveauParcours.listeHabitations = new ArrayList<>(parcours.listeHabitations);
                    nouveauParcours.listeHabitations.add(voisin);

                    fileAttente.add(nouveauParcours);
                    listeVisitees.add(voisin);
                    i++;
                }
            }
        }

        System.out.println("Aucun chemin trouvé après " + listeVisitees.size() + " maisons explorées");
        return null;
    }

    public void testerConnectivite(Habitation depart, Habitation arrivee) {
        System.out.println("=== TEST DE CONNECTIVITÉ ===");
        System.out.println("Départ: " + depart.nomDeLaRue + " | Arrivée: " + arrivee.nomDeLaRue);

        // Test 1: Vérifier si ce sont les mêmes objets
        System.out.println("Même objet? " + (depart == arrivee));
        System.out.println("Égaux? " + depart.equals(arrivee));

        // Test 2: Vérifier les voisins directs
        System.out.println("\n--- Voisins du départ ---");
        for (Arrete arrete : depart.listeVoisins) {
            System.out.println(depart.nomDeLaRue + " -> " + arrete.arrivee.nomDeLaRue);
            if (arrete.arrivee.equals(arrivee)) {
                System.out.println("★ VOISIN DIRECT TROUVÉ!");
            }
        }

        System.out.println("\n--- Voisins de l'arrivée ---");
        for (Arrete arrete : arrivee.listeVoisins) {
            System.out.println(arrivee.nomDeLaRue + " -> " + arrete.arrivee.nomDeLaRue);
        }

        // Test 3: Composante connexe du départ
        Set<Habitation> composanteConnexe = new HashSet<>();
        Queue<Habitation> file = new LinkedList<>();
        file.add(depart);
        composanteConnexe.add(depart);

        while (!file.isEmpty()) {
            Habitation current = file.poll();
            for (Arrete arrete : current.listeVoisins) {
                if (composanteConnexe.add(arrete.arrivee)) {
                    file.add(arrete.arrivee);
                }
            }
        }

        System.out.println("\n--- RÉSULTATS ---");
        System.out.println("Taille composante connexe départ: " + composanteConnexe.size());
        System.out.println("Arrivée dans composante connexe? " + composanteConnexe.contains(arrivee));

        if (!composanteConnexe.contains(arrivee)) {
            System.out.println("❌ DÉPART ET ARRIVÉE DANS DES COMPOSANTES DIFFÉRENTES!");
            System.out.println("Habitations accessibles depuis le départ: " + composanteConnexe.size());
            System.out.println("Habitations totales: 2991");
            System.out.println("Habitations INACCESSIBLES: " + (2991 - composanteConnexe.size()));
        }
    }
}
