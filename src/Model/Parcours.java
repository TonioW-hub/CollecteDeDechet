package Model;

import java.util.ArrayList;

public class Parcours implements Comparable<Parcours>{
    String id;
    ArrayList<Habitation> listeHabitations = new ArrayList<>();
    int distance;

    @Override
    public int compareTo(Parcours o) {
        return this.distance - o.distance;
    }
}
