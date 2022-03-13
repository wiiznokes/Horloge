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


    public void effacer(int id, Context context, List<Alarm> ListSortAlarm, ListView listView, View convertView,
                        Map<Integer, Alarm> MapIdAlarm, Map<Integer, Calendar> MapIdDate,
                        List<Integer> ListActif, List<Integer> ListInactif, List<Integer> ListSortId){

        int index = ListSortId.indexOf(id);

        //si alarm et la premiere
        if(ListActif.size() > 0){
            if(ListActif.get(0) == id){
                try {
                    ListActif.get(1);
                    textViewTempsRestant.setText(new Affichage().tempsRestant(ListSortAlarm.get(1)));
                }
                catch (IndexOutOfBoundsException indexOutOfBoundsException){
                    textViewTempsRestant.setText(R.string.tempsRestant0alarm);
                }

            }
        }
        ListSortAlarm.remove(index);
        listView.removeView(convertView);
        MapIdAlarm.remove(id);
        MapIdDate.remove(id);

        if(ListActif.contains(id)){
            ListActif.remove((Object) id);
        }
        else{
            ListInactif.remove((Object) id);
        }
        ListSortId = new Trie().ListSortId(ListActif, ListInactif);


        //maj nb alarm active
        textViewAlarmeActive.setText(new Affichage().NombreAlarmsActives(ListActif.size()));

        //ecriture
        new StorageUtils().write(MapIdAlarm, context);

    }


}
