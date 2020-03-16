package reseau;

import gui.AttenteGUI;
import gui.FinPartie;
import gui.Login;
import gui.plateau.PlateauGUI;
import jeu.plateau.Symbole;

import java.io.IOException;
import java.net.*;

/**
 * Classe Client
 * Client recevant les packets du serveur
 * @author Yann REIBEL L3 INFO
 */
public class Client implements Runnable {

    private String pseudo;
    private String adresse;
    private int port;
    private boolean enCours = false;

    private DatagramSocket socket;
    private InetAddress ip;

    private Login login;
    private AttenteGUI attenteGUI;
    private PlateauGUI plateauGUI;
    private FinPartie finPartieGUI;

    private Thread reception;
    private Thread lancement;

    /**
     * Constructeur Client
     * @param adresse
     * @param port
     */
    public Client(Login login, String pseudo, String adresse, int port) {
        this.login = login;
        this.pseudo = pseudo;
        this.adresse = adresse;
        this.port = port;

        this.attenteGUI = new AttenteGUI();

        boolean connecte = this.connexion(adresse);
        if(!connecte) {
           this.attenteGUI.erreurConnexion("Impossible de se connecter");

        }else{
            this.attenteGUI.erreurConnexion("Connecté sur le port " + port + " en attente d'autres joueurs");
        }

        // Envoi du client au serveur
        String message = "/c/" + this.pseudo;
        this.envoyer(message.getBytes());

        this.lancement = new Thread(this, "LancementClient");
        this.lancement.start();
    }

    /**
     * Permet de lancer une connexion au serveur
     * @param adresse
     * @return boolean
     */
    public boolean connexion(String adresse) {
        try {
            this.socket = new DatagramSocket();
            this.ip = InetAddress.getByName(adresse);

        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Retourne un String créé depuis un packet
      * @return String
     */
    public String receptionner(){
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        try {
            socket.receive(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message = new String(packet.getData());
        //System.out.println(message);
        return message;
    }

    /**
     * Envoie un DatagramPacket au serveur
     * @param data
     */
    public void envoyer(final byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, this.ip, this.port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        this.enCours = true;
        System.out.println("testt");
        this.attenteReception();

    }

    public void attenteReception(){
        this.reception = new Thread("AttenteReceptionClient"){
            public void run(){
                while(enCours){
                    String message = receptionner();
                    traiterPacket(message);
                }
            }
        };
        this.reception.start();
    }

    /**
     * Traite le packet en fonction du pré-message envoyé
     * @param message
     */
    public void traiterPacket(String message){
        String messageSansControle = message.substring(3);

        // Réception début de partie
        if(message.startsWith("/d/")){
            this.debutPartieClient();
        }
        // Réception la position du symbole
        if(message.startsWith("/p/")){
            this.traitementPositionPacket(messageSansControle);
        }
        // Reçoit un message d'erreur
        if(message.startsWith("/e/")){
            System.out.println("Impossible de cliquer ici !");
        }

        if(message.startsWith("/f/")){
            this.finDePartie(messageSansControle);
        }

    }

    /**
     * Action de début de partie
     */
    public void debutPartieClient(){
        this.attenteGUI.dispose();
        this.plateauGUI = new PlateauGUI(this);
    }

    /**
     * Action de fin de partie
     * Lance la fenêtre de fin de partie
     */
    public void finDePartie(String vainqueur){
        this.plateauGUI.dispose();
        this.finPartieGUI = new FinPartie(vainqueur);
    }

    /**
     * Action lorsque la position est reçue par le client depuis le serveur
     * @param messageSansControle
     */
    public void traitementPositionPacket(String messageSansControle){
        String[] tab = messageSansControle.split(":");
        System.out.println(tab[0]);

        String[] position = tab[1].split(",");

        int j = Integer.parseInt(position[1].trim());
        int i = Integer.parseInt(position[0]);

        this.plateauGUI.getCaseGUI(i, j).setChar(tab[0].charAt(0));
    }

    /**
     * Retourne le pseudo du client
     * @return String
     */
    public String getPseudo(){
        return this.pseudo;
    }

    /**
     * Retourne le port du client
     * @return int
     */
    public int getPort(){
        return this.port;
    }

    /**
     * Retourne l'adresse du client
     * @return String
     */
    public String getAdresse(){
        return this.adresse;
    }


}
