package fr.wiiznokes.horloge11.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;

public class Affichage extends AppCompatActivity {


    public List<List> afficheAlarmesInit(List<Alarm> Array1, List<Integer> ListSortId, Map<Integer, Integer> MapIdPos){


        //creation list pour les view des switchs
        List<Switch> switchsView = new ArrayList<>();




        for (int id : ListSortId){
            Alarm Alarme = Array1.get(MapIdPos.get(id));

            //recupération de linear layout
            LinearLayout linearLayout= findViewById(R.id.linearLayout1);

            //création du constraint Layout
            ConstraintLayout constraintLayout = new ConstraintLayout(this);
            constraintLayout.setId(View.generateViewId());

            //objet set pour ajouter des contraintes
            ConstraintSet set = new ConstraintSet();

            //création heure alarme
            TextView textView = new TextView(this);
            textView.setText(Alarme.getHoursText());
            textView.setId(View.generateViewId());

            //création nom alarme
            TextView textView2 = new TextView(this);
            textView2.setText(Alarme.getNameAlarm());
            textView2.setId(View.generateViewId());

            //création switch
            Switch switch2 = new Switch(this);
            switch2.setText("");
            switch2.setChecked(Alarme.isActive());
            switch2.setId(id);

            //ajout de switch a la liste des views
            switchsView.add(switch2);


            //création jour alarme
            TextView textView3 = new TextView(this);
            if (Alarme.isWeek()){
                textView3.setText("Tous les jours");
            }
            else{
                String joursActif = "";
                if(Alarme.isMonday()){
                    joursActif = joursActif + "lun ";
                }
                if (Alarme.isTuesday()){
                    joursActif = joursActif + "mar ";
                }
                if (Alarme.isWednesday()){
                    joursActif = joursActif + "mer ";
                }
                if (Alarme.isThursday()){
                    joursActif = joursActif + "jeu ";
                }
                if (Alarme.isFriday()){
                    joursActif = joursActif + "ven ";
                }
                if (Alarme.isSaturday()){
                    joursActif = joursActif + "sam ";
                }
                if (Alarme.isSunday()){
                    joursActif = joursActif + "dim ";
                }
                textView3.setText(joursActif);
            }

            textView3.setId(View.generateViewId());




            //ajout des view au constraint layout
            constraintLayout.addView(textView);
            constraintLayout.addView(textView2);
            constraintLayout.addView(switch2);
            constraintLayout.addView(textView3);

            //ajout constraint layout au linear layout
            linearLayout.addView(constraintLayout);

            //lien entre set et constraint layout
            set.clone(constraintLayout);

            //constraint pour l'heure de l'alarme
            set.connect(textView.getId(), ConstraintSet.LEFT, ((View) textView.getParent()).getId(), ConstraintSet.LEFT);

            //constraint pour le nom de l'alarme
            set.connect(textView2.getId(), ConstraintSet.LEFT, textView.getId(), ConstraintSet.RIGHT);
            set.connect(textView2.getId(), ConstraintSet.RIGHT, switch2.getId(), ConstraintSet.LEFT);
            set.connect(textView2.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.TOP);

            //consraint pour le switch
            set.connect(switch2.getId(), ConstraintSet.RIGHT, ((View) switch2.getParent()).getId(), ConstraintSet.RIGHT);
            set.connect(switch2.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.TOP);
            set.connect(switch2.getId(), ConstraintSet.BOTTOM, textView3.getId(), ConstraintSet.BOTTOM);

            //constraint pour les jours de sonnerie
            set.connect(textView3.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.BOTTOM);
            set.connect(textView3.getId(), ConstraintSet.RIGHT, switch2.getId(), ConstraintSet.LEFT);
            set.connect(textView3.getId(), ConstraintSet.LEFT, textView.getId(), ConstraintSet.LEFT);

            //application des constraints
            set.applyTo(constraintLayout);

        }


        List<List> ListViews = new ArrayList<>();
        ListViews.add(switchsView);


        return ListViews;

    }


    public String NombreAlarmsActives (int numberAlarmsActives){
        String phrase;
        if (numberAlarmsActives == 0 || numberAlarmsActives == 1){
            phrase = "Nombre d'alarme active : " + Integer.toString(numberAlarmsActives);
        }
        else{
            phrase = "Nombre d'alarmes actives : " + Integer.toString(numberAlarmsActives);
        }
        return phrase;
    }

    public String tempsRestant(Alarm Alarme){
        String tempsRestant;
        Calendar dateSonnerie = new Trie().dateProchaineSonnerie(Alarme);
        Calendar date = Calendar.getInstance();
        long diffMiliSec = dateSonnerie.getTimeInMillis() - date.getTimeInMillis();
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
        if(jour > 0){
            if(jour == 1) {
                tempsRestant = "1 jour et ";
                if(heure == 1 || heure == 0){
                    tempsRestant = tempsRestant + Integer.toString(heure) + "heure";
                }
                else{
                    tempsRestant = tempsRestant + Integer.toString(heure) + "heures";
                }
                return tempsRestant;
            }
            else{
                tempsRestant = Integer.toString(jour) + " jours";
                return tempsRestant;
            }
        }
        else {
            if(heure > 0){
                if(heure == 1){
                    tempsRestant = "1 heure et ";
                    if(minute == 1 || minute == 0){
                        tempsRestant = tempsRestant + Integer.toString(minute) + "minute";
                    }
                    else{
                        tempsRestant = tempsRestant + Integer.toString(minute) + "minutes";
                    }
                    return tempsRestant;
                }
                else{
                    tempsRestant = Integer.toString(heure) + " heures et ";
                    if(minute == 1 || minute == 0){
                        tempsRestant = tempsRestant + Integer.toString(minute) + " minute";
                    }
                    else{
                        tempsRestant = tempsRestant + Integer.toString(minute) + " minutes";
                    }
                    return tempsRestant;

                }
            }
            else{
                if(minute == 0){
                    tempsRestant = "Moins d'une minute";
                    return tempsRestant;
                }
                if(minute == 1){
                    tempsRestant = "1 minute";
                    return tempsRestant;
                }
                else{
                    tempsRestant = Integer.toString(minute) + " minutes";
                    return tempsRestant;
                }
            }
        }

    }

}
