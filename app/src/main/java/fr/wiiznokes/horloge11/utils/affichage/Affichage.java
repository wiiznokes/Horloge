package fr.wiiznokes.horloge11.utils.affichage;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.Trie;

public class Affichage extends AppCompatActivity {



    public static String NombreAlarmsActives (int numberAlarmsActives){
        String phrase;
        if (numberAlarmsActives == 0 || numberAlarmsActives == 1){
            phrase = "Alarme active : " + numberAlarmsActives;
        }
        else{
            phrase = "Alarmes actives : " + numberAlarmsActives;
        }
        return phrase;
    }

    public static String tempsRestant(Alarm Alarme){

        Calendar dateSonnerie = Trie.dateProchaineSonnerie(Alarme);
        long heureSys = System.currentTimeMillis();

        long diffMiliSec = dateSonnerie.getTimeInMillis() - heureSys;
        int jour = 0;
        int heure = 0;
        int minute = 0;
        long jourEnMili = 1000 * 60 * 60 * 24;
        long heureEnMili = 1000 * 60 * 60;
        long minuteEnMili = 1000 * 60;
        while(diffMiliSec - jourEnMili > 0){
            diffMiliSec = diffMiliSec - jourEnMili;
            jour = jour +1;
        }
        while(diffMiliSec - heureEnMili > 0){
            diffMiliSec = diffMiliSec - heureEnMili;
            heure = heure +1;
        }
        while(diffMiliSec - minuteEnMili > 0){
            diffMiliSec = diffMiliSec - minuteEnMili;
            minute = minute +1;
        }

        String tempsRestant;

        if(jour > 0){
            if(jour == 1) {
                if(heure == 1 || heure == 0){
                    tempsRestant = "Temps restant : " + jour + " jours et " + heure + " heures";
                }
                else{
                    tempsRestant = "Temps restant : " + jour + " jours et " + heure + "heures";
                }
            }
            else{
                tempsRestant = "Temps restant : " + jour + " jours";
            }
        }
        //jour == 0
        else {
            if(heure > 0){
                if(heure == 1){
                    if(minute == 1 || minute == 0){
                        tempsRestant = "Temps restant : " + heure + " heure et " + minute + "minute";
                    }
                    else{
                        tempsRestant = "Temps restant : " + heure + " heure et " + minute + "minutes";
                    }
                }
                else{
                    if(minute == 1 || minute == 0){
                        tempsRestant = "Temps restant : " + heure + " heures et " + minute + "minute";
                    }
                    else{
                        tempsRestant = "Temps restant : " + heure + " heures et " + minute + "minutes";
                    }

                }
            }
            //heure == 0
            else{
                if(minute == 0){
                    tempsRestant = "Temps restant : Moins d'une minute";
                }
                else if(minute == 1){
                    tempsRestant = "Temps restant : " + minute + " minute";
                }
                else{
                    tempsRestant = "Temps restant : " + minute + " minutes";
                }
            }
        }
        return tempsRestant;

    }


}
