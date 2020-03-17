package gui;

import reseau.Client;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Classe Login
 * Fenêtre de login pour la connexion au serveur
 * @author Yann REIBEL L3 INFO
 */
public class Login extends JFrame {

    private JTextField txtPseudo;
    private JTextField txtAdresse;
    private JTextField txtPort;


    private JLabel lblPseudo;
    private JLabel lblAdresse;
    private JLabel lblPort;
    private JLabel lblLogin;

    private JButton btnLogin;

    private JPanel panelSaisie;
    private JPanel panelLogin;


    /**
     * Constructeur Login
     */
    public Login( ) {
        this.setTitle("Fenetre de connexion");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(400,400);

        this.setLayout(new GridLayout(2,1));
        this.panelSaisie = new JPanel();
        this.panelSaisie.setLayout(new GridLayout(3, 2));
        this.panelLogin = new JPanel();
        this.panelLogin.setLayout(new FlowLayout());


        this.txtPseudo = new JTextField();
        this.txtPort = new JTextField();
        this.txtPort.setText("9000");
        this.txtAdresse = new JTextField();
        this.txtAdresse.setText("127.0.0.1");

        this.lblPseudo = new JLabel("Pseudonyme");
        this.lblAdresse = new JLabel("Adresse IP");
        this.lblPort = new JLabel("Port");

        this.btnLogin = new JButton("Login");
        this.btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                String pseudo = txtPseudo.getText();
                String adresse = txtAdresse.getText();
                int port = Integer.parseInt(txtPort.getText());

                login(pseudo, adresse, port);

            }
        });

        this.lblLogin = new JLabel("");

        this.panelSaisie.add(this.lblPseudo);
        this.panelSaisie.add(this.txtPseudo);
        this.panelSaisie.add(this.lblAdresse);
        this.panelSaisie.add(this.txtAdresse);
        this.panelSaisie.add(this.lblPort);
        this.panelSaisie.add(this.txtPort);

        this.panelLogin.add(this.btnLogin);
        this.panelLogin.add(this.lblLogin);

        this.add(this.panelSaisie);
        this.add(this.panelLogin);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    /**
     * Crée le Client et ferme la fenêtre de login
     * @param pseudo
     * @param adresse
     * @param port
     */
    public void login(String pseudo, String adresse, int port) {
        this.dispose();
        System.out.println("pseudo : " + pseudo + " adresse : " + adresse + " port" + port);
        new Client(this, pseudo, adresse, port);


    }

    /**
     * Inscrit une erreur de connexion au label
     * @param erreur
     */
    public void erreurConnexion(String erreur){
        this.lblLogin.setText(erreur);
    }

    /**
     * Main de lancement
     * @param args
     */
    public static void main(String[] args) {
        new Login();
    }
}
