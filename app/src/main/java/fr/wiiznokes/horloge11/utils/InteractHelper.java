package fr.wiiznokes.horloge11.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;

public class InteractHelper {

    private final TextView textViewTempsRestant;
    private final TextView textViewAlarmeActive;





    public InteractHelper(TextView textViewTempsRestant, TextView textViewAlarmeActive){
        this.textViewTempsRestant = textViewTempsRestant;
        this.textViewAlarmeActive = textViewAlarmeActive;

    }


    public void switchHelper(SwitchMaterial switchView, List<Alarm> Array1, Map<Integer, Integer> MapIdPos, Map<Integer, Calendar> MapIdDate,
                             List<Integer> ListActif, List<Integer> ListInactif, List<Integer> ListSortId){



    }


    public void effacer(int id, MainActivity mainActivity){

        int index = mainActivity.ListSortId.indexOf(id);

        System.out.println("helaaaaaaaaaaaaaaaaa");
        for(Alarm alarm: mainActivity.ListSortAlarm){
            System.out.println(alarm.getNameAlarm());
        }

        //si alarm et la premiere
        if(mainActivity.ListActif.size() > 0){
            if(mainActivity.ListActif.get(0) == id){
                try {
                    mainActivity.ListActif.get(1);
                    textViewTempsRestant.setText(new Affichage().tempsRestant(mainActivity.ListSortAlarm.get(1)));
                }
                catch (IndexOutOfBoundsException indexOutOfBoundsException){
                    textViewTempsRestant.setText(R.string.tempsRestant0alarm);
                }

            }
        }
        mainActivity.ListSortAlarm.remove(index);
        mainActivity.MapIdAlarm.remove(id);
        mainActivity.MapIdDate.remove(id);

        if(mainActivity.ListActif.contains(id)){
            mainActivity.ListActif.remove((Object) id);
        }
        else{
            mainActivity.ListInactif.remove((Object) id);
        }
        mainActivity.ListSortId = new Trie().ListSortId(mainActivity.ListActif, mainActivity.ListInactif);


        //maj nb alarm active
        textViewAlarmeActive.setText(new Affichage().NombreAlarmsActives(mainActivity.ListActif.size()));

        //ecriture
        new StorageUtils().write(mainActivity.MapIdAlarm, mainActivity);

    }


}
