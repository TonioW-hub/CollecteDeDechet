package Model.Intersection;

import java.util.ArrayList;

public class Intersection {
    public double lon, lat;
    public float x, y;
    public String nom = null; // Nom du Hub (ex: Place Leclerc)
    public ArrayList<String> rues = new ArrayList<>();

    // On stocke les segments (Arêtes) qui partent de cette intersection
    public ArrayList<Segment> voisins = new ArrayList<>();

    public Intersection(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public void addRue(String r) {
        if (!rues.contains(r)) rues.add(r);
    }

    public void addVoisin(Intersection cible, double distance, String nomRue) {
        this.voisins.add(new Segment(cible, distance, nomRue));
    }

    @Override
    public String toString() {
        return (nom != null ? "[HUB] " + nom : "Inter.") + " (" + rues.size() + " rues)";
    }

    //classe segment pour entre 2 intersections
    public class Segment {
        public Intersection destination;
        public double distance;
        public String nomRue;

        public int nombrePassages = 1;
        public int passagesEffectues = 0;

        public Segment(Intersection destination, double distance, String nomRue) {
            this.destination = destination;
            this.distance = distance;
            this.nomRue = nomRue;
        }
    }
}