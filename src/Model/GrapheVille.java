package Model;

import java.util.*;
import java.io.*;

public class GrapheVille {
    HashMap<Integer, Habitation> listeHabitations = new HashMap<>();
    
    public HashMap<Integer, Habitation> extraireHabitations(String nomFichier){
        //Les fichiers pour les villes devront être du format texte et les lignes comme suit :
        //"numéro de 1 au nbr de maison";"numéroRue,adresseHabitations";"voisins (numéroRue + adresseHabitations)";"voisins" (Pour l'instant, cela sera surement améné à changer avec nombre d'habitant ect...)
        try (FileReader lecteurFichier = new FileReader(".\\src\\" + nomFichier);
        BufferedReader br = new BufferedReader(lecteurFichier)) {

            String ligne;
            while((ligne = br.readLine()) != null){
                String[] tab = ligne.split(";");
                Habitation h = new Habitation();
                h.numeroMaison = Integer.parseInt(tab[0]);
                h.nomDeLaRue = tab[1];

                for(int i = 2; i < tab.length; i++){
                    Arrete ar = new Arrete();
                    ar.depart = tab[1];
                    ar.arrivee = tab[i];
                    h.listeVoisins.add(ar);
                }

                listeHabitations.put(h.numeroMaison, h);
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

        Habitation habitationInitiale = listeHabitations.get(0);

        int i = 0;
        for(Habitation h : listeHabitations.values()){
            h.coordX = i;
            h.coordY = i;
            i += 100;
        }
    }
}
