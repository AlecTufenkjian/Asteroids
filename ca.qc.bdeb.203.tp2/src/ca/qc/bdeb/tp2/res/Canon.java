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
public class Canon extends Entite implements Bougeable, Collisionable {
    
    public int positionInitialY;
    
    
    public Canon(int x, int y, int width, int height, String imagepath, int positionInitialY) {
        super(x, y, width, height, imagepath);
        this.positionInitialY = positionInitialY;
    }

    public int getPositionInitialY() {
        return positionInitialY;
    }
    
    @Override
    public void bouger(){
        this.setY(this.getY() - 3);
    }

    @Override
    public void collisioner() {
        
    }
}
