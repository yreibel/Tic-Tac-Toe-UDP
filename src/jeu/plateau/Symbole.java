package jeu.plateau;

/**
 * Classe Symbole
 * @author Yann REIBEL L3 INFO
 */
public enum Symbole {
    X('X', "croix"), O('O', "rond"), VIDE('1', "vide");

    private char charSymbole;
    private String icone;

    /**
     * Constructeur Valeur
     * @param symbole
     */
    private Symbole(char symbole, String icone){
        this.charSymbole = symbole;
        this.icone = icone;
    }

    /**
     * Retourne le nom de l'icone
     * @return String
     */
    public String getIcone(){
        return this.icone;
    }

    /**
     * Retourne le symbole sous la forme d'un caractère
     * @return char
     */
    public char getCharSymbole(){
        return this.charSymbole;
    }
}
