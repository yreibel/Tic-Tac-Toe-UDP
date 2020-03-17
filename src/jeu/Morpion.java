package jeu;

import jeu.joueurs.Joueur;
import jeu.joueurs.PaireJoueurs;
import jeu.plateau.Plateau;
import jeu.plateau.Symbole;

import java.net.InetAddress;

/**
 * Classe Morpion
 * Classe permettant la gestion du jeu global (joueurs, plateau)
 * @author Yann REIBEL L3 INFO
 */
public class Morpion {

    public static final int TAILLE = 3;

    private PaireJoueurs paireJoueurs;
    private Plateau plateau;
    private Joueur joueurEnCours;
    private Joueur vainqueur;

    private int nbCoups;

    /**
     * Constructeur Morpion
     * @param paireJoueurs
     */
    public Morpion(PaireJoueurs paireJoueurs){
        this.paireJoueurs = paireJoueurs;
        this.plateau = new Plateau(this);

        this.getJoueurs().getJoueur1().setSymboleJoue(Symbole.O);
        this.getJoueurs().getJoueur2().setSymboleJoue(Symbole.X);

        this.joueurEnCours = this.joueurCommenceAjouer();
        this.vainqueur = null;
        this.nbCoups = 0;
    }

    /**
     * Retourne true si la partie est terminée
     * @return boolean
     */
    public boolean estFiniePartie(){
        if(this.conditionsVictoiresRemplies() || this.nbCoups == 9) return true;
        return false;
    }

    /**
     * Retourne true si les conditions de victoires sont remplies
     * @return boolean
     */
    public boolean conditionsVictoiresRemplies(){
        if(nSymbolesAlignes(Symbole.O)){
            this.vainqueur = joueurEnCours;
            return true;
        }
        if(nSymbolesAlignes(Symbole.X)){
            this.vainqueur = joueurEnCours;
            return true;
        }
        return false;
    }

    /**
     * Retourne true si n symboles identiques sont alignés, où n représente la taille du Morpion
     * @param symbole
     * @return boolean
     */
    public boolean nSymbolesAlignes(Symbole symbole){
        if( this.symbolesEnLigne(symbole)       ||
                this.symbolesEnColonne(symbole)     ||
                this.symbolesEnDiagGauche(symbole)  ||
                this.symbolesEnDiagDroite(symbole)
        ){
            return true;
        }
        return false;
    }

    /**
     * Retourne s'il y a une ligne de même symboles
     * @param symbole
     * @return boolean
     */
    public boolean symbolesEnLigne(Symbole symbole){

        for(int i=0; i<Morpion.TAILLE; i++){
            boolean aligne = true;
            for(int j=0; j<Morpion.TAILLE; j++){
                // vérifie chaque symbole de la ligne (si un false ça restera en false du coup...)
                aligne &= this.plateau.getCase(i, j).getSymbole() == symbole;

            }

            // Si aligne est vrai, inutile de continuer, on peut return true, si aligne = false alors on poursuit la boucle
            if(aligne) return true;

        }

        return false;
    }

    /**
     * Retourne si il y a une colonne de même symboles
     * @param symbole
     * @return boolean
     */
    public boolean symbolesEnColonne(Symbole symbole){
        for(int j=0; j<Morpion.TAILLE; j++){
            boolean aligne = true;
            for(int i=0; i<Morpion.TAILLE; i++){
                // vérifie chaque symbole de la colonne (si un false ça restera en false du coup...)
                aligne &= this.plateau.getCase(i, j).getSymbole() == symbole;
            }

            // Si aligne est vrai, inutile de continuer, on peut return true, si aligne = false alors on poursuit la boucle
            if(aligne) return true;

        }

        return false;
    }

    /**
     * Retourne s'il y a une diagonale partant à gauche de même symboles
     * @param symbole
     * @return boolean
     */
    public boolean symbolesEnDiagGauche(Symbole symbole){
        for(int i=0; i<Morpion.TAILLE; i++){
            if ( this.plateau.getCase(i, i).getSymbole() != symbole)
                return false;
        }
        return true;
    }

    /**
     * Retourne s'il y a une diagonale partant à droite de même symboles
     * @param symbole
     * @return boolean
     */
    public boolean symbolesEnDiagDroite(Symbole symbole){
        for(int i=0; i<Morpion.TAILLE; i++){
            if( this.plateau.getCase(Morpion.TAILLE - i - 1, i).getSymbole() != symbole )
                return false;
        }
        return true;
    }

    /**
     * Retourne le joueur commençant la partie
     * @return Joueur
     */
    public Joueur joueurCommenceAjouer(){
        int random = (int)(Math.random() * 2);
        if(random == 0) return this.paireJoueurs.getJoueur1();
        return this.paireJoueurs.getJoueur2();
    }


    /**
     * Vérifie si une case est occupée
     * @param i
     * @param j
     * @return boolean
     */
    public boolean estOccupe(int i, int j){
       if(!this.plateau.getCase(i, j).getSymbole().equals(Symbole.VIDE)) return true;
       return false;
    }

    /**
     * Vérifie si l'emplacement est valide dans le morpion
     * @param chaine
     * @return boolean
     */
    public boolean estValideEmplacement(String chaine){
        String[] tab = chaine.split(",");

        int i = Integer.parseInt(tab[0].trim());
        int j = Integer.parseInt(tab[1].trim());

        if(this.estOccupe(i, j)){
            //System.out.println("occupe");
            return false;
        }
        return true;
    }

    /**
     * Place le pion sur le plateau
     * @return boolean
     */
    public void placerPion(int x, int y, char symbole){
        this.plateau.getCase(x, y).setSymbole(symbole);
    }

    /**
     * Changer le joueur en train de jouer
     * @param joueur
     */
    public void setJoueurEnCours(Joueur joueur){
        this.joueurEnCours = joueur;
    }

    /**
     * Retourne les joueurs de la partie
     * @return PaireJoueurs
     */
    public PaireJoueurs getJoueurs(){
        return this.paireJoueurs;
    }

    /**
     * Retourne le plateau de la partie
     * @return Plateau
     */
    public Plateau getPlateau(){
        return this.plateau;
    }

    /**
     * Retourne le joueur en train de jouer
     * @return Joueur
     */
    public Joueur getJoueurEnCours(){
        return this.joueurEnCours;
    }

    /**
     * Retourne le vainqueur de la partie
     * @return Joueur
     */
    public Joueur getVainqueur(){
        return this.vainqueur;
    }

    /**
     * Augmente le nombre de coups joués
     */
    public void augmenterNombreCoups(){
        this.nbCoups++;
    }

}
