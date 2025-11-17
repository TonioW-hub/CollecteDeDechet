package Model;

import java.util.*;
import java.io.*;

public class GrapheVille {
    HashMap<Integer, Habitation> listeHabitations = new HashMap<>();
    HashMap<String, Rue> listeRues = new HashMap<>();

    public HashMap<Integer, Habitation> extraireHabitations(String nomFichier){
        // Première passe : créer toutes les habitations
        try (FileReader lecteurFichier = new FileReader(".\\src\\" + nomFichier);
             BufferedReader br = new BufferedReader(lecteurFichier)) {

            String ligne;
            while((ligne = br.readLine()) != null){
                String[] tab = ligne.split(";");
                int numeroMaison = Integer.parseInt(tab[0]);

                if(!listeHabitations.containsKey(numeroMaison)) {
                    Habitation h = new Habitation();
                    h.numeroMaison = numeroMaison;
                    h.nomDeLaRue = tab[1];
                    listeHabitations.put(numeroMaison, h);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deuxième passe : ajouter les voisins
        try (FileReader lecteurFichier = new FileReader(".\\src\\" + nomFichier);
             BufferedReader br = new BufferedReader(lecteurFichier)) {

            String ligne;
            while((ligne = br.readLine()) != null){
                String[] tab = ligne.split(";");
                Habitation h = listeHabitations.get(Integer.parseInt(tab[0]));

                for(int i = 2; i < tab.length; i++){
                    Arrete ar = new Arrete();
                    ar.depart = tab[1];
                    ar.arrivee = tab[i];
                    h.listeVoisins.add(ar);

                    String numeroStr = tab[i].split(" ")[0];
                    int numeroVoisin = Integer.parseInt(numeroStr);

                    // Récupérer le voisin depuis listeHabitations
                    Habitation hVoisin = listeHabitations.get(numeroVoisin);
                    if(hVoisin != null) {
                        h.listeVoisinsHabitation.add(hVoisin);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return listeHabitations;
    }

    public void afficherHabitations(){
        for(Habitation h : listeHabitations.values()){
            System.out.println("Habitation : " + h.nomDeLaRue);
            for(Arrete a : h.listeVoisins){
                System.out.println("Voisin : " + a.arrivee);
            }
        }
    }

    public void definirCoordGraphe() {
        definirRue();

        int j = 0;
        for(Rue r : listeRues.values()){
            int i = 0;
            for(Habitation h : r.listeHabitation){
                h.coordX = 10 + i * 110 ;
                h.coordY = 10 + j * 110;
                i++;
            }
            j++;
        }

    }

    public void definirRue(){
        for(Habitation h : listeHabitations.values()){
            String[] tab = h.nomDeLaRue.split(",");

            if(!listeRues.containsKey(tab[1])){
                Rue r = new Rue();
                r.nomRue = tab[1];
                listeRues.put(tab[1], r);
                r.listeHabitation.add(h);
            } else {
                Rue r = listeRues.get(tab[1]);
                r.listeHabitation.add(h);
            }
        }
    }
}
