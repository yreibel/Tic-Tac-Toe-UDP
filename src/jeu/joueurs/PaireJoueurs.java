package jeu.joueurs;

/**
 * Classe PaireJoueurs
 * Paire de joueurs d'une partie de Morpion
 * @author Yann REIBEL L3 INFO
 */
public class PaireJoueurs {
    private Joueur joueur1;
    private Joueur joueur2;

    /**
     * Constructeur PaireJoueurs
     * @param joueur1
     * @param joueur2
     */
    public PaireJoueurs(Joueur joueur1, Joueur joueur2){
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
    }

    /**
     * Retourne le joueur 1
     * @return Joueur
     */
    public Joueur getJoueur1(){
        return this.joueur1;
    }

    /**
     * Retourne le joueur 2
     * @return Joueur
     */
    public Joueur getJoueur2(){
        return this.joueur2;
    }

    /**
     * Retourne l'autre Joueur de la paire à partir de celui passé en paramètre
     * @param j
     * @return Joueur
     */
    public Joueur getAutreJoueur(Joueur j){
        if(j.equals(this.joueur1)) return this.joueur2;
        if(j.equals(this.joueur2)) return this.joueur1;

        return null;
    }
}
