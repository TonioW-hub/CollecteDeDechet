package Model;

import java.util.*;
import java.io.*;

public class GrapheVille {
    HashMap<Integer, Habitation> listeHabitations = new HashMap<>();
    HashMap<String, Rue> listeRues = new HashMap<>();

    public String choixVille(String nomFichier) throws IOException {
        HashSet<String> villes = new HashSet<>();
        String ligne;
        String ville;

        FileReader lecteurFichier = new FileReader(".\\src\\" + nomFichier);
        BufferedReader br = new BufferedReader(lecteurFichier);

        while((ligne = br.readLine()) != null){
            String[] tab = ligne.split(";");
            villes.add(tab[7]);
        }

        System.out.println("Villes disponibles à l'étude : ");
        for(String v : villes){
            System.out.println(v);
        }
        System.out.println("Veuillez choisir une ville : ");
        Scanner sc = new Scanner(System.in);
        ville = sc.nextLine();
        while(!villes.contains(ville)){
            System.out.println("Ville inconnue retente ta chance : ");
            ville = sc.nextLine();
        }

        return ville;
    }

    public HashMap<Integer, Habitation> extraireHabitations(String nomFichier){
        // Première passe : créer toutes les habitations
        try (FileReader lecteurFichier = new FileReader(".\\src\\" + nomFichier);
             BufferedReader br = new BufferedReader(lecteurFichier)) {

            String ville = choixVille(nomFichier);

            String ligne;
            int i = 0;
            while((ligne = br.readLine()) != null){
                String[] tab = ligne.split(";");

                int numeroMaison = Integer.parseInt(tab[2]);
                boolean bis;
                bis = tab[3].equals("bis");

                if(tab[7].equals(ville)) {
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
        int i = 0;
        for(Habitation h : listeHabitations.values()){
            System.out.println("Habitation : " + h.nomDeLaRue);
            for(Arrete a : h.listeVoisins){
                System.out.println("Voisin : " + a.arrivee);
            }
            System.out.println("--------------------------------" + i);
            i++;
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

    public void trouverVoisin(){
        for(Habitation h : listeHabitations.values()){

        }
    }
}
