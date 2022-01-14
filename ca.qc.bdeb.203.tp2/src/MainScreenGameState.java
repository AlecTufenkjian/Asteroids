/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 *
 * @author ryang
 */
public class MainScreenGameState extends BasicGameState{
    Font font = new Font("Arial", 1, 30); //font du text a ecrire
    TrueTypeFont ttf = new TrueTypeFont(font, true);
    
    public static final int ID = 1;
    
    private Image background;
    private StateBasedGame game;
    
    @Override
    public int getID() {
        return ID;
    }
    @Override
  public void keyPressed(int key, char c) {
    game.enterState(Jeu.ID);
  }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        this.game = sbg;
        this.background = new Image("images/background.jpg");
    }

    @Override
    public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
        background.draw(0, 0, container.getWidth(), container.getHeight());
        ttf.drawString( MainClass.LARGEUR/2- 300, MainClass.HAUTEUR/2- 20, "Appuyer sur une touche pour commencer", Color.white);
    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int i) throws SlickException {
    }
    //il n'y a rien qu'on doit changer ici
}
