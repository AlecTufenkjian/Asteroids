/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author ryang
 */
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alec
 */
public class MainClass extends StateBasedGame{

    public static final int LARGEUR = 1024, HAUTEUR = 768; // Constantes publiques de taille

    public static void main(String[] args){
        try {
            AppGameContainer app = new AppGameContainer(new MainClass("Asteroide"), LARGEUR, HAUTEUR, false);
            app.setVSync(true);
            app.start();
        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public MainClass(String name) {
        super(name);
    }

    @Override
    public void initStatesList(GameContainer gc) throws SlickException {
        addState(new MainScreenGameState());
        addState(new Jeu());
    }// initialisation des boucles de jeu
    
    
}