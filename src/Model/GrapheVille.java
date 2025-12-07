package Model;
import Model.Intersection.Intersection;

import java.util.*;
import java.io.*;

public class GrapheVille {
    final double SEUIL_FUSION_CLASSIQUE = 0.0001;
    public HashMap<String, Habitation> listeHabitations = new HashMap<>();
    public HashMap<String, Rue> listeRues = new HashMap<>();
    public HashMap<String, Intersection> intersections = new HashMap<>();

    int distanceMax = 20;

    private double distance(Habitation a, Habitation b) {
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public String choixVille(String nomFichier) throws IOException {
        HashSet<String> villes = new HashSet<>();
        String ligne;
        String ville;

        FileReader lecteurFichier = new FileReader(nomFichier);
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
        try (FileReader lecteurFichier = new FileReader(nomFichier);
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
        //trouverVoisinAvecDistance();
        connecterViaIntersectionsBestPair();
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
                    a.calculeDistance();
                    habitationActuelle.listeVoisins.add(a);
                    habitationActuelle.listeVoisinsHabitations.add(precedente);
                }

                if(suivante != null){
                    Arrete a = new Arrete(habitationActuelle, suivante);
                    a.calculeDistance();
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
                    Arrete a = new Arrete(h, voisin);
                    a.calculeDistance();
                    h.listeVoisins.add(a);
                    h.listeVoisinsHabitations.add(voisin);
                } else {
                    break;
                }
            }
        }
    }

    public String choixVilleIntersections(String fichier) {

        HashSet<String> villes = new HashSet<>();

        try {
            // Charger tout le fichier en une seule String
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }

            String json = sb.toString();

            // Trouver tous les "context":"Ville, ..."
            int index = 0;

            while ((index = json.indexOf("\"context\"", index)) != -1) {

                int colon = json.indexOf(":", index);
                int q1 = json.indexOf("\"", colon + 1);
                int q2 = json.indexOf("\"", q1 + 1);

                if (colon < 0 || q1 < 0 || q2 < 0) break;

                String context = json.substring(q1 + 1, q2);
                String ville = context.split(",")[0].trim();

                villes.add(ville);

                index = q2;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Affichage
        System.out.println("Villes disponibles :");
        for (String v : villes) System.out.println(" - " + v);

        System.out.println("Choisissez une ville : ");
        Scanner sc = new Scanner(System.in);
        String choix = sc.nextLine();

        while (!villes.contains(choix)) {
            System.out.println("Ville inconnue, réessayez : ");
            choix = sc.nextLine();
        }

        return choix;
    }

    public void chargerIntersections(String fichier) {
        String villeChoisie = choixVilleIntersections(fichier);
        HashMap<String, Intersection> lieuxSpeciaux = new HashMap<>();

        try {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(fichier))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }
            String json = sb.toString();
            String[] features = json.split("\\{\\\"type\\\":\\\"Feature\\\"");

            for (String f : features) {
                if (!f.contains("coordinates")) continue;
                int iCtx = f.indexOf("\"context\"");
                if (iCtx < 0) continue;
                int cColon = f.indexOf(":", iCtx);
                int cQ1 = f.indexOf("\"", cColon + 1);
                int cQ2 = f.indexOf("\"", cQ1 + 1);
                String context = f.substring(cQ1 + 1, cQ2);
                String ville = context.split(",")[0].trim();

                if (!ville.equals(villeChoisie)) continue;

                int iCoord = f.indexOf("\"coordinates\"");
                int iStart = f.indexOf("[", iCoord);
                int iEnd = f.indexOf("]", iCoord);
                String coordStr = f.substring(iStart + 1, iEnd);
                String[] parts = coordStr.split(",");
                double lon = Double.parseDouble(parts[0]);
                double lat = Double.parseDouble(parts[1]);

                int iName = f.indexOf("\"name\"");
                int nQ1 = f.indexOf("\"", iName + 6 + 1);
                int nQ2 = f.indexOf("\"", nQ1 + 1);
                String nameRaw = f.substring(nQ1 + 1, nQ2);

                Intersection interTrouvee = null;
                boolean isHub = false;

                // CAS HUB (Place/Rond-Point/CArrefour/Square)
                if (estUnLieuSpecial(nameRaw)) {
                    String nomLieu = extraireNomLieu(nameRaw);

                    if (nomLieu != null) {
                        isHub = true;
                        if (lieuxSpeciaux.containsKey(nomLieu)) {
                            interTrouvee = lieuxSpeciaux.get(nomLieu);
                        } else {
                            interTrouvee = new Intersection(lon, lat);
                            interTrouvee.nom = nomLieu; // ICI : On définit le nom du sommet

                            lieuxSpeciaux.put(nomLieu, interTrouvee);
                            intersections.put("HUB_" + nomLieu, interTrouvee);
                        }
                    }
                }

                // PAs HUB mais intersection normal
                if (interTrouvee == null) {
                    for (Intersection existante : intersections.values()) {
                        // On ne touche pas aux Hubs via la distance
                        if (existante.nom != null) continue;

                        double dLat = Math.abs(existante.lat - lat);
                        double dLon = Math.abs(existante.lon - lon);
                        if (dLat > SEUIL_FUSION_CLASSIQUE || dLon > SEUIL_FUSION_CLASSIQUE) continue;

                        double dist = Math.sqrt(dLat*dLat + dLon*dLon);
                        if (dist < SEUIL_FUSION_CLASSIQUE) {
                            interTrouvee = existante;
                            break;
                        }
                    }
                }

                if (interTrouvee == null) {
                    interTrouvee = new Intersection(lon, lat);
                    String key = String.format(Locale.US, "%.7f_%.7f", lon, lat);
                    intersections.put(key, interTrouvee);
                }

                for (String r : nameRaw.split(" / ")) {
                    String ruePropre = nettoyerNomRue(r.trim());

                    // Si Hub, on n'ajoute pas le nom du Hub dans la liste des rues mais on le met en nom de l'intersection
                    if (interTrouvee.nom != null && ruePropre.equals(interTrouvee.nom)) {
                        continue;
                    }

                    // On n'ajoute pas non plus les noms vides
                    if (!ruePropre.isEmpty()) {
                        interTrouvee.addRue(ruePropre);
                    }
                }
            }

            // Conversion en Liste pour le tri
            List<Intersection> listeTriee = new ArrayList<>(intersections.values());

            // Tri par nombre de rues décroissant (Degré)
            listeTriee.sort((i1, i2) -> Integer.compare(i2.rues.size(), i1.rues.size()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean estUnLieuSpecial(String name) {
        String n = name.toLowerCase();
        return n.contains("place") || n.contains("rond-point") ||
                n.contains("carrefour") || n.contains("square");
    }

    private String extraireNomLieu(String name) {
        String[] parties = name.split(" / ");
        for (String partie : parties) {
            if (estUnLieuSpecial(partie)) {
                return nettoyerNomRue(partie.trim());
            }
        }
        return null;
    }

    private String nettoyerNomRue(String rue) {
        if (rue == null) return null;

        rue = rue.replaceAll("\\s+D\\d+[A-Z]?", "");

        if (rue.contains("|")) {
            rue = rue.substring(0, rue.indexOf("|")).trim();
        }

        if (rue.contains("Sortie")) {
            rue = rue.substring(0, rue.indexOf("Sortie")).trim();
        }

        return rue.trim();
    }

    private String normaliserRue(String rue) {
        return nettoyerNomRue(rue);
    }

    public HashMap<String, List<Habitation>> construireIndexHabitationsParRue() {
        HashMap<String, List<Habitation>> index = new HashMap<>();

        for (Habitation h : listeHabitations.values()) {
            String nomNorm = normaliserRue(h.nomDeLaRue);
            index.computeIfAbsent(nomNorm, k -> new ArrayList<>())
                    .add(h);
        }

        return index;
    }

    public void connecterHabitationsParIntersections() {

        HashMap<String, List<Habitation>> habitationsParRue = construireIndexHabitationsParRue();

        int nbArretesAjoutees = 0;

        for (Intersection inter : intersections.values()) {

            HashMap<String, Habitation> habitationPlusProcheParRue = new HashMap<>();

            for (String nomRueBrut : inter.rues) {
                String nomNorm = normaliserRue(nomRueBrut);

                List<Habitation> habs = habitationsParRue.get(nomNorm);
                if (habs == null || habs.isEmpty()) continue;

                Habitation best = null;
                double bestD2 = Double.MAX_VALUE;

                for (Habitation h : habs) {
                    double dx = h.lon - inter.lon;
                    double dy = h.lat - inter.lat;
                    double d2 = dx * dx + dy * dy;

                    if (d2 < bestD2) {
                        bestD2 = d2;
                        best = h;
                    }
                }

                if (best != null) {
                    habitationPlusProcheParRue.put(nomNorm, best);
                }
            }

            ArrayList<Habitation> habsInter =
                    new ArrayList<>(habitationPlusProcheParRue.values());

            for (int i = 0; i < habsInter.size(); i++) {
                for (int j = i + 1; j < habsInter.size(); j++) {

                    Habitation h1 = habsInter.get(i);
                    Habitation h2 = habsInter.get(j);

                    if (h1 == h2) continue;

                    // éviter les doublons
                    if (h1.listeVoisinsHabitations.contains(h2)) continue;

                    // h1 -> h2
                    Arrete a1 = new Arrete(h1, h2);
                    a1.calculeDistance();
                    h1.listeVoisins.add(a1);
                    h1.listeVoisinsHabitations.add(h2);

                    // h2 -> h1
                    Arrete a2 = new Arrete(h2, h1);
                    a2.calculeDistance();
                    h2.listeVoisins.add(a2);
                    h2.listeVoisinsHabitations.add(h1);

                    nbArretesAjoutees += 2;
                }
            }
        }

        System.out.println("Connexions via intersections ajoutées : " + nbArretesAjoutees + " arêtes.");
    }

    public void connecterViaIntersectionsBestPair() {

        // Prépare un map rue : liste d'habitations pour faciliter les accès
        HashMap<String, ArrayList<Habitation>> maisonsParRue = new HashMap<>();
        for (Rue r : listeRues.values()) {
            maisonsParRue.put(r.nomRue, new ArrayList<>(r.listeHabitation.values()));
        }

        int nbAretesCreees = 0;

        for (Model.Intersection.Intersection inter : intersections.values()) {

            // On récupère la liste des rues de cette intersection
            ArrayList<String> rues = new ArrayList<>(inter.rues);

            // Il faut au moins 2 rues pour faire une connexion
            if (rues.size() < 2) continue;

            // Pour chaque paire de rues (r1, r2)
            for (int i = 0; i < rues.size(); i++) {
                String rue1 = rues.get(i);
                ArrayList<Habitation> list1 = maisonsParRue.get(rue1);
                if (list1 == null || list1.isEmpty()) continue;

                for (int j = i + 1; j < rues.size(); j++) {
                    String rue2 = rues.get(j);
                    ArrayList<Habitation> list2 = maisonsParRue.get(rue2);
                    if (list2 == null || list2.isEmpty()) continue;

                    // Chercher le couple (h1, h2) qui minimise la distance entre habitations
                    Habitation bestH1 = null;
                    Habitation bestH2 = null;
                    double bestDist = Double.MAX_VALUE;

                    for (Habitation h1 : list1) {
                        for (Habitation h2 : list2) {
                            double d = distance(h1, h2);
                            if (d < bestDist) {
                                bestDist = d;
                                bestH1 = h1;
                                bestH2 = h2;
                            }
                        }
                    }

                    // Si on a trouve un couple valide, on crée l'arête
                    if (bestH1 != null && bestH2 != null &&
                            !bestH1.listeVoisinsHabitations.contains(bestH2)) {

                        Arrete a1 = new Arrete(bestH1, bestH2);
                        a1.calculeDistance();
                        bestH1.listeVoisins.add(a1);
                        bestH1.listeVoisinsHabitations.add(bestH2);

                        Arrete a2 = new Arrete(bestH2, bestH1);
                        a2.calculeDistance();
                        bestH2.listeVoisins.add(a2);
                        bestH2.listeVoisinsHabitations.add(bestH1);

                        nbAretesCreees += 2;
                    }
                }
            }
        }

        System.out.println("Connexions via intersections (best pair) ajoutées : " + nbAretesCreees + " arêtes.");
    }

    public void construireGraphe() {
        System.out.println("Construction du graphe topologique...");

        HashMap<String, ArrayList<Intersection>> rueVersIntersections = new HashMap<>();

        for (Intersection inter : intersections.values()) {
            for (String nomRue : inter.rues) {
                if (nomRue == null || nomRue.isEmpty()) continue;
                if (inter.nom != null && nomRue.equals(inter.nom)) continue;

                rueVersIntersections.putIfAbsent(nomRue, new ArrayList<>());
                rueVersIntersections.get(nomRue).add(inter);
            }
        }

        int nbSegments = 0;

        // Création des liens
        for (Map.Entry<String, ArrayList<Intersection>> entry : rueVersIntersections.entrySet()) {
            String nomRue = entry.getKey();
            ArrayList<Intersection> points = entry.getValue();

            if (points.size() < 2) continue;

            points.sort((i1, i2) -> Double.compare(i1.lon, i2.lon));

            for (int i = 0; i < points.size() - 1; i++) {
                Intersection depart = points.get(i);
                Intersection arrivee = points.get(i + 1);

                if (depart == arrivee) continue;

                // Calcul distance approximative (en mètres)
                double dist = calculerDistanceMetres(depart, arrivee);

                // Ajout bidirectionnel
                depart.addVoisin(arrivee, dist, nomRue);
                arrivee.addVoisin(depart, dist, nomRue);

                nbSegments++;
            }
        }
        System.out.println("Graphe construit : " + nbSegments + " segments de routes.");
    }

    // Distance entre intersections
    private double calculerDistanceMetres(Intersection a, Intersection b) {
        double latMoyenne = Math.toRadians((a.lat + b.lat) / 2.0);
        double mPerDegLat = 111132.954 - 559.822 * Math.cos(2 * latMoyenne);
        double mPerDegLon = 111412.84 * Math.cos(latMoyenne);

        double dLat = (a.lat - b.lat) * mPerDegLat;
        double dLon = (a.lon - b.lon) * mPerDegLon;

        return Math.sqrt(dLat * dLat + dLon * dLon);
    }

}
