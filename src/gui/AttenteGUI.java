package gui;

import javax.swing.*;

/**
 * Classe Attente
 * Salle d'attente en attendant qu'un autre client se connecte pour débuter une partie de Morpion
 * @author Yann REIBEL L3 INFO
 */
public class AttenteGUI extends JFrame {

    private JLabel labelAttente;

    /**
     * Constructeur AttenteGUI
     */
    public AttenteGUI(){
        super("Attente début de partie");

        this.labelAttente = new JLabel("");
        this.add(this.labelAttente);

        this.pack();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void erreurConnexion(String erreur){
        this.labelAttente.setText(erreur);
    }
}
