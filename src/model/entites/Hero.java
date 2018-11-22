package model.entites;

import model.Jeu;
import model.plateau.Plateau;

import java.awt.*;

public class Hero extends Entites {

    private boolean attaque;

    /**
     * Constructeur à partir d'une position et d'un plateau avec définition de la vie du héro
     * @param coord Coordonnée du héro sur le plateau
     * @param plateau Plateau au qu'elle appartient le héro
     */
    public Hero(Point coord, Plateau plateau) {
        super(coord, plateau);
        dir = Direction.BAS;
        coord = new Point(1,1);
        vie = 9;
        vieMax = 10;
        degat = 1;
        vitesse = 13;
    }

    public boolean getAttaque() {
        return attaque;
    }

    public void setAttaque(boolean attaque) {
        this.attaque = attaque;
    }

    public void attaquer() {
        int x = coord.x;
        int y = coord.y;
        switch (dir) {
            case BAS:
                y++;
                break;
            case HAUT:
                y--;
                break;
            case GAUCHE:
                x--;
                break;
            case DROITE:
                x++;
                break;
        }
        Point coordBG = new Point(x,y+Jeu.TAILLE -1);
        Point coordBD = new Point(x + Jeu.TAILLE -1,y+Jeu.TAILLE -1);
        if (plateau.estLibre(coordBG) && plateau.estLibre(coordBD)){
            attaque = true;
            Entites e = Jeu.getInstance().collisionEntites(this,new Point(x,y));
            if(e != null)
                e.subirDegat(degat);
        } else
            attaque = false;
    }

    /**
     * TODO
     * @return degat du hero
     */
    public int getDegat() { return degat; }


    /**
     * Vérifie les coordonnées avant de deplacer le hero au coordonnées données
     * @param coord futur position du héro
     */
    public void deplacer(Point coord,Direction direction) {
        Point coordBG = new Point(coord.x,coord.y+Jeu.TAILLE-1);
        Point coordBD = new Point(coord.x + Jeu.TAILLE-1,coord.y+Jeu.TAILLE-1);
        Point coordHG = new Point(coord.x,coord.y+Jeu.TAILLE-1- Jeu.ECHELLE/4);
        Point coordHD = new Point(coord.x + Jeu.TAILLE-1,coord.y+Jeu.TAILLE-1- Jeu.ECHELLE/4);
        dir = direction;
        Jeu mod = Jeu.getInstance();
        if (coordBG.x >= 0 && coordBG.y>= 0 && coordBD.x < plateau.getLargeur() && coordBD.y < plateau.getHauteur() ){
            //Test de colision et de zone libre pour le deplacement du héro
            if(plateau.estLibre(coordBG) && plateau.estLibre(coordBD) && plateau.estLibre(coordHD) && plateau.estLibre(coordHG) && mod.collisionEntites(this,coord) == null) {
                this.coord = coord;
                getPlateau().appliquerEffetCase(coordBD);
                getPlateau().appliquerEffetCase(coordBG);
            } else {
                switch (direction) {
                    case DROITE:
                        if((coordBD.x +1 ) % Jeu.ECHELLE != 0 ) {
                            this.coord.x = coord.x - (coordBD.x +1 ) % Jeu.ECHELLE;
                        }
                        break;
                    case GAUCHE:
                        if(coordBG.x % Jeu.ECHELLE != 0 ) {
                            this.coord.x = this.coord.x / Jeu.ECHELLE * Jeu.ECHELLE;
                        }
                        break;
                    case BAS:
                        if((coordBD.y +1 ) % Jeu.ECHELLE != 0 ) {
                            this.coord.y = (this.coord.y + Jeu.ECHELLE)/Jeu.ECHELLE*Jeu.ECHELLE - Jeu.TAILLE ;
                        }
                        break;
                    case HAUT:
                        if((coordBD.y ) % Jeu.ECHELLE != 0 ) {
                            this.coord.y = this.coord.y / Jeu.ECHELLE * Jeu.ECHELLE + (Jeu.ECHELLE - Jeu.TAILLE + Jeu.ECHELLE/4 +1) ;
                        }
                        break;
                }
            }
        }

    }

    /**
     * Diminue la vie du héro en fonction du nombre de dégats subit
     * @param nbDegats
     */
    public void subirDegat(int nbDegats) {
        this.vie = vie - nbDegats;
        System.out.println("aie");
        if (vie <= 0 ){
            Jeu.getInstance().estMort();
            System.out.println("je suis mort");
        }
    }

    /**
     * Verifie que le héro soit encore en vie
     * @return true si le héro a plus de 0 de vie, false sinon
     */
    public boolean estVivant() {
        return this.vie <= 0;
    }

    public String getType(){return "h";}


}

