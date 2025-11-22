package Model;

import java.util.*;
import java.io.*;

public class GrapheVille {
    public HashMap<String, Habitation> listeHabitations = new HashMap<>();
    public HashMap<String, Rue> listeRues = new HashMap<>();

    int distanceMax = 20; //Peut être amener à changer en fonction de ville/campage/

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

    public HashMap<String, Habitation> extraireHabitations(String nomFichier){
        // Première passe : créer toutes les habitations
        try (FileReader lecteurFichier = new FileReader(".\\src\\" + nomFichier);
             BufferedReader br = new BufferedReader(lecteurFichier)) {

            String ville = choixVille(nomFichier);

            String ligne;
            int i = 0;

            while((ligne = br.readLine()) != null){
                String[] tab = ligne.split(";");

                float numeroMaison = Float.parseFloat(tab[2]);


                boolean bis = false;
                if(tab[3] != null && !tab[3].trim().isEmpty()) {
                    bis = tab[3].trim().equalsIgnoreCase("bis");
                }

                boolean ter = false;
                if(tab[3] != null && !tab[3].trim().isEmpty()) {
                    ter = tab[3].trim().equalsIgnoreCase("ter");
                }

                if(tab[7].equals(ville)) {
                    Habitation h = new Habitation();
                    h.bis = bis;
                    if(h.bis){
                        numeroMaison += 0.5;
                    }
                    if(ter){
                        numeroMaison += 0.75;
                    }
                    h.numeroMaison = numeroMaison;
                    h.nomDeLaRue = tab[4];
                    h.x = Float.parseFloat(tab[10]);
                    h.y = Float.parseFloat(tab[11]);
                    h.lon = Float.parseFloat(tab[12]);
                    h.lat = Float.parseFloat(tab[13]);
                    listeHabitations.put(h.numeroMaison + h.nomDeLaRue, h);
                    i++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        definirRue();
        trouverVoisinAvecRue();
        trouverVoisinAvecDistance();
        return listeHabitations;
    }

    public void afficherHabitations(){
        int i = 0;
        for(Habitation h : listeHabitations.values()){
            System.out.println("Habitation : " + h.numeroMaison + h.nomDeLaRue);
            i++;
        }
    }

    public void definirRue(){
        for(Habitation h : listeHabitations.values()){


           if(!listeRues.containsKey(h.nomDeLaRue)){
               Rue r = new Rue();
               float num = h.numeroMaison;
               r.listeHabitation.put(num ,h);
               r.nomRue = h.nomDeLaRue;
               listeRues.put(h.nomDeLaRue, r);
           } else {
               float num = h.numeroMaison;
               listeRues.get(h.nomDeLaRue).listeHabitation.put(num, h);
           }
        }
    }

    public void afficherRue(){
        for(Rue r : listeRues.values()){
           System.out.println("Rue : " + r.nomRue);
            for(Map.Entry<Float, Habitation> entry : r.listeHabitation.entrySet()){
                float numeroCalcule = entry.getKey();
                Habitation h = entry.getValue();
                System.out.println("Habitation : " + numeroCalcule);
            }
           System.out.println("--------------------------------");
        }
    }

    //Deux méthodes pour trouver les voisins d'une habitation. La première avec la rue et la deuxième avec la distance
    public void trouverVoisinAvecRue(){
        for(Rue r : listeRues.values()){

            List<Float> numerosTries = new ArrayList<>(r.listeHabitation.keySet());
            Collections.sort(numerosTries);

            for(int i = 0; i < numerosTries.size(); i++){
                float numeroActuel = numerosTries.get(i);
                Habitation habitationActuelle = r.listeHabitation.get(numeroActuel);


                Habitation precedente = null;
                if(i > 0) {
                    float numeroPrecedent = numerosTries.get(i - 1);
                    precedente = r.listeHabitation.get(numeroPrecedent);
                }


                Habitation suivante = null;
                if(i < numerosTries.size() - 1) {
                    float numeroSuivant = numerosTries.get(i + 1);
                    suivante = r.listeHabitation.get(numeroSuivant);
                }


                if(precedente != null){
                    Arrete a = new Arrete(habitationActuelle, precedente);
                    habitationActuelle.listeVoisins.add(a);
                    habitationActuelle.listeVoisinsHabitations.add(precedente);
                }

                if(suivante != null){
                    Arrete a = new Arrete(habitationActuelle, suivante);
                    habitationActuelle.listeVoisins.add(a);
                    habitationActuelle.listeVoisinsHabitations.add(suivante);
                }
            }
        }
    }

    public void trouverVoisinAvecDistance(){
        for(Habitation h : listeHabitations.values()){

            while(h.listeVoisins.size() < 3){

                Habitation voisin = null;
                int distanceMin = Integer.MAX_VALUE;

                for(Habitation hdistance : listeHabitations.values()){
                    if(!hdistance.nomDeLaRue.equals(h.nomDeLaRue) && !h.listeVoisinsHabitations.contains(hdistance)){
                        int distance = (int) Math.round(Math.sqrt(Math.pow(h.x - hdistance.x, 2) + Math.pow(h.y - hdistance.y, 2)));
                        if((distance < distanceMin && distance <= distanceMax) || (h.listeVoisins.size() < 2 && distance < distanceMin)){
                            distanceMin = distance;
                            voisin = hdistance;
                        }
                    }
                }
                if(voisin != null){
                    h.listeVoisins.add(new Arrete(h, voisin));
                    h.listeVoisinsHabitations.add(voisin);
                } else {
                    break;
                }
            }
        }
    }
}
