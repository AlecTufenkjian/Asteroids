package ca.qc.bdeb.tp2.res;


import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alec
 */
public abstract class Entite {
    protected int x, y, width, height;
    protected Image image;


    public Entite(int x, int y, int width, int height, String imagepath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        try {
            image = new Image(imagepath);
        } catch (SlickException e) {
            System.out.println("Image non trouvée pour " + getClass());
        }
    }
    
    public Entite(int x, int y, int width, int height, SpriteSheet spriteSheet, int ligne, int colonne) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = spriteSheet.getSubImage(ligne, colonne);
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    

    public int getX() { // Position en X du coin supérieur gauche de l’Entite
        return x;
    }

    public int getY() { // Position en Y du coin supérieur gauche de l’Entite
        return y;
    }

    public int getWidth() { // Largeur de l’Entite
        return width;
    }

    public int getHeight() { // Hauteur de l’Entite
        return height;
    }

    public Image getImage() { // Retourne l’image de l’entité
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    
    public Rectangle getRectangle() { // Retourne le rectangle qui englobe l’entité
        return new Rectangle(x, y, width, height);
    }

    public void setLocation(int x, int y) { // Pour déplacer l’élément depuis le Jeu
        this.x = x;
        this.y = y;
    }

}
