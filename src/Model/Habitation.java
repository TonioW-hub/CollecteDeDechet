package Model;

import java.util.ArrayList;

public class Habitation {
    public int numeroMaison, coordX, coordY;
    public String nomDeLaRue;
    ArrayList<Arrete> listeVoisins = new ArrayList<>();
    ArrayList<Habitation> listeVoisinsHabitation = new ArrayList<>();
    boolean sensRue = false; //false horizontale / true verticale

    public Habitation() {}
    public Habitation(String nomDeLaRue, int numeroMaison) {
        this.nomDeLaRue = nomDeLaRue;
        this.numeroMaison = numeroMaison;
    }
}
