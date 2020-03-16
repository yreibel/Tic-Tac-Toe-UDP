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
            Joueur j = new Joueur(messageRecu.substring(3), packet.getAddress(), packet.getPort());
            listeClients.add(j);

            // Commence la partie contre le serveur lorsque le joueur rejoint la partie
            this.commencerJeuContreServeur(j);
            String messageDebut = "/d/";
            this.envoyer(messageDebut.getBytes());

        }
        // Réception packet position
        if(messageRecu.startsWith("/p/")){

           if(this.morpion.estValideEmplacement(messageSansControle)){
                char symbole = this.getSymboleJoueur(packet.getAddress(), packet.getPort());
                this.traitementPositionPacket(messageSansControle, symbole);
                // Symbole correspondant au joueur

                String messagePosition = "/p/" + symbole + ":" + messageSansControle;
                this.envoyer(messagePosition.getBytes());

                // Serveur joue
                if(!this.morpion.estFiniePartie()){
                    String donnees = this.serveurJoue();
                    messagePosition = "/p/" + donnees;
                    this.envoyer(messagePosition.getBytes());
                }
                else{
                    String messageFinpartie = "/f/" + this.morpion.getVainqueur().getPseudo();
                    this.envoyer(messageFinpartie.getBytes());
                }

            }
            else{
                String messageInvalide = "/e/" + "impo";
                // Envoi au client que l'emplacement souhaité est invalide
                this.envoyer(messageInvalide.getBytes());

            }
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

        this.morpion.placerPion(i, j, symbole);
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
    public void commencerJeuContreServeur(Joueur joueurReel){
        Joueur ordinateur = new Joueur();
        PaireJoueurs paireJoueurs = new PaireJoueurs(joueurReel, ordinateur);

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
        System.out.println("position tirée : " + i + ":" + j);
        while(!this.morpion.estValideEmplacement(pos)){

            i = (int)(Math.random() * 3);
            j = (int)(Math.random() * 3);
            pos = i + "," + j;
            System.out.println("position tirée : " + i + ":" + j);
        }


        char symbole =  this.morpion.getJoueurs().getJoueur2().getSymboleJoue().getCharSymbole();
        this.morpion.placerPion(i, j, symbole);

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