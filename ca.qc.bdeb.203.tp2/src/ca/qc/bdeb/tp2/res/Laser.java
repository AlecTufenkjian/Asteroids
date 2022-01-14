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
public class Laser extends Entite implements Collisionable {
    
    public Laser(int x, int y, int width, int height, String imagepath) {
        super(x, y, width, height, imagepath);
    }

    @Override
    public void collisioner() {
        
    }
    
    
}
