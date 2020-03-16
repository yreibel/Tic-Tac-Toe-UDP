package gui.plateau;

import jeu.Morpion;
import jeu.plateau.Plateau;
import reseau.Client;

import javax.swing.*;
import java.awt.*;

/**
 * Classe PlateauGUI
 * Représente le tablier graphique du jeu de Morpion
 * @author Yann REIBEL L3 INFO
 */
public class PlateauGUI extends JFrame {

    private CaseGUI[][] tabCaseGUI;

    private Client client;

    /**
     * Constructeur PlateauGUI
     */
    public PlateauGUI(Client client){
        this.client = client;
        this.setLayout(new GridLayout(Morpion.TAILLE, Morpion.TAILLE));
        this.tabCaseGUI = new CaseGUI[Morpion.TAILLE][Morpion.TAILLE];

        this.initialisationGrilleGUI();
        this.ajouterCasesGUI();

        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    /**
     * Initialisation de la grille sous forme graphique
     */
    public void initialisationGrilleGUI(){
        for(int i=0; i<Morpion.TAILLE; i++){
            for (int j=0; j<Morpion.TAILLE; j++){
                this.tabCaseGUI[i][j] = new CaseGUI(this, i, j);
            }
        }
    }

    /**
     * Ajoute les cases graphiques à la fenêtre représentant le plateau
     */
    public void ajouterCasesGUI(){
        for(int i=0; i<Morpion.TAILLE; i++){
            for (int j=0; j<Morpion.TAILLE; j++){
                this.add(this.tabCaseGUI[i][j]);
            }
        }
    }

    /**
     * Retourne le client
     * @return Client
     */
    public Client getClient(){
        return this.client;
    }

    public CaseGUI getCaseGUI(int i, int j){
        return this.tabCaseGUI[i][j];
    }
}
