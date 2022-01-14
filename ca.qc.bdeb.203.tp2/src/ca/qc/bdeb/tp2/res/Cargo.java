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
public class Cargo {
    private boolean estEnvoye = false;
    private int etatCargo = 0;
    private int descelerateur = 0;

    public Cargo() {
    }
    
    public void lancerCargaison(){
        estEnvoye = true;
    }

    public int getEtatCargo() {
        return etatCargo;
    }

    public boolean getEstEnvoye() {
        return estEnvoye;
    }

    public void setEstEnvoye(boolean estEnvoye) {
        this.estEnvoye = estEnvoye;
    }

    public void setEtatCargo(int etatCargo) {
        this.etatCargo = etatCargo;
    }

    public void setDescelerateur(int descelerateur) {
        this.descelerateur = descelerateur;
    }

    public int getDescelerateur() {
        return descelerateur;
    }
    
    
    
    
}
