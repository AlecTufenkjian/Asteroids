/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.tp2.res;


/**
 *
 * @author Alec
 */
public class Vaisseau extends Entite implements Collisionable{
    private boolean flashVaisseau = false;
    private boolean immunitaire = false;
    private boolean estEnMouvement = false;
    private int tempsImmunite = 0;
    private int nbCollisions = 0;
    private int direction = 0;
    
    public Vaisseau(int x, int y, int width, int height, String imagepath) {
        super(x, y, width, height, imagepath);
    }

    @Override
    public void collisioner() {
        
    }

    public int getNbCollisions() {
        return nbCollisions;
    }

    public int getTempsImmunite() {
        return tempsImmunite;
    }
    
    public boolean isImmunitaire() {
        return immunitaire;
    }

    public void setNbCollisions(int nbCollisions) {
        this.nbCollisions = nbCollisions;
    }

    public void setImmunitaire(boolean immunitaire) {
        this.immunitaire = immunitaire;
    }

    public void setTempsImmunite(int tempsImmunite) {
        this.tempsImmunite = tempsImmunite;
    }

    public boolean getEstEnMouvement() {
        return estEnMouvement;
    }

    public void setEstEnMouvement(boolean estEnMouvement) {
        this.estEnMouvement = estEnMouvement;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean getFlashVaisseau() {
        return flashVaisseau;
    }

    public void setFlashVaisseau(boolean flashVaisseau) {
        this.flashVaisseau = flashVaisseau;
    }
    
    
    
    
    
    
    

    
}
