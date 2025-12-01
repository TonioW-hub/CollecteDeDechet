package Model.Intersection;

import java.util.ArrayList;

public class Intersection {
    public float lon;   // longitude
    public float lat;   // latitude
    public float x;     // conversion en coordonnées locales (après transformation)
    public float y;

    // On stocke toutes les rues impliquées dans l'intersection
    public ArrayList<String> rues = new ArrayList<>();

    // Pour relier les intersections entre elles
    public ArrayList<Intersection> voisins = new ArrayList<>();

    public Intersection(float lon, float lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public void addRue(String r) {
        if (!rues.contains(r)) rues.add(r);
    }
}
