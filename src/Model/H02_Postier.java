package Model;

import Model.Intersection.Intersection;
import Model.Intersection.Intersection.Segment;

import java.util.*;

public class H02_Postier {

    private HashMap<String, Intersection> intersections;

    public H02_Postier(HashMap<String, Intersection> intersections) {
        this.intersections = intersections;
    }

    public List<Intersection> resoudre(Intersection depot) {
        System.out.println("--- DÉBUT ALGORITHME POSTIER CHINOIS ---");

        // sommets impairs
        List<Intersection> impairs = getSommetsImpairs();
        System.out.println("Nombre de sommets de degré impair : " + impairs.size());

        // Selon le cas
        if (impairs.isEmpty()) {
            System.out.println("CAS 1 : Graphe Eulérien parfait. Lancement direct.");
            return hierholzer(depot);
        }
        else if (impairs.size() == 2) {
            System.out.println("CAS 2 : Graphe Semi-Eulérien.");
            return hierholzer(impairs.get(0));
        }
        else {
            System.out.println("CAS 3 : Graphe non-Eulérien. " + impairs.size() + " sommets impairs.");
            System.out.println(">>> Démarrage du couplage et de la duplication d'arêtes...");
            euleriserLeGraphe(impairs);
            System.out.println(">>> Graphe rendu Eulérien (tous degrés pairs). Lancement tournée.");
            return hierholzer(depot);
        }
    }

    private List<Intersection> getSommetsImpairs() {
        List<Intersection> impairs = new ArrayList<>();
        for (Intersection i : intersections.values()) {
            // degré = somme des 'nombrePassages' des segments partant
            // Au départ, c'est juste le nombre de voisins, mais ça change après duplication
            int degre = 0;
            for (Segment s : i.voisins) {
                degre += s.nombrePassages;
            }

            if (degre % 2 != 0) {
                impairs.add(i);
            }
        }
        return impairs;
    }

    private void euleriserLeGraphe(List<Intersection> impairs) {
        // Gloutonne
        // Premier impair

        while (!impairs.isEmpty()) {
            Intersection start = impairs.remove(0); // POrmier

            Intersection bestMatch = null;
            double minDist = Double.MAX_VALUE;
            List<Segment> cheminMin = null;

            // chemin vers les autres impairs
            for (Intersection target : impairs) {
                ResultatDijkstra res = dijkstraChemin(start, target);
                if (res.distance < minDist) {
                    minDist = res.distance;
                    bestMatch = target;
                    cheminMin = res.chemin;
                }
            }

            if (bestMatch != null) {
                System.out.println("Couplage : " + start.toString() + " <-> " + bestMatch.toString() + " (+ " + (int)minDist + "m)");

                for (Segment seg : cheminMin) {
                    seg.nombrePassages++;

                    boolean foundReverse = false;
                    for (Segment sRetour : seg.destination.voisins) {
                    }
                    augmenterPassageRetour(seg);
                }

                impairs.remove(bestMatch);
            }
        }
    }

    private void augmenterPassageRetour(Segment segmentAller) {
        Intersection destination = segmentAller.destination;
    }

    private class ResultatDijkstra {
        double distance;
        List<Segment> chemin;
        public ResultatDijkstra(double d, List<Segment> c) { distance = d; chemin = c; }
    }

    private ResultatDijkstra dijkstraChemin(Intersection depart, Intersection arrivee) {
        HashMap<Intersection, Double> dist = new HashMap<>();
        HashMap<Intersection, Segment> predSegment = new HashMap<>();
        HashMap<Intersection, Intersection> predNode = new HashMap<>();
        PriorityQueue<Intersection> pq = new PriorityQueue<>(Comparator.comparingDouble(n -> dist.getOrDefault(n, Double.MAX_VALUE)));

        dist.put(depart, 0.0);
        pq.add(depart);

        while (!pq.isEmpty()) {
            Intersection u = pq.poll();
            if (u == arrivee) break;

            for (Segment s : u.voisins) {
                Intersection v = s.destination;
                double newDist = dist.get(u) + s.distance;
                if (newDist < dist.getOrDefault(v, Double.MAX_VALUE)) {
                    dist.put(v, newDist);
                    predSegment.put(v, s);
                    predNode.put(v, u);
                    pq.add(v);
                }
            }
        }

        LinkedList<Segment> chemin = new LinkedList<>();
        Intersection curr = arrivee;
        while (curr != depart) {
            Segment s = predSegment.get(curr);
            if (s == null) return new ResultatDijkstra(Double.MAX_VALUE, new ArrayList<>());
            chemin.addFirst(s);
            curr = predNode.get(curr);
        }

        return new ResultatDijkstra(dist.get(arrivee), chemin);
    }


    public List<Intersection> hierholzer(Intersection startNode) {
        List<Intersection> circuit = new ArrayList<>();
        Stack<Intersection> stack = new Stack<>();
        stack.push(startNode);



        Intersection current = startNode;
        while (!stack.isEmpty()) {
            Segment availableSegment = null;

            for (Segment s : current.voisins) {
                if (s.passagesEffectues < s.nombrePassages) {
                    availableSegment = s;
                    break;
                }
            }

            if (availableSegment != null) {
                stack.push(current);

                availableSegment.passagesEffectues++;
                augmenterPassageEffectueRetour(current, availableSegment.destination);

                current = availableSegment.destination;
            } else {
                circuit.add(current);
                current = stack.pop();
            }
        }
        Collections.reverse(circuit);
        return circuit;
    }

    private void augmenterPassageEffectueRetour(Intersection depart, Intersection arrivee) {
        for (Segment s : arrivee.voisins) {
            if (s.destination == depart) {
                s.passagesEffectues++;
                return;
            }
        }
    }
}