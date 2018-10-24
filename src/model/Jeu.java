package model;

import model.plateau.Plateau;

public class Jeu {

    private static Jeu instance;
    private boolean fini = false;
    private Hero hero = new Hero(this);
    private Plateau plateau = new Plateau();

    private Jeu() {};

    public static Jeu getInstance() {
        if(instance == null)
            instance = new Jeu();
        return instance;
    }

    /**
     * @param cmd Input de l'utilisateur
     */
    public void evolve(String cmd){
        int x = hero.getPosX();
        int y = hero.getPosY();
        switch (cmd){
            case "h" :
                y--;
                break;
            case "b" :
                y++;
                break;
            case "g" :
                x--;
                break;
            case "d" :
                x++;
                break;
            case "f" :
                this.fini = true;
                break;
        }
        if(plateau.estLibre(x,y)) {
            hero.deplacer(x, y);
            plateau.appliquerEffetCase(x,y);
        }

    }

    public boolean isFini() {
        return fini;
    }

    public void setFini(boolean fini) {
        this.fini = fini;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    /**
     * @return true si partie terminée, false sinon
     */
    public boolean isFinished(){
        return this.fini;
    }

    /**
     * @return Le hero lié au jeu
     */
    public Hero getHero(){
        return this.hero;
    }

    public void estMort() {
        fini = true;
    }

    public void appliquerDegats(int nbDegats) {
        hero.subirDegat(nbDegats);
    }
}
