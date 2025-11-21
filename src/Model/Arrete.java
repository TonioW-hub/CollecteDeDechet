package Model;

public class Arrete {
    Habitation depart;
    Habitation arrivee;
    int distance;

    public Arrete(){}
    public Arrete(Habitation depart, Habitation arrivee){
        this.depart = depart;
        this.arrivee = arrivee;
    }
}
