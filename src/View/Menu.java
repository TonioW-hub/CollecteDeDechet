package View;

import Model.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Menu extends JFrame {

    // Les fichiers dont on a besoin
    private File fichierVille = null;
    private File fichierIntersections = null;

    // Pour afficher le nom des fichiers qu'on a choisi
    private JLabel labelVille;
    private JLabel labelIntersections;

    // Constructeur, c'est comme le main de la fenêtre
    public Menu() {
        super("Menu – Collecteur de Déchets");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Pour que ça se ferme quand on clique sur la croix
        setLayout(new BorderLayout(10, 10)); // Layout principal
        setLocationRelativeTo(null); // Ça centre la fenêtre sur l'écran

        // On appelle la méthode qui crée tous les boutons et tout
        initUI();
    }

    // Cette méthode crée toute l'interface
    private void initUI() {
        // Le panel principal, c'est comme une boîte qu'on remplit
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Tout aligné en colonne
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Un peu de marge

        // Le titre en haut
        JLabel titre = new JLabel("Menu Collecte de Déchets");
        titre.setFont(new Font("Arial", Font.BOLD, 18)); // Police en gras
        titre.setAlignmentX(Component.CENTER_ALIGNMENT); // Centré
        mainPanel.add(titre);
        mainPanel.add(Box.createVerticalStrut(30)); // Un espace vide de 30 pixels

        // Première section : les fichiers
        JLabel sectionFichiers = new JLabel("1. Chargement des fichiers");
        sectionFichiers.setFont(new Font("Arial", Font.BOLD, 14));
        sectionFichiers.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(sectionFichiers);
        mainPanel.add(Box.createVerticalStrut(10));

        // Bouton pour choisir ville.txt
        JButton btnVille = new JButton("Choisir fichier des habitations (ville.txt)");
        btnVille.setAlignmentX(Component.LEFT_ALIGNMENT);
        // Quand on clique, ça appelle choisirFichierVille()
        btnVille.addActionListener(e -> choisirFichierVille());
        mainPanel.add(btnVille);

        // Label qui montre quel fichier on a choisi (vide au début)
        labelVille = new JLabel("Aucun fichier sélectionné");
        labelVille.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelVille.setForeground(Color.GRAY); // Gris tant qu'on a rien choisi
        mainPanel.add(labelVille);
        mainPanel.add(Box.createVerticalStrut(15));

        // Bouton pour choisir intersections.txt
        JButton btnInter = new JButton("Choisir fichier des intersections (intersections.txt)");
        btnInter.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnInter.addActionListener(e -> choisirFichierIntersections());
        mainPanel.add(btnInter);

        // Label pour le fichier intersections
        labelIntersections = new JLabel("Aucun fichier sélectionné");
        labelIntersections.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelIntersections.setForeground(Color.GRAY);
        mainPanel.add(labelIntersections);

        mainPanel.add(Box.createVerticalStrut(30)); // Encore un espace

        // Deuxième section : les algorithmes
        JLabel sectionAlgos = new JLabel("2. Sélection de l'algorithme");
        sectionAlgos.setFont(new Font("Arial", Font.BOLD, 14));
        sectionAlgos.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(sectionAlgos);
        mainPanel.add(Box.createVerticalStrut(10));

        // Panel pour les boutons des algos, en grille 2 colonnes
        JPanel algoPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // 2 colonnes
        algoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Les noms qui vont apparaître sur les boutons
        String[] nomBoutons = {
                "BFS - Une habitation", "Dijkstra - Une habitation",
                "BFS - 10 habitations", "Dijkstra - 10 habitations",
                "Parcours complet", "MST (Arbre couvrant)"
        };

        // Les codes correspondants qu'on va passer aux méthodes
        String[] codesAlgos = {
                "bfs1", "dijkstra1",
                "bfs10", "dijkstra10",
                "touteVille", "mst"
        };

        // On crée tous les boutons
        for (int i = 0; i < nomBoutons.length; i++) {
            JButton btn = new JButton(nomBoutons[i]);
            final String code = codesAlgos[i]; // final pour que ça marche dans le lambda
            // Quand on clique, ça lance l'algo correspondant
            btn.addActionListener(e -> lancerAlgo(code));
            btn.setEnabled(false); // Désactivés au début, on active que quand les fichiers sont choisis
            algoPanel.add(btn);
        }

        mainPanel.add(algoPanel);

        // On met le panel principal au centre
        add(mainPanel, BorderLayout.CENTER);

        // Un petit panneau en bas pour les messages
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEtchedBorder()); // Petite bordure
        JLabel statusLabel = new JLabel("Sélectionnez les deux fichiers pour activer les algorithmes");
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
    }

    // Quand on clique sur "Choisir fichier des habitations"
    private void choisirFichierVille() {
        JFileChooser chooser = new JFileChooser(new File(".")); // Ouvre à la racine du projet
        chooser.setDialogTitle("Sélectionnez le fichier des habitations (ville.txt)");
        int val = chooser.showOpenDialog(this);

        if (val == JFileChooser.APPROVE_OPTION) {
            fichierVille = chooser.getSelectedFile();
            labelVille.setText("Fichier sélectionné : " + fichierVille.getName());
            labelVille.setForeground(Color.BLUE); // Passe en bleu quand c'est bon
            verifierFichiers(); // On vérifie si les deux fichiers sont là
        }
    }

    // Même chose pour les intersections
    private void choisirFichierIntersections() {
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setDialogTitle("Sélectionnez le fichier des intersections (intersections.txt)");
        int val = chooser.showOpenDialog(this);

        if (val == JFileChooser.APPROVE_OPTION) {
            fichierIntersections = chooser.getSelectedFile();
            labelIntersections.setText("Fichier sélectionné : " + fichierIntersections.getName());
            labelIntersections.setForeground(Color.BLUE);
            verifierFichiers();
        }
    }

    // Vérifie si les deux fichiers sont choisis, si oui active les boutons
    private void verifierFichiers() {
        if (fichierVille != null && fichierIntersections != null) {
            // C'est un peu moche mais ça marche : on parcourt tous les boutons pour les activer
            for (Component comp : getContentPane().getComponents()) {
                if (comp instanceof JPanel) {
                    for (Component subComp : ((JPanel) comp).getComponents()) {
                        if (subComp instanceof JPanel) {
                            for (Component btn : ((JPanel) subComp).getComponents()) {
                                if (btn instanceof JButton) {
                                    btn.setEnabled(true);
                                }
                            }
                        }
                    }
                }
            }

            // Met à jour le message en bas
            JPanel statusPanel = (JPanel) getContentPane().getComponent(1); // Le panel en bas
            if (statusPanel.getComponentCount() > 0) {
                ((JLabel) statusPanel.getComponent(0)).setText("Fichiers chargés. Sélectionnez un algorithme.");
            }
        }
    }

    // Quand on clique sur un bouton d'algorithme
    private void lancerAlgo(String mode) {
        // Vérifie d'abord qu'on a bien les fichiers
        if (fichierVille == null || fichierIntersections == null) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez d'abord sélectionner les deux fichiers !",
                    "Fichiers manquants",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Désactive la fenêtre pendant que ça tourne pour pas qu'on clique partout
        setEnabled(false);

        // Utilise un SwingWorker pour pas que l'interface gèle pendant les calculs
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    executerAlgo(mode); // C'est là que tout se passe
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(Menu.this,
                            "Erreur lors de l'exécution : " + e.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }

            @Override
            protected void done() {
                // Réactive la fenêtre quand c'est fini
                setEnabled(true);
                requestFocus();
            }
        };

        worker.execute(); // Lance le worker
    }

    // La grosse méthode qui fait tout le travail
    private void executerAlgo(String mode) {
        System.out.println("=== Lancement de l'algorithme : " + mode + " ===");

        // 1. On crée le graphe de la ville
        GrapheVille grapheVille = new GrapheVille();

        // 2. On charge les intersections
        System.out.println("Chargement des intersections...");
        grapheVille.chargerIntersections(fichierIntersections.getAbsolutePath());

        // 3. On charge les habitations
        System.out.println("Extraction des habitations...");
        HashMap<String, Habitation> listeHabitations = grapheVille.extraireHabitations(fichierVille.getAbsolutePath());

        // 4. On connecte tout ça
        System.out.println("Connexion des habitations...");
        grapheVille.connecterHabitationsParIntersections();

        // 5. On crée le dépôt et l'entrepôt
        Habitation depot = new Habitation(1, "Dépôt");
        Habitation entrepot = new Habitation(1, "Entrepôt");

        // On trouve l'habitation la plus proche du coin pour y attacher dépôt/entrepôt
        Habitation habitationProche = trouverHabitationLaPlusProche(grapheVille.listeHabitations);

        // On connecte le dépôt et l'entrepôt à cette habitation
        depot.listeVoisinsHabitations.add(habitationProche);
        entrepot.listeVoisinsHabitations.add(habitationProche);
        depot.listeVoisins.add(new Arrete(depot, habitationProche));
        entrepot.listeVoisins.add(new Arrete(entrepot, habitationProche));

        // 6. On lance l'interface graphique
        Interface interfaceVille = new Interface();
        interfaceVille.lancerFenetre();
        interfaceVille.afficherGraphe(grapheVille.listeHabitations);

        // 7. On crée les instances des algorithmes
        H01 h01 = new H01(grapheVille.listeHabitations, grapheVille.listeRues);
        H02 h02 = new H02(grapheVille.listeHabitations, grapheVille.listeRues);

        ArrayList<Habitation> resultat = null;

        // 8. On exécute l'algorithme demandé
        switch (mode) {
            case "bfs1":
                System.out.println("Exécution de BFS pour 1 destination...");
                resultat = h01.bfs1(depot, habitationProche, interfaceVille);
                break;

            case "dijkstra1":
                System.out.println("Exécution de Dijkstra pour 1 destination...");
                resultat = h01.dijkstra1(depot, habitationProche, interfaceVille);
                break;

            case "bfs10":
                System.out.println("Exécution de BFS pour 10 destinations...");
                ArrayList<Habitation> destinationsBfs = genererDestinationsAleatoires(grapheVille, 10);
                resultat = h01.bfs10(depot, destinationsBfs, interfaceVille);
                break;

            case "dijkstra10":
                System.out.println("Exécution de Dijkstra pour 10 destinations...");
                ArrayList<Habitation> destinationsDijkstra = genererDestinationsAleatoires(grapheVille, 10);
                resultat = h01.dijkstra10(depot, destinationsDijkstra, interfaceVille);
                break;

            case "touteVille":
                System.out.println("Exécution du parcours complet de la ville...");
                resultat = h01.touteVille(entrepot, interfaceVille);
                break;

            case "mst":
                System.out.println("Exécution de l'algorithme MST...");
                ArrayList<Truck> camions = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    camions.add(new Truck(1000, i));
                }
                h02.calculerTournees(entrepot, camions);
                JOptionPane.showMessageDialog(this,
                        "MST terminé !\n10 camions ont été assignés.",
                        "MST Complet",
                        JOptionPane.INFORMATION_MESSAGE);
                return; // On return direct car MST ne renvoie pas de chemin
        }

        // 9. On affiche le résultat si y en a un
        if (resultat != null && !resultat.isEmpty()) {
            System.out.println("=== Résultat de l'algorithme ===");
            StringBuilder sb = new StringBuilder();
            sb.append("Chemin trouvé (").append(resultat.size()).append(" étapes):\n");

            // On affiche les 20 premières étapes max pour pas faire trop long
            for (int i = 0; i < Math.min(resultat.size(), 20); i++) {
                Habitation h = resultat.get(i);
                sb.append(i + 1).append(". ").append(h.nomDeLaRue)
                        .append(" ").append(h.numeroMaison).append("\n");
            }

            // Si y en a plus de 20, on le dit
            if (resultat.size() > 20) {
                sb.append("... et ").append(resultat.size() - 20).append(" autres étapes\n");
            }

            // Popup avec le résultat
            JOptionPane.showMessageDialog(this,
                    sb.toString(),
                    "Résultat de l'algorithme " + mode,
                    JOptionPane.INFORMATION_MESSAGE);
        } else if (resultat != null) {
            // Si resultat est vide
            JOptionPane.showMessageDialog(this,
                    "Aucun chemin trouvé.",
                    "Résultat",
                    JOptionPane.WARNING_MESSAGE);
        }

        // On rafraîchit l'affichage
        interfaceVille.repaintOnly();
    }

    // Trouve l'habitation la plus proche du coin (0,0)
    private Habitation trouverHabitationLaPlusProche(HashMap<String, Habitation> map) {
        if (map.isEmpty()) return null;

        Habitation plusProche = null;
        double distanceMin = Double.MAX_VALUE; // Très grand nombre au début

        // On parcourt toutes les habitations
        for (Habitation h : map.values()) {
            // Calcul de la distance au coin (0,0)
            double distance = Math.sqrt(h.x * h.x + h.y * h.y);
            if (distance < distanceMin) {
                distanceMin = distance;
                plusProche = h;
            }
        }

        return plusProche;
    }

    // Génère une liste d'habitations au hasard pour tester
    private ArrayList<Habitation> genererDestinationsAleatoires(GrapheVille graphe, int nombre) {
        // On prend toutes les habitations
        ArrayList<Habitation> toutesHabitations = new ArrayList<>(graphe.listeHabitations.values());
        ArrayList<Habitation> destinations = new ArrayList<>();

        // On enlève le dépôt et l'entrepôt si jamais ils sont dans la liste
        toutesHabitations.removeIf(h -> h.nomDeLaRue.equals("Dépôt") || h.nomDeLaRue.equals("Entrepôt"));

        // Si y a moins d'habitations que demandé, on ajuste
        if (toutesHabitations.size() < nombre) {
            nombre = toutesHabitations.size();
        }

        // On prend 'nombre' habitations au hasard
        for (int i = 0; i < nombre; i++) {
            int index = (int) (Math.random() * toutesHabitations.size());
            destinations.add(toutesHabitations.get(index));
            toutesHabitations.remove(index); // Pour pas reprendre la même
        }

        System.out.println("Destinations aléatoires générées : " + destinations.size());
        return destinations;
    }

    // Pour afficher la fenêtre
    public void afficher() {
        setVisible(true);
    }

    // Pour tester le menu tout seul
    public static void main(String[] args) {
        // SwingUtilities c'est pour être sûr que ça tourne dans le bon thread
        SwingUtilities.invokeLater(() -> {
            Menu menu = new Menu();
            menu.afficher();
        });
    }
}