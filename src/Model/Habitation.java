package Model;

import java.util.ArrayList;

public class Habitation {
    public float numeroMaison, lon, lat, x, y;
    public boolean bis = false;//si pas bis false et si bis true
    public String nomDeLaRue;
    public ArrayList<Arrete> listeVoisins = new ArrayList<>();
    public ArrayList<Habitation> listeVoisinsHabitations = new ArrayList<>();
    int kruskalIndex;

    public Habitation() {}

    public Habitation(float numeroMaison, String nomDeLaRue) {
        this.numeroMaison = numeroMaison;
        this.nomDeLaRue = nomDeLaRue;
    }
}
