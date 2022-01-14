
import ca.qc.bdeb.tp2.res.*;

import java.awt.Font;
import java.io.File;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javax.swing.JOptionPane;

public class Jeu extends BasicGameState {

    public static final int ID = 2;

    private GameContainer container;

    private Vaisseau vaisseau;
    private Laser laser;
    private BarreVide barreVide;
    private BarreDeVie barreDeVie;
    private BarreEnvoiMars barreMars;
    private Background background;
    private Cargo cargo;
    private final int HAUTEUR_BARRE_DE_VIE = 100;
    private final int LARGEUR_BARRE_DE_VIE = 267;
    private final int HAUTEUR_BARRE_ENVOI_MARS = 34;
    private final int LARGEUR_BARRE_ENVOI_MARS = 94;
    private final int HAUTEUR_VAISSEAU = 128;
    private final int LARGEUR_VAISSEAU = 128;
    private final int HAUTEUR_CANON = 20;
    private final int LARGEUR_CANON = 10;
    private final int HAUTEUR_LASER = 20;
    private final int LARGEUR_LASER = 128;
    private final int HAUTEUR_ASTEROIDE = 256;
    private final int LARGEUR_ASTEROIDE = 256;
    private final int HAUTEUR_BACKGROUND = 768;
    private final int LARGEUR_BACKGROUND = 1024;

    private final int POSITION_INITIAL_VAISSEAU_X = 300;
    private final int POSITION_INITIAL_VAISSEAU_Y = 500;
    private final int POSITION_BARRE_DE_VIE_X = 750;
    private final int POSITION_BARRE_DE_VIE_Y = 30;
    private final int POSITION_BARRE_ENVOI_MARS_X = 330;
    private final int POSITION_BARRE_ENVOI_MARS_Y = -8;

    private final int NOMBRE_ASTEROIDES_PAR_SERIE = 3;
    private final int INTERVALLE_ENTRE_SERIE_ASTEROIDES = 300;
    private final int INTERVALLE_IMMUNITE = 200;
    private final int TAILLE_MINIMALE_ASTEROIDE = 32;

    private ArrayList<Entite> listeEntite = new ArrayList<>();
    private ArrayList<Bougeable> listeEntiteBougeable = new ArrayList<>();

    private final Font FONT = new Font("Arial", 1, 30);
    private final TrueTypeFont TTF = new TrueTypeFont(FONT, true);

    private SpriteSheet spriteSheet;

    private float remplisseurBarreMars;
    private int tempsPasseApresUneSerie;
    private int scoreTotal;
    private int bgParcouru = 0;

    public Jeu() {
        super();
    }

    @Override
    public void init(GameContainer container, StateBasedGame sbg) throws SlickException {
        initialiserVariables();
        initialiserBackground();
        initialiserVaisseau();
        initialiserLaser();
        initialiserBarreDeVie();
        initialiserAsteroides();
        initialiserBarreEnvoiMars();

        this.container = container;
    }

    @Override
    public void update(GameContainer container, StateBasedGame sbg, int delta) throws SlickException {
        verifierFinDuJeu(container, sbg);
        creerNouvelleSerieAsteroide();

        ajusterPositionVaisseau(delta);
        ajusterPositionEntitesBougeables();
        ajusterTempsImmunite();
        ajusterBarreEnvoiMars();
        ajusterBackground();

        ArrayList<Entite> listeTemp = new ArrayList<>(); //Entites a supprimer
        ArrayList<Asteroide> listeTemp2 = new ArrayList<>(); //Entites a ajouter

        disparaitreCanon(listeTemp);
        gererCollisions(listeTemp, listeTemp2);
        supprimerAsteroidesHorsEcran(listeTemp);

        listeEntite.removeAll(listeTemp);
        listeEntiteBougeable.removeAll(listeTemp);
        listeTemp.clear();

        listeEntite.addAll(listeTemp2);
        listeEntiteBougeable.addAll(listeTemp2);
        listeTemp2.clear();
    }

    @Override
    public void render(GameContainer container, StateBasedGame sbg, Graphics g) throws SlickException {
        tracerBackground(g);
        tracerToutesLesEntites(g);
        tracerEtatDuJeu(g);
    }

    @Override
    public void keyReleased(int key, char c) {

        if (Input.KEY_ESCAPE == key) {
            this.container.exit();
        }

        traiterPlusieursTouches(key);
    }

    @Override
    public void keyPressed(int key, char c) {
        traiterClavier(key);
    }

    @Override
    public int getID() {
        return ID;
    }

    private void initialiserVaisseau() {
        vaisseau = new Vaisseau(POSITION_INITIAL_VAISSEAU_X, POSITION_INITIAL_VAISSEAU_Y, HAUTEUR_VAISSEAU, LARGEUR_VAISSEAU, "images/vaisseau.png");
        cargo = new Cargo();
        listeEntite.add(vaisseau);
    } // initialisation du vaisseau
    private void initialiserLaser() {
        laser = new Laser(vaisseau.getX(), vaisseau.getY() - 20, LARGEUR_LASER, HAUTEUR_LASER, "images/laser.png");
        listeEntite.add(laser);
    } // initialisation du laser
    private void initialiserBarreDeVie() {
        barreVide = new BarreVide(POSITION_BARRE_DE_VIE_X, POSITION_BARRE_DE_VIE_Y, LARGEUR_BARRE_DE_VIE, HAUTEUR_BARRE_DE_VIE, "images/barre de vie vide.png");
        barreDeVie = new BarreDeVie(POSITION_BARRE_DE_VIE_X + 20, POSITION_BARRE_DE_VIE_Y, LARGEUR_BARRE_DE_VIE - 17, HAUTEUR_BARRE_DE_VIE - 3, "images/barre de vie remplie.png");
    } // initialisation de la barre de vie avec ses 2 composantes: la barre vide et le remplissage
    private void initialiserAsteroides() throws SlickException {
        Random rnd = new Random();
        spriteSheet = new SpriteSheet("images/asteroide" + Integer.toString(rnd.nextInt(3)+1)+ ".png", LARGEUR_ASTEROIDE, HAUTEUR_ASTEROIDE);

        for (int i = 0; i < NOMBRE_ASTEROIDES_PAR_SERIE; i++) {
            Asteroide asteroide = new Asteroide(i * MainClass.LARGEUR / 3, -rnd.nextInt(100) - 256, LARGEUR_ASTEROIDE, HAUTEUR_ASTEROIDE, spriteSheet);
            listeEntiteBougeable.add(asteroide);
            listeEntite.add(asteroide);
        }
    } // initialisation des asteroides
    private void initialiserBarreEnvoiMars() {
        barreMars = new BarreEnvoiMars(POSITION_BARRE_ENVOI_MARS_X, POSITION_BARRE_ENVOI_MARS_Y, LARGEUR_BARRE_ENVOI_MARS, HAUTEUR_BARRE_ENVOI_MARS, "images/MarsBarre.png");
        barreMars.setImage(barreMars.getImage().getScaledCopy(300, 120));
    } // initialisation de la barre qui "load" l'envoi a mars
    private void initialiserBackground() {
        this.background = new Background(0, 0, LARGEUR_BACKGROUND, HAUTEUR_BACKGROUND, "images/background.jpg");
        background.setY(background.getY() - 30);
    } // initialisation du background
    private void initialiserVariables() {
        tempsPasseApresUneSerie = 0;
        remplisseurBarreMars = 0;
        scoreTotal = 0;
    } // initialisation des variables qui doivent etre remises a 0 quand on recommence le jeu

    private void ajusterBackground() {
        background.setY(background.getY() - 10);
        if (bgParcouru != background.getY() / background.getHeight()) {
            bgParcouru = -background.getY() / background.getHeight();
        }
    } // mouvement du background
    private void ajusterTempsImmunite() {
        if (vaisseau.isImmunitaire()) {
            if (vaisseau.getTempsImmunite() % 25 == 0) {
                if (vaisseau.getFlashVaisseau()) {
                    vaisseau.setFlashVaisseau(false);
                } else {
                    vaisseau.setFlashVaisseau(true);
                }
            }
            vaisseau.setTempsImmunite(vaisseau.getTempsImmunite() + 1);

            if (vaisseau.getTempsImmunite() == INTERVALLE_IMMUNITE) {
                vaisseau.setTempsImmunite(0);
                vaisseau.setImmunitaire(false);
            }
        }
    } // ajustement du temps d'immunite apres avoir touche un asteroide et perdu une vie
    private void ajusterBarreEnvoiMars() {
        if (cargo.getDescelerateur() == 5) {
            if (cargo.getEstEnvoye()) {
                if (remplisseurBarreMars != 2.5f) {
                    remplisseurBarreMars = remplisseurBarreMars + 0.1f;
                }
                if (remplisseurBarreMars > 2.5f) {
                    remplisseurBarreMars = 0;
                    cargo.setEstEnvoye(false);
                    scoreTotal += cargo.getEtatCargo();
                    cargo.setEtatCargo(0);
                }
            }
            cargo.setDescelerateur(0);
        } else {
            cargo.setDescelerateur(cargo.getDescelerateur() + 1);
        }
    } // ajustement du remplissage de la barre d'envoi a mars
    private void ajusterPositionVaisseau(int delta) {
        int positionVaisseauX = vaisseau.getX();
        int positionVaisseauY = vaisseau.getY();

        if (vaisseau.getEstEnMouvement()) {
            switch (vaisseau.getDirection()) {
                case 0:
                    if (vaisseau.getY() <= 20) { //les if pour les extremites sont mis comme ca parce que delta grandit trop si on essaye d'aller en dehors de la fenetre de jeu, et ceci cause que lorsqu'on essaye de boger, il y a un delay
                        vaisseau.setY(20);
                        laser.setY(0);
                    } else {
                        positionVaisseauY -= .5 * delta;
                        vaisseau.setLocation(vaisseau.getX(), positionVaisseauY);
                        laser.setLocation(vaisseau.getX(), positionVaisseauY - 20);
                    }
                    break;
                case 1:
                    if (vaisseau.getX() <= 0) {
                        vaisseau.setX(0);
                        laser.setX(0);
                    } else {
                        positionVaisseauX -= .5 * delta;
                        vaisseau.setLocation(positionVaisseauX, vaisseau.getY());
                        laser.setLocation(positionVaisseauX, vaisseau.getY() - 20);
                    }
                    break;
                case 2:
                    if (vaisseau.getY() >= MainClass.HAUTEUR - vaisseau.getHeight()) {
                        vaisseau.setY(MainClass.HAUTEUR - vaisseau.getHeight());
                        laser.setY(MainClass.HAUTEUR - vaisseau.getHeight() - laser.getHeight());
                    } else {
                        positionVaisseauY += .5 * delta;
                        vaisseau.setLocation(vaisseau.getX(), positionVaisseauY);
                        laser.setLocation(vaisseau.getX(), positionVaisseauY - 20);
                    }
                    break;
                case 3:
                    if (vaisseau.getX() >= MainClass.LARGEUR - vaisseau.getWidth()) {
                        vaisseau.setX(MainClass.LARGEUR - vaisseau.getWidth());
                        laser.setX(MainClass.LARGEUR - vaisseau.getWidth());
                    } else {
                        positionVaisseauX += .5 * delta;
                        vaisseau.setLocation(positionVaisseauX, vaisseau.getY());
                        laser.setLocation(positionVaisseauX, vaisseau.getY() - 20);
                    }
                    break;
            }
        }
    } // ajustement de la position du vaisseau
    private void ajusterPositionEntitesBougeables() {
        for (Bougeable entiteBougeable : listeEntiteBougeable) {
            entiteBougeable.bouger();
        }
    } // appel de la methode bouger() pour tous les bougeables

    private void tracerToutesLesEntites(Graphics g) {
        for (Entite entite : listeEntite) {
            if (entite instanceof Asteroide) {
                if (entite.getImage().getHeight() != entite.getHeight()) {
                    ((Asteroide) entite).bouger();
                }
                g.drawImage(entite.getImage(), entite.getX(), entite.getY());
            } else {
                if (!(entite instanceof Vaisseau) || (!vaisseau.getFlashVaisseau())) {
                    g.drawImage(entite.getImage(), entite.getX(), entite.getY());
                }
            }
        }
        g.drawImage(barreVide.getImage(), barreVide.getX(), barreVide.getY());
        g.drawImage(barreDeVie.getImage(), barreDeVie.getX(), barreDeVie.getY());
    } // render de toutes les entites sauf ce qui doit etre specifiquement en arriere plan ou en avant plan
    private void tracerBackground(Graphics g) {
        g.drawImage(background.getImage(), background.getX(), -background.getY() - background.getHeight() * bgParcouru);
        g.drawImage(background.getImage(), background.getX(), -background.getY() - background.getHeight() * (bgParcouru + 1));
    } // render du background, on le fait au debut pour qu'il soit en arriere
    private void tracerEtatDuJeu(Graphics g) {
        TTF.drawString(20, barreVide.getY() + 0, "Statut Cargo: " + Integer.toString(cargo.getEtatCargo() * 100 / (128 * 128)) + "%", Color.white);
        TTF.drawString(20, barreVide.getY() + 100, "Score: " + Integer.toString(scoreTotal), Color.white);
        g.resetTransform();
        g.fillRect(barreMars.getX() + 36, barreMars.getY() + 40, remplisseurBarreMars * barreMars.getWidth(), barreMars.getHeight() - 10);
        g.setColor(new Color(0, 0, 255));
        g.drawImage(barreMars.getImage(), barreMars.getX(), barreMars.getY());
    } // render du HUD
    
    private void traiterClavier(int key) {
        switch (key) {
            case Input.KEY_W:
                vaisseau.setDirection(0);
                vaisseau.setEstEnMouvement(true);
                break;
            case Input.KEY_A:
                vaisseau.setDirection(1);
                vaisseau.setEstEnMouvement(true);
                break;
            case Input.KEY_S:
                vaisseau.setDirection(2);
                vaisseau.setEstEnMouvement(true);
                break;
            case Input.KEY_D:
                vaisseau.setDirection(3);
                vaisseau.setEstEnMouvement(true);
                break;
            case Input.KEY_E:
                cargo.lancerCargaison();
                break;
            case Input.KEY_SPACE:
                Canon canon = new Canon(vaisseau.getX() + LARGEUR_VAISSEAU / 2 - 2, vaisseau.getY(), HAUTEUR_CANON, LARGEUR_CANON, "images/canon.png", vaisseau.getY());
                faireSon("son/sonLaser.wav");
                listeEntiteBougeable.add(canon);
                listeEntite.add(canon);
                break;
        }
    } // traitement des touches du clavier
    private void traiterPlusieursTouches(int key) {
        //Pour assurer que le vaisseau ne s'arrete pas quand on lache un bouton autre que WASD
        //ou si on presse plusieurs boutons entre WASD et qu'on lache une
        if (Input.KEY_W == key && vaisseau.getDirection() == 0) {
            vaisseau.setEstEnMouvement(false);
        }
        if (Input.KEY_A == key && vaisseau.getDirection() == 1) {
            vaisseau.setEstEnMouvement(false);
        }
        if (Input.KEY_S == key && vaisseau.getDirection() == 2) {
            vaisseau.setEstEnMouvement(false);
        }
        if (Input.KEY_D == key && vaisseau.getDirection() == 3) {
            vaisseau.setEstEnMouvement(false);
        }
    } // ici, la methode nous permet de continuer a marcher en une direction meme si la methode keyReleased() est invoquee pour une touche differente a notre direction

    private boolean sontCollisionnables(Entite entite1, Entite entite2) {
        boolean sontCollisionnables = true;

        if (entite1 == entite2) {
            sontCollisionnables = false;
        }

        if (entite1.getClass() == entite2.getClass()) {
            sontCollisionnables = false;
        }

        if (!(entite1 instanceof Collisionable)) {
            sontCollisionnables = false;
        }

        if (!(entite2 instanceof Collisionable)) {
            sontCollisionnables = false;
        }

        if (entite1 instanceof Vaisseau && entite2 instanceof Canon) {
            sontCollisionnables = false;
        }

        if (entite2 instanceof Vaisseau && entite1 instanceof Canon) {
            sontCollisionnables = false;
        }

        return sontCollisionnables;
    } // methode qui analyse si 2 entites peuvent entrer en collision
    private boolean sontEnCollision(Entite entite1, Entite entite2) {
        return entite1.getRectangle().intersects(entite2.getRectangle());
    } // methode qui regarde si 2 entites sont en collision

    private int determinerTypeCollision(Entite entite1, Entite entite2) {
        int typeCollision = 0;

        if (entite1 instanceof Vaisseau && entite2 instanceof Asteroide) {
            typeCollision = 1;
        }

        if (entite2 instanceof Vaisseau && entite1 instanceof Asteroide) {
            typeCollision = 1;
        }

        if (entite1 instanceof Canon && entite2 instanceof Asteroide) {
            typeCollision = 2;
        }

        if (entite2 instanceof Canon && entite1 instanceof Asteroide) {
            typeCollision = 2;
        }

        if (entite1 instanceof Laser && entite2 instanceof Asteroide) {
            typeCollision = 3;
        }

        if (entite2 instanceof Laser && entite1 instanceof Asteroide) {
            typeCollision = 3;
        }

        return typeCollision;
    } // methode qui retourne une valeur int qui est associee a un type specifique de collision
    private void gererCollisions(ArrayList<Entite> listeTemp, ArrayList<Asteroide> listeTemp2) {
        for (int i = 0; i < listeEntite.size(); i++) {
            for (int j = i; j < listeEntite.size(); j++) {
                Entite entite1 = listeEntite.get(i);
                Entite entite2 = listeEntite.get(j);
                if (sontEnCollision(entite1, entite2)) {
                    if (sontCollisionnables(entite1, entite2)) {
                        int typeCollision = determinerTypeCollision(entite1, entite2);

                        switch (typeCollision) {
                            case 1:
                                gererCollisionAsteroideVaisseau(entite1, entite2, listeTemp);
                                break;
                            case 2:
                                gererCollisionAsteroideCanon(entite1, entite2, listeTemp, listeTemp2);
                                break;
                            case 3:
                                gererCollisionAsteroideLaser(entite1, entite2, listeTemp);
                                break;
                        }
                    }
                }
            }
        }
    } // appel d'autres methodes de facon organisee, pour gerer et traiter les collisions
    private void gererCollisionAsteroideVaisseau(Entite entite1, Entite entite2, ArrayList<Entite> listeTemp) {
        if (entite1 instanceof Asteroide) {
            absorberAsteroide(entite1, listeTemp);
        }
        if (entite2 instanceof Asteroide) {
            absorberAsteroide(entite2, listeTemp);
        }
    }// gerer la collision asteroide et vaisseau
    private void gererCollisionAsteroideCanon(Entite entite1, Entite entite2, ArrayList<Entite> listeTemp, ArrayList<Asteroide> listeTemp2) {
        if (entite1 instanceof Canon) {
            listeTemp.add(entite1);
        }
        if (entite2 instanceof Canon) {
            listeTemp.add(entite2);
        }
        if (entite1 instanceof Asteroide) {
            fracturerAsteroide(entite1, listeTemp, listeTemp2);
        }
        if (entite2 instanceof Asteroide) {
            fracturerAsteroide(entite2, listeTemp, listeTemp2);
        }
    }// gerer la collision asteroide et canon
    private void gererCollisionAsteroideLaser(Entite entite1, Entite entite2, ArrayList<Entite> listeTemp) {
        if (entite1 instanceof Asteroide) {
            absorberAsteroide(entite1, listeTemp);
        }
        if (entite2 instanceof Asteroide) {
            absorberAsteroide(entite2, listeTemp);
        }
    } // gerer la collision asteroide et laser

    private void creerNouvelleSerieAsteroide() throws SlickException {
        if (tempsPasseApresUneSerie > INTERVALLE_ENTRE_SERIE_ASTEROIDES) {
            initialiserAsteroides();
            tempsPasseApresUneSerie = 0;
        } else {
            tempsPasseApresUneSerie++;
        }

    } // creation de nouveaux asteroides
    private void fracturerAsteroide(Entite entite, ArrayList<Entite> listeTemp, ArrayList<Asteroide> listeTemp2) {
        if (entite.getHeight() > TAILLE_MINIMALE_ASTEROIDE) {
            Asteroide asteroideDroite = new Asteroide(entite.getX() + entite.getWidth() / 2, entite.getY() + 5, entite.getWidth() / 2, entite.getHeight() / 2, spriteSheet);
            Asteroide asteroideGauche = new Asteroide(entite.getX() - entite.getWidth() / 2, entite.getY() - 5, entite.getWidth() / 2, entite.getHeight() / 2, spriteSheet);

            listeTemp.add(entite);
            listeTemp2.add(asteroideDroite);
            listeTemp2.add(asteroideGauche);
        }
    }// methode qui brise un asteroide en 2 asteroides plus petits
    private void absorberAsteroide(Entite entite, ArrayList<Entite> listeTemp) {
        if (entite.getHeight() <= vaisseau.getHeight() / 2) {
            listeTemp.add(entite);
            if (cargo.getEtatCargo() < vaisseau.getHeight() * vaisseau.getHeight()) {
                cargo.setEtatCargo(cargo.getEtatCargo() + (entite.getWidth() * entite.getHeight())/2);
            }
            if (cargo.getEtatCargo() > vaisseau.getHeight() * vaisseau.getHeight()) {
                cargo.setEtatCargo(vaisseau.getHeight() * vaisseau.getHeight());
            }
        } else {
            enleverVie();
        }
    } // methode qui analyse si un asteroide doit etre ajoute au score ou si on doit enlever une vie au joueur
    private void supprimerAsteroidesHorsEcran(ArrayList<Entite> listeTemp) {
        for (Entite entite : listeEntite) {
            if (entite instanceof Asteroide) {
                if (entite.getX() < 0 - entite.getWidth() || entite.getX() > MainClass.LARGEUR || entite.getY() > MainClass.HAUTEUR) {
                    listeTemp.add(entite);
                }
            }
        }
    } // methode qui permet d'enlever des asteroides qui sont hors d'ecran
    private void faireSon(String nomSon) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(nomSon).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Erreur pour le son");
        }
    }//methode pour jouer un son
    private void verifierFinDuJeu(GameContainer container, StateBasedGame sbg) {
        if (vaisseau.getNbCollisions() == 3) {
            faireSon("son/sonPerdant.wav");
            Random x = new Random();
            String[] buttons = {"Oui, je me mets a la mercie d'enfants qui ont le quart de mon age", "Aint no way"};

            int rep = JOptionPane.showOptionDialog(null, "Vous etes un disappointment: Vous avez juste eu " + scoreTotal + " points. Je connais des enfants de " + (x.nextInt(8) + 1) + " ans qui jouent mieux que ca. Est-ce que vous allez accepter la defaite?", "Asteroide", JOptionPane.INFORMATION_MESSAGE, 0, null, buttons, buttons[0]);
            if (rep == 0) {
                System.exit(0);
            } else {
                try {
                    listeEntite.clear();
                    listeEntiteBougeable.clear();
                    init(container, sbg);
                    sbg.enterState(MainScreenGameState.ID);
                } catch (SlickException e) {
                    e.printStackTrace();
                }
            }
        }
    }// methode de finalisement de jeu
    private void enleverVie() {
        if (!vaisseau.isImmunitaire()) {
            vaisseau.setNbCollisions(vaisseau.getNbCollisions() + 1);
            barreDeVie.setImage(barreDeVie.getImage().getScaledCopy((LARGEUR_BARRE_DE_VIE * ((100 - vaisseau.getNbCollisions() * 33)) / 100), 25));
            vaisseau.setImmunitaire(true);
        }
    } // methode pour enlever une vie pour le joueur
    private void disparaitreCanon(ArrayList<Entite> listeTemp) {
        for (Entite entite : listeEntite) {
            if (entite instanceof Canon) {
                Canon canonVariable = (Canon) entite;
                if (canonVariable.getY() < canonVariable.getPositionInitialY() - MainClass.HAUTEUR / 2) {
                    listeTemp.add(canonVariable);
                }
            }
        }
    }   // methode pour faire disparaitre un canon
}
