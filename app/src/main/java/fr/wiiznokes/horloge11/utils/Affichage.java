package fr.wiiznokes.horloge11.utils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;

public class Affichage extends MainActivity {


    public List<List> afficheAlarmesInit(List<Alarm> Array1, List<Integer> ListSortId, Map<Integer, Integer> MapIdPos, Context context, LinearLayout linearLayout){

        //creation list pour les view des switchs
        List<SwitchMaterial> switchsViews = new ArrayList<>();
        List<ConstraintLayout> constraintLayoutViews = new ArrayList<>();

        for (int id : ListSortId){
            Alarm Alarme = Array1.get(MapIdPos.get(id));
            ConstraintLayout constraintLayout = newConstaintLayout(Alarme, context);

            //ajout de switch a la liste des views
            switchsViews.add(((SwitchMaterial) constraintLayout.getChildAt(1)));
            //ajout du constraint layout a la liste des views
            constraintLayoutViews.add(constraintLayout);

            //ajout constraint layout au linear layout
            linearLayout.addView(constraintLayout);
        }


        List<List> ListViews = new ArrayList<>();
        ListViews.add(switchsViews);
        ListViews.add(constraintLayoutViews);

        return ListViews;

    }


    public ConstraintLayout newConstaintLayout(Alarm Alarme, Context context){

        //création du constraint Layout
        ConstraintLayout constraintLayout = new ConstraintLayout(context);
        constraintLayout.setId(Alarme.getId()+10000);

        //objet set pour ajouter des contraintes
        ConstraintSet set = new ConstraintSet();

        //création heure alarme
        TextView textView = new TextView(context);
        textView.setText(Alarme.getHoursText());
        textView.setId(View.generateViewId());

        //création nom alarme
        TextView textView2 = new TextView(context);
        textView2.setText(Alarme.getNameAlarm());
        textView2.setId(View.generateViewId());

        //création switch
        SwitchMaterial switch2 = new SwitchMaterial(context);
        switch2.setText("");
        switch2.setChecked(Alarme.isActive());
        switch2.setId(Alarme.getId());


        //création jour alarme
        TextView textView3 = new TextView(context);
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
        constraintLayout.addView(switch2);
        constraintLayout.addView(textView2);
        constraintLayout.addView(textView3);

        //lien entre set et constraint layout
        set.clone(constraintLayout);

        //constraint pour l'heure de l'alarme
        set.connect(textView.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT);
        set.connect(textView.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);

        //consraint pour le switch
        set.connect(switch2.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT);
        set.connect(switch2.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);
        set.connect(switch2.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);

        //constraint pour le nom de l'alarme
        set.connect(textView2.getId(), ConstraintSet.LEFT, textView.getId(), ConstraintSet.RIGHT);
        set.connect(textView2.getId(), ConstraintSet.RIGHT, switch2.getId(), ConstraintSet.LEFT);
        set.connect(textView2.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP);

        //constraint pour les jours de sonnerie
        set.connect(textView3.getId(), ConstraintSet.LEFT, textView.getId(), ConstraintSet.RIGHT);
        set.connect(textView3.getId(), ConstraintSet.RIGHT, switch2.getId(), ConstraintSet.LEFT);
        set.connect(textView3.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM);

        //application des constraints
        constraintLayout.setConstraintSet(set);

        return constraintLayout;

    }



    public String NombreAlarmsActives (int numberAlarmsActives){
        String phrase;
        if (numberAlarmsActives == 0 || numberAlarmsActives == 1){
            phrase = "Alarme active : " + numberAlarmsActives;
        }
        else{
            phrase = "Alarmes actives : " + numberAlarmsActives;
        }
        return phrase;
    }

    public String tempsRestant(Alarm Alarme){

        Calendar dateSonnerie = new Trie().dateProchaineSonnerie(Alarme);
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
                    tempsRestant = "Temps restant : " + jour + " jour et " + heure + "heure";
                }
                else{
                    tempsRestant = "Temps restant : " + jour + " jour et " + heure + "heures";
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
