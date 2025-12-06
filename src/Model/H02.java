package Model;

import View.Interface;

import java.util.*;

public class H02 {
    HashMap<String, Habitation> listeHabitations;
    HashMap<String, Rue> listeRues;

    public H02(HashMap<String, Habitation> listeHabitations, HashMap<String, Rue> listeRues) {
        this.listeHabitations = listeHabitations;
        this.listeRues = listeRues;
    }

    public ArrayList<Habitation> touteVille(Habitation depart, Interface interfaceVille) {

        Queue<Habitation> fileAttente = new LinkedList<>();
        HashSet<Habitation> listeVisitees = new HashSet<>();

        Parcours parcours = new Parcours();

        fileAttente.add(depart);
        listeVisitees.add(depart);
        parcours.listeHabitations.add(depart);

        while(!fileAttente.isEmpty()) {
            Habitation habitationActuelle = fileAttente.poll();
            boolean voisinTrouve = false;

            for (Arrete arrete : habitationActuelle.listeVoisins) {
                Habitation voisin = arrete.arrivee;

                if (voisin.nomDeLaRue.equals(habitationActuelle.nomDeLaRue) && !listeVisitees.contains(voisin)) {
                    fileAttente.add(voisin);
                    listeVisitees.add(voisin);
                    parcours.listeHabitations.add(voisin);
                    visualisation(parcours, interfaceVille);
                    voisinTrouve = true;
                    break;
                }
            }

            if (!voisinTrouve) {
                for (Arrete arrete : habitationActuelle.listeVoisins) {
                    Habitation voisin = arrete.arrivee;

                    if (!listeVisitees.contains(voisin)) {
                        fileAttente.add(voisin);
                        listeVisitees.add(voisin);
                        parcours.listeHabitations.add(voisin);
                        visualisation(parcours, interfaceVille);
                        voisinTrouve = true;
                        break;
                    }
                }
            }

            if (!voisinTrouve) {//Petit Bfs pour trouver le chemin le plus court vers l'habitation non marqué la plus proche

                ArrayList<Habitation> listeVisiteBFS = new ArrayList<>();
                Queue<Parcours> fileAttenteBFS = new LinkedList<>();

                Parcours parcoursInitial = new Parcours();
                parcoursInitial.listeHabitations.add(habitationActuelle);

                fileAttenteBFS.add(parcoursInitial);
                listeVisiteBFS.add(habitationActuelle);

                while (!fileAttenteBFS.isEmpty()) {
                    Parcours parcoursBFS = fileAttenteBFS.poll();
                    Habitation habitationActuelleBFS = parcoursBFS.listeHabitations.get(parcoursBFS.listeHabitations.size() - 1);

                    for (Arrete a : habitationActuelleBFS.listeVoisins) {
                        Habitation voisinBfs = a.arrivee;

                        if (!listeVisitees.contains(voisinBfs)) {
                            fileAttente.add(voisinBfs);
                            listeVisitees.add(voisinBfs);
                            parcours.listeHabitations.add(voisinBfs);
                            visualisation(parcours, interfaceVille);
                            fileAttenteBFS.clear();
                            break;
                        }

                        if (!listeVisiteBFS.contains(voisinBfs)) {
                            Parcours nouveauParcours = new Parcours();

                            nouveauParcours.listeHabitations = new ArrayList<>(parcoursBFS.listeHabitations);
                            nouveauParcours.listeHabitations.add(voisinBfs);

                            fileAttenteBFS.add(nouveauParcours);
                            listeVisiteBFS.add(voisinBfs);
                        }
                    }
                }
            }
        }

        return parcours.listeHabitations;
    }

    public void visualisation(Parcours nouveauParcours, Interface interfaceVille){
        HashSet<Arrete> arreteParcours = new HashSet<>();
        for(Habitation hTemporaire : nouveauParcours.listeHabitations) {
            for(Arrete aTemporaire : hTemporaire.listeVoisins) {
                aTemporaire.parcourue = true;
                arreteParcours.add(aTemporaire);
            }
        }
        interfaceVille.repaintOnly();
        try { Thread.sleep(10); } catch (InterruptedException e) {}
        for(Arrete a : arreteParcours) a.parcourue = false;
    }

    public ArrayList<Arrete> kruskal(){
        PriorityQueue<Arrete> classement = new PriorityQueue<>();
        ArrayList<Arrete> arreteConserve = new ArrayList<>();

        int i = 1;
        for(Habitation h : listeHabitations.values()) {
            for(Arrete a : h.listeVoisins) classement.add(a);
            h.kruskalIndex = i;
            i++;
        }

        int nbrArretes = 0;
        while(nbrArretes < listeHabitations.size() - 1){
            Arrete aTemporaire = classement.poll();
            if(aTemporaire.depart.kruskalIndex != aTemporaire.arrivee.kruskalIndex){
                arreteConserve.add(aTemporaire);
                for(Habitation h : listeHabitations.values()){
                    if(h.kruskalIndex == aTemporaire.arrivee.kruskalIndex){
                        h.kruskalIndex = aTemporaire.depart.kruskalIndex;
                    }
                }

                nbrArretes++;
            }
        }
        return arreteConserve;
    }

    public Map<Habitation, List<Habitation>> construireAdj(ArrayList<Arrete> mst) {
        Map<Habitation, List<Habitation>> adj = new HashMap<>();
        for (Habitation h : listeHabitations.values()) {
            adj.put(h, new ArrayList<>());
        }
        for (Arrete a : mst) {
            adj.get(a.depart).add(a.arrivee);
            adj.get(a.arrivee).add(a.depart);
        }
        return adj;
    }

    public void dfs(Habitation courant, Habitation parent, List<Habitation> resultat,
                    Map<Habitation, List<Habitation>> adj) {
        resultat.add(courant);
        for(Habitation voisin : adj.get(courant)) {
            if(voisin != parent) {
                dfs(voisin, courant, resultat, adj);
                resultat.add(courant); // revenir pour garder la structure arbre
            }
        }
    }

    public List<Habitation> shortcutting(List<Habitation> parcours) {
        Set<Habitation> dejaVu = new HashSet<>();
        List<Habitation> resultat = new ArrayList<>();
        for(Habitation h : parcours) {
            if(!dejaVu.contains(h)) {
                resultat.add(h);
                dejaVu.add(h);
            }
        }
        return resultat;
    }

    public List<Parcours> decouperTournees(List<Habitation> tourUnique, List<Truck> camions) {
        List<Parcours> tours = new ArrayList<>();
        int camionIndex = 0;

        Parcours parcoursCourant = new Parcours();
        int charge = 0;
        Truck camion = camions.get(camionIndex);

        for(Habitation h : tourUnique) {
            if(charge + 1 > camion.capacite) {
                tours.add(parcoursCourant);
                parcoursCourant = new Parcours();
                charge = 0;

                camionIndex = Math.min(camionIndex + 1, camions.size() - 1);
                camion = camions.get(camionIndex);
            }
            parcoursCourant.listeHabitations.add(h);
            charge += 1;
        }
        if(!parcoursCourant.listeHabitations.isEmpty()) {
            tours.add(parcoursCourant);
        }

        return tours;
    }

    public List<Parcours> calculerTournées(Habitation depot, List<Truck> camions) {
        // 1. MST
        ArrayList<Arrete> mst = kruskal();

        // 2. Adjacence
        Map<Habitation, List<Habitation>> adj = construireAdj(mst);

        // 3. DFS préfixe
        List<Habitation> parcours = new ArrayList<>();
        dfs(depot, null, parcours, adj);

        // 4. Shortcutting
        List<Habitation> tourUnique = shortcutting(parcours);

        // 5. Découpage
        return decouperTournees(tourUnique, camions);
    }
}
