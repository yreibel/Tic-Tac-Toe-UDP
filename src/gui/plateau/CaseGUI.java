package gui.plateau;

import jeu.plateau.Case;
import jeu.plateau.Symbole;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 * Classe CaseGUI
 * Représentation graphique d'une case d'un Plateau
 * @author Yann REIBEL L3 INFO
 */
public class CaseGUI extends JButton implements ActionListener {

    private int x1;
    private int y1;

    private PlateauGUI plateauGUI;

    /**
     * Constructeur CaseGUI
     */
    public CaseGUI(PlateauGUI plateauGUI, int x1, int y1){
        this.plateauGUI = plateauGUI;
        this.x1 = x1;
        this.y1 = y1;

        this.setMargin(new Insets(0,0,0,0));
        this.remplacerImage("vide");

        this.addActionListener(this);
    }

    /**
     * Remplacer l'icone de la case par l'image passée en paramètre
     * @param img
     */
    public void remplacerImage(String img){
        try {
            URL imageUrl = ClassLoader.getSystemResource(img + ".png");
            Image image = ImageIO.read(imageUrl);
            this.setIcon(new ImageIcon(image));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * Place le symbole sur le plateau de jeu en mettant à jour le plateau virtuel (Plateau)
     * @param symbole
     */
    public void placerSymbole(Symbole symbole){
        this.remplacerImage(symbole.getIcone());
    }

    public void setChar(char car){
        if(car == 'O') this.placerSymbole(Symbole.O);
        else if(car == 'X') this.placerSymbole(Symbole.X);
        else this.placerSymbole(Symbole.VIDE);
    }

    /**
     * Retourne la position x de la case
     * @return int
     */
    public int getX1(){
        return this.x1;
    }

    /**
     * Retourne la position y de la case
     * @return int
     */
    public int getY1(){
        return this.y1;
    }


    @Override
    public void actionPerformed(ActionEvent e) {

        String message = "/p/" + this.getX1() + "," + this.getY1();
        System.out.println("poseee");
        this.plateauGUI.getClient().envoyer(message.getBytes());
    }
}
