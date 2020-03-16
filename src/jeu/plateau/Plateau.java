package jeu.plateau;

import jeu.Morpion;

/**
 * Classe Plateau
 * @author Yann REIBEL L3 INFO
 */
public class Plateau {

    private Case[][] tabCases;
    private Morpion morpion;

    /**
     * Constructeur PlateauGUI
     */
    public Plateau(Morpion morpion) {
        this.morpion = morpion;
        this.tabCases = new Case[Morpion.TAILLE][Morpion.TAILLE];
        this.initialiserPlateauVide();
    }

    /**
     * Initialisation du plateau de Case
     */
    public void initialiserPlateauVide(){
        for(int i=0; i < Morpion.TAILLE; i++){
            for(int j=0; j < Morpion.TAILLE; j++){
                this.tabCases[i][j] = new Case(i,j, this);
            }
        }
    }

    /**
     * Retourne la Case à la position [x][y]
     * @param x
     * @param y
     * @return Case
     */
    public Case getCase(int x, int y){
        return this.tabCases[x][y];
    }

    /**
     * Retourne le tableau de Case
     * @return Case[][]
     */
    public Case[][] getTabCases(){
        return this.tabCases;
    }

    /**
     * Retourne le morpion contenant ce plateau
     * @return Morpion
     */
    public Morpion getMorpion(){
        return this.morpion;
    }

    /**
     * Retourne un formatage du plateau virtuel
     * @return String
     */
    public String toString(){
        String s ="";

        for(int i=0; i<this.tabCases.length; i++){
            for(int j=0; j<this.tabCases[i].length; j++){
                s+=this.tabCases[i][j].getSymbole().getCharSymbole();
            }
            s+="\n";
        }
        return s;
    }

}

