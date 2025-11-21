package Model;

import java.util.ArrayList;

public class Habitation {
    public float numeroMaison, lon, lat, x, y;
    boolean bis = false;//si pas bis false et si bis true
    public String nomDeLaRue;
    ArrayList<Arrete> listeVoisins = new ArrayList<>();
    ArrayList<Habitation> listeVoisinsHabitations = new ArrayList<>();

    public Habitation() {}
}
