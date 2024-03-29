package reseau;

import jeu.Morpion;
import jeu.joueurs.Joueur;
import jeu.joueurs.PaireJoueurs;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Serveur
 * Classe gérant les packets en provenance du Client
 * @author Yann REIBEL L3 INFO
 */
public class Serveur implements Runnable {

    private List<Joueur> listeClients;

    private DatagramSocket socket;

    private Thread lancement;
    private Thread receptionPackets;

    private Morpion morpion;

    private int port;
    private boolean enCours = false;

    /**
     * Constructeur Serveur
     * @param port
     */
    public Serveur(int port) {
        this.port = port;
        this.listeClients = new ArrayList<>();

        try {
            this.socket = new DatagramSocket(port);
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.lancement = new Thread(this, "Serveur");
        this.lancement.start();

    }


    @Override
    public void run() {
        this.enCours = true;
        System.out.println("Serveur lancé sur le port " + port);
        this.receptionner();
    }

    /**
     * Envoi des packets aux clients
     * @param data
     */
    public void envoyer(final byte[] data) {
        for(Joueur j : listeClients){
            DatagramPacket packet = new DatagramPacket(data, data.length, j.getAdresse(), j.getPort());
            try {
                socket.send(packet);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * Réception des packets des clients
     */
    private void receptionner() {
        this.receptionPackets = new Thread("ReceptionServeur") {
            public void run() {
                while(enCours) {
                    byte[] data = new byte[1024];
                    DatagramPacket packet = new DatagramPacket(data, data.length);
                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // Traite le packet reçu
                    traiterPacket(packet);
                    if(listeClients.size() == 2 && morpion == null){
                        commencerJeuContreJoueur(listeClients.get(0), listeClients.get(1));
                        // Envoi du packet de début de partie
                        String messageDebut = "/d/";
                        envoyer(messageDebut.getBytes());
                    }

                }
            }
        };
        this.receptionPackets.start();
    }

    /**
     * Traite le packet reçu selon la
     * @param packet
     */
    public void traiterPacket(DatagramPacket packet){
        String messageRecu = new String(packet.getData());
        String messageSansControle = messageRecu.substring(3);

        // Réception du packet de connexion
        if(messageRecu.startsWith("/c/")){
            // Ajout du client à la liste

            if(listeClients.size() < 2) {
                System.out.println("test liste ");
                Joueur j = new Joueur(messageRecu.substring(3), packet.getAddress(), packet.getPort());
                listeClients.add(j);

            }else if(listeClients.size() == 2){
                System.out.println("il y a trop de monde");
            }

        }

        // Réception packet position
        if(messageRecu.startsWith("/p/")){
            this.actionPositionServeur(packet, messageSansControle);
        }


    }


    /**
     * Action lorsqu'un packet contenant la position est reçue
     * @param packet
     * @param messageSansControle
     */
    public void actionPositionServeur(DatagramPacket packet, String messageSansControle){

        if( this.morpion.estValideEmplacement(messageSansControle) &&
            this.morpion.getJoueurEnCours().getAdresse().equals(packet.getAddress()) &&
            this.morpion.getJoueurEnCours().getPort() == packet.getPort()
        ){

            String messagePosition;
            String messageFinPartie;

            char symbole = this.getSymboleJoueur(packet.getAddress(), packet.getPort());
            this.traitementPositionPacket(messageSansControle, symbole);
            // Symbole correspondant au joueur
            messagePosition = "/p/" + symbole + ":" + messageSansControle;
            this.envoyer(messagePosition.getBytes());

            if(this.morpion.estFiniePartie()){
                if(this.morpion.getVainqueur().getPseudo() == null)
                    messageFinPartie = "/f/" + "egalite";
                else {
                    // On applique l'autre joueur comme vainqueur étant donné que le partie se termine lorsque le joueur précédent a gagné
                    this.morpion.setVainqueur(this.morpion.getJoueurs().getAutreJoueur(this.morpion.getVainqueur()));
                    messageFinPartie = "/f/" + this.morpion.getVainqueur().getPseudo();
                }
                this.envoyer(messageFinPartie.getBytes());
            }


            /*// Serveur joue
            if(!this.morpion.estFiniePartie()){

                String donnees = this.serveurJoue();
                messagePosition = "/p/" + donnees;
                this.envoyer(messagePosition.getBytes());

                // Si une fois que le serveur a joué la partie est considérée comme terminée alors envoyer un message au client
                if(this.morpion.estFiniePartie()){

                    if(this.morpion.getVainqueur().getPseudo() == null)
                        messageFinPartie = "/f/" + "egalite";
                    else {
                        // On applique l'autre joueur comme vainqueur étant donné que le partie se termine lorsque le joueur précédent a gagné
                        this.morpion.setVainqueur(this.morpion.getJoueurs().getAutreJoueur(this.morpion.getVainqueur()));
                        messageFinPartie = "/f/" + this.morpion.getVainqueur().getPseudo();
                    }

                    this.envoyer(messageFinPartie.getBytes());
                }
            }
            else{
                if(this.morpion.getVainqueur().getPseudo() == null)
                    messageFinPartie = "/f/" + "egalite";
                else {
                    // On applique l'autre joueur comme vainqueur étant donné que le partie se termine lorsque le joueur précédent a gagné
                    this.morpion.setVainqueur(this.morpion.getJoueurs().getAutreJoueur(this.morpion.getVainqueur()));
                    messageFinPartie = "/f/" + this.morpion.getVainqueur().getPseudo();
                }

                this.envoyer(messageFinPartie.getBytes());
            }
            */
        }
        else{
            String messageInvalide = "/e/" + "impo";
            // Envoi au client que l'emplacement souhaité est invalide
            this.envoyer(messageInvalide.getBytes());
        }
    }



    /**
     * Ajoute le symbole reçu par le joueur sur le plateau de jeu associé au serveur
     * @param messageSansControle
     * @param symbole
     */
    public void traitementPositionPacket(String messageSansControle, char symbole){
        String[] position = messageSansControle.split(",");

        int j = Integer.parseInt(position[1].trim());
        int i = Integer.parseInt(position[0]);

        // Applique le jeu au serveur
        this.morpion.placerPion(i, j, symbole);
        this.morpion.augmenterNombreCoups();
        this.morpion.setJoueurEnCours(this.morpion.getJoueurs().getAutreJoueur(this.morpion.getJoueurEnCours()));
    }


    /**
     * Retourne le symbole associé au joueur
     * @param adresse
     * @param port
     * @return char
     */
    public char getSymboleJoueur(InetAddress adresse, int port){
        for(Joueur j : listeClients){
            if(j.getAdresse().equals(adresse) && j.getPort() == port){
                if(j.getSymboleJoue() != null){
                    return j.getSymboleJoue().getCharSymbole();
                }
            }
        }
        return 'l';
    }

    /**
     * Commence la partie contre le serveur
     */
    public String commencerJeuContreServeur(Joueur joueurReel){
        Joueur ordinateur = new Joueur();
        PaireJoueurs paireJoueurs = new PaireJoueurs(joueurReel, ordinateur);

        this.morpion = new Morpion(paireJoueurs);

        // test

        if(this.morpion.getJoueurEnCours() == this.morpion.getJoueurs().getJoueur2()){
           return this.serveurJoue();
        }else{
            return "";
        }
    }

    /**
     * Commence la partie avec un autre joueur
     * @param joueur1
     * @param joueur2
     */
    public void commencerJeuContreJoueur(Joueur joueur1, Joueur joueur2){
        PaireJoueurs paireJoueurs = new PaireJoueurs(joueur1, joueur2);
        this.morpion = new Morpion(paireJoueurs);
    }

    /**
     * Fait jouer le serveur et retourne les données (symbole + position) sous forme de chaine à envoyer au client
     * @return String
     */
    public String serveurJoue(){
        int i = (int)(Math.random() * 3);
        int j = (int)(Math.random() * 3);
        String pos = i + "," + j;

        while(!this.morpion.estValideEmplacement(pos)){
            i = (int)(Math.random() * 3);
            j = (int)(Math.random() * 3);
            pos = i + "," + j;
        }

        char symbole =  this.morpion.getJoueurs().getJoueur2().getSymboleJoue().getCharSymbole();

        this.morpion.placerPion(i, j, symbole);
        this.morpion.setJoueurEnCours(this.morpion.getJoueurs().getAutreJoueur(this.morpion.getJoueurEnCours()));
        this.morpion.augmenterNombreCoups();

        // Retourne l'information sous forme de chaîne
        return symbole + ":" + pos;
    }

    /**
     * Main de lancement du Serveur
     * @param args
     */
    public static void main(String[] args){
        // Lance le serveur sur le port 9000
        new Serveur(9000);
    }
}
