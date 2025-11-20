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
            int i = 0;
            while((ligne = br.readLine()) != null){
                String[] tab = ligne.split(";");

                int numeroMaison = Integer.parseInt(tab[2]);

                boolean bis;
                if (tab[3].equals("bis")){
                    bis = true;
                } else {
                    bis = false;
                }

                if(!listeHabitations.containsKey(numeroMaison)) {
                    Habitation h = new Habitation();
                    h.bis = bis;
                    h.numeroMaison = numeroMaison;
                    h.nomDeLaRue = tab[4];
                    h.x = Float.parseFloat(tab[10]);
                    h.y = Float.parseFloat(tab[11]);
                    h.lon = Float.parseFloat(tab[12]);
                    h.lat = Float.parseFloat(tab[13]);
                    listeHabitations.put(i, h);
                    i++;
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
