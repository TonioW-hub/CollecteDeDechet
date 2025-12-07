import Model.*;
import View.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.afficher();

        System.out.println("Application de collecte de déchets démarrée.");
        System.out.println("Veuillez sélectionner les fichiers dans le menu.");
    }
}