package Model;

public class Arrete {
    public Habitation depart;
    public Habitation arrivee;
    int distance;
    public boolean parcourue = false; // Pour les couleurs de l'affichage

    public Arrete(){}
    public Arrete(Habitation depart, Habitation arrivee){
        this.depart = depart;
        this.arrivee = arrivee;
    }
}
