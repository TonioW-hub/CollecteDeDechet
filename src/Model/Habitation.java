package Model;

import java.util.ArrayList;

public class Habitation {
    public float lon, lat, x, y;
    int numeroMaison;
    boolean bis = false;//si pas bis false et si bis true
    public String nomDeLaRue;
    ArrayList<Arrete> listeVoisins = new ArrayList<>();
    ArrayList<Habitation> listeVoisinsHabitation = new ArrayList<>();

    public Habitation() {}
    public Habitation(String nomDeLaRue, int numeroMaison) {
        this.nomDeLaRue = nomDeLaRue;
        this.numeroMaison = numeroMaison;
    }
}
