package Model;

public class Arrete implements Comparable<Arrete>{
    public Habitation depart;
    public Habitation arrivee;
    int distance;
    public boolean parcourue = false; // Pour les couleurs de l'affichage

    public Arrete(){}
    public Arrete(Habitation depart, Habitation arrivee){
        this.depart = depart;
        this.arrivee = arrivee;
    }

    public void calculeDistance(){
        this.distance = (int) Math.round(Math.sqrt(Math.pow(depart.x - arrivee.x, 2) + Math.pow(depart.y - arrivee.y, 2)));
    }

    @Override
    public int compareTo(Arrete o) {
        return this.distance - o.distance;
    }
}
