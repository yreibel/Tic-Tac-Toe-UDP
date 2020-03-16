package jeu.plateau;

/**
 * Classe Case
 * Représente la case d'un plateau de jeu de Morpion
 * @author Yann REIBEL L3 INFO
 */
public class Case{

    private int x;
    private int y;

    private Symbole symbole;
    private Plateau plateau;

    /**
     * Constructeur Case
     * @param x
     * @param y
     */
    public Case(int x, int y, Plateau plateau){
        this.x = x;
        this.y = y;
        this.plateau = plateau;

        this.symbole = Symbole.VIDE;
    }

    /**
     * Retourne la position X de la case
     * @return int
     */
    public int getX(){
        return this.x;
    }

    /**
     * Retourne la position Y de la case
     * @return int
     */
    public int getY(){
        return this.y;
    }

    /**
     * Retourne la valeur de la case
     * @return Symbole
     */
    public Symbole getSymbole(){
        return this.symbole;
    }

    /**
     * Retourne s'il s'agit d'un symbole gagnant
     * 3X ou 3O côte à côté permettent de gagner la partie
     * @return boolean
     */
    public boolean estUnSymboleGagnant(){
        if(this.symbole == Symbole.X || this.symbole == Symbole.O){
            return true;
        }

        return false;
    }

    /**
     * Applique la valeur passée en paramètre à la case
     * @param symbole
     */
    public void setValeur(Symbole symbole){
        this.symbole = symbole;
    }

    /**
     * Applique un symbole à la case
     * @param car
     */
    public void setSymbole(char car){
        if(car == 'O') this.symbole = Symbole.O;
        else if(car == 'X') this.symbole = Symbole.X;
        else this.symbole = Symbole.VIDE;
    }

    /**
     * Retourne le plateau contenant la case
     * @return Plateau
     */
    public Plateau getPlateau(){
        return this.plateau;
    }

}
