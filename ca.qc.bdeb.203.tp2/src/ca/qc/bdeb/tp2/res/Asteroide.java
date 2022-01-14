/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.qc.bdeb.tp2.res;

import java.util.ArrayList;
import java.util.Random;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

/**
 *
 * @author Alec
 */
public class Asteroide extends Entite implements Bougeable, Collisionable {
    
    private ArrayList<Image> listeAnimation = new ArrayList<>();
    
    private boolean bougeHorizontalement;
    private boolean bougeVersLaDroite;
    
    private int tempsEntreDeplacement;

    private int animation = 0; 
    private int numeroImage = 0; 
    private int timer = 0;

    public Asteroide(int x, int y, int width, int height, SpriteSheet spriteSheet) {
        super(x, y, width, height, spriteSheet, 0, 0);       
        this.height = height;
        this.width = width;
        
        Random rnd = new Random();
        this.tempsEntreDeplacement = rnd.nextInt(5)+1;
        this.bougeHorizontalement = rnd.nextBoolean();
        this.bougeVersLaDroite = rnd.nextBoolean();
        
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {
                listeAnimation.add(spriteSheet.getSubImage(j, i).getScaledCopy(width, height));
            }
        }
    }
    
    @Override
    public void bouger() {
        
        this.image = listeAnimation.get(numeroImage);
        if(animation == 10){
            numeroImage++;
            animation = 0; 
            if (numeroImage == 4){
                numeroImage = 0;
            }
        }
        
        animation++;
        
        if(timer == tempsEntreDeplacement){
            this.setY(this.getY() + 2);
            
            if(bougeHorizontalement){
                if(bougeVersLaDroite){
                    this.setX(this.getX() + 1);
                }else{
                    this.setX(this.getX() - 1);
                }
            }
            timer = 0;
        }
        timer++;
        
    }

    @Override
    public void collisioner() {
        
    }

    
}
