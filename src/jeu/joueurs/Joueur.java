package jeu.joueurs;

import jeu.plateau.Symbole;

import java.net.InetAddress;

/**
 * Classe Joueur
 * Représente le joueur d'une partie de Morpion
 * @author Yann REIBEL L3 INFO
 */
public class Joueur {

    private String pseudo;
    private InetAddress adresse;
    private int port;
    private Symbole symboleJoue;

    private static int id;

    /**
     * Constructeur ClientServeur
     * @param pseudo
     * @param adresse
     * @param port
     */
    public Joueur(String pseudo, InetAddress adresse, int port) {
        this.pseudo = pseudo;
        this.adresse = adresse;
        this.id++;
        this.port = port;
        this.symboleJoue = null;
    }

    /**
     * Constructeur pour Joueur serveur
     */
    public Joueur(){
        this.pseudo = "OrdiMan";
        this.symboleJoue = null;
    }

    /**
     * Applique un symbole à jouer au joueur
     * @param symboleJoue
     */
    public void setSymboleJoue(Symbole symboleJoue){
        this.symboleJoue = symboleJoue;
    }

    /**
     * Retourne l'identifiant du client
     * @return int
     */
    public int getID() {
        return this.id;
    }

    /**
     * Retourne le pseudonyme du client
     * @return String
     */
    public String getPseudo() {
        return this.pseudo;
    }

    /**
     * Retourne le symbole joué
     * @return Symbole
     */
    public Symbole getSymboleJoue(){
        return this.symboleJoue;
    }

    /**
     * Retourne l'adresse du client
     * @return InetAddress
     */
    public InetAddress getAdresse() {
        return this.adresse;
    }

    /**
     * Retourne le port du client
     * @return int
     */
    public int getPort() {
        return this.port;
    }
}
