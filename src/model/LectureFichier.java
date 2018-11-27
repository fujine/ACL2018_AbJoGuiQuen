package model;

import model.entites.Chevalier;
import model.entites.Fantome;
import model.entites.Monstre;
import model.factory.CaseFactory;
import model.factory.ObjetFactory;
import model.plateau.*;
import model.plateau.objet.ObjetPiege;
import model.plateau.objet.ObjetTresor;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LectureFichier {



    public static Plateau lireFichier(URI uri) {


        try {
            boolean first = true;
            File f = new File(uri);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            Scanner sc = new Scanner(br);
            String line;
            String splitVirgule[];
            String[] splitTmp; // permet de faire les splits temporaires pour les teleporteurs etc
            int i=0;
            line = sc.nextLine();
            splitVirgule = line.split(";");
            HashMap<Point,String> monstres = new HashMap<>();

            ICase plat[][] = new ICase[splitVirgule.length][splitVirgule.length];

            while (sc.hasNextLine()){
                if (!first) {
                    line = sc.nextLine();
                    splitVirgule = line.split(";");
                }else{
                    first = false;
                }
                for(int j=0; j<splitVirgule.length ; j++){
                    if (splitVirgule[j].length() <= 1){
                        switch (splitVirgule[j]){
                            case "M": plat[j][i] = CaseFactory.creerCase(ECase.MUR);
                                break;
                            case "O": plat[j][i] = CaseFactory.creerCase(ECase.SOL);
                                break;
                            case "P":  plat[j][i] = CaseFactory.creerCase(ECase.PIEGE);
                                break;
                            case "T": plat[j][i] = CaseFactory.creerCase(ECase.TRESOR);
                                break;
                            case "V":  plat[j][i] = CaseFactory.creerCase(ECase.VIDE);
                                break;
                            case "C":
                                monstres.put(new Point(j*Jeu.ECHELLE, i*Jeu.ECHELLE),"C");
                                plat[j][i] = CaseFactory.creerCase(ECase.SOL);
                                break;
                            case "F":
                                monstres.put(new Point(j*Jeu.ECHELLE,i*Jeu.ECHELLE),"F");
                                plat[j][i] = CaseFactory.creerCase(ECase.SOL);
                                break;
                            default:
                                plat[j][i] = CaseFactory.creerCase(ECase.SOL);
                                break;
                        }
                    }else{
                        String[] splitRecupInfo = splitVirgule[j].split(",");
                        int x,y,p;
                        switch (splitRecupInfo[0]){
                            case "T" :
                                x = Integer.parseInt(splitRecupInfo[1]);
                                y = Integer.parseInt(splitRecupInfo[2]);
                                if (x < splitVirgule.length* Jeu.ECHELLE && y < splitVirgule.length* Jeu.ECHELLE) {
                                    plat[j][i] = CaseFactory.creerCase(ECase.TELEPORTEUR, new Point(x, y));
                                }else {
                                    plat[j][i] = CaseFactory.creerCase(ECase.SOL);
                                }
                                break;
                            case "E":
                                x = Integer.parseInt(splitRecupInfo[1]);
                                y = Integer.parseInt(splitRecupInfo[2]);
                                p = Integer.parseInt(splitRecupInfo[3]);
                                if (x < splitVirgule.length * Jeu.ECHELLE && y < splitVirgule.length * Jeu.ECHELLE) {
                                    plat[j][i] = CaseFactory.creerCase(ECase.ESCALIER,new Point(x,y),p);
                                } else
                                    plat[j][i] = CaseFactory.creerCase(ECase.SOL);
                                break;
                        }
                    }
                }
                i++;
            }
            Plateau plateau = new Plateau(plat);
            for (Point p : monstres.keySet()) {
                switch (monstres.get(p)) {
                    case "C" :
                        plateau.addMonstre(new Chevalier(p,plateau));
                        break;
                    case "F" :
                        plateau.addMonstre(new Fantome(p,plateau));
                }
            }
            sc.close();
            return plateau;

        } catch (FileNotFoundException e) {
            return new Plateau();
        }
    }

}
