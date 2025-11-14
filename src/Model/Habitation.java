package Model;

import java.util.ArrayList;

public class Habitation {
    public int numeroMaison, coordX, coordY;
    public String nomDeLaRue;
    ArrayList<Arrete> listeVoisins = new ArrayList<>();
    boolean sensRue = false; //false horizontale / true verticale
}
