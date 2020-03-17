package gui;



import javax.swing.*;

/**
 * Classe FinPartie
 * @author Yann REIBEL L3 INFO
 */
public class FinPartie extends JFrame {

    private JLabel labelWinner;
    private JButton btnRejouter;

    private String vainqueur;

    /**
     * Constructeur FinPartie
     */
    public FinPartie(String vainqueur){
        super("Fin de la partie");
        /*if(this.vainqueur != null){
            this.vainqueur = vainqueur;
        }else{
            this.vainqueur = "Egalité";
        }*/

        this.vainqueur = vainqueur;


        this.initialiserComposants();

        this.pack();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Initialise les composants dans la fenêtre
     */
    public void initialiserComposants(){
        this.labelWinner = new JLabel(this.vainqueur);
        this.add(this.labelWinner);
    }


}