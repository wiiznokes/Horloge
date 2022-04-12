package fr.wiiznokes.horloge11.utils.interact;


import static fr.wiiznokes.horloge11.app.MainActivity.items;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.fragments.app.AddFragment;
import fr.wiiznokes.horloge11.fragments.app.MainFragment;
import fr.wiiznokes.horloge11.utils.affichage.Affichage;
import fr.wiiznokes.horloge11.utils.alert.AlertHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.StorageUtils;
import fr.wiiznokes.horloge11.utils.storage.Trie;

public class InteractHelper {

    private final TextView activeAlarmTextView;
    private final TextView timeLeftTextView;
    private final MainActivity mainActivity;







    public InteractHelper(MainActivity mainActivity, TextView activeAlarmTextView, TextView timeLeftTextView){
        this.activeAlarmTextView = activeAlarmTextView;
        this.timeLeftTextView = timeLeftTextView;
        this.mainActivity = mainActivity;
    }

    public void switchHelper(Alarm currentAlarm){

        //l'alarm devient inactive
        if(currentAlarm.active){
            currentAlarm.active = false;

            //si l'alarm etait la première, affichage temps restant modifié
            if (currentAlarm.id == MainActivity.ListActif.get(0)){
                if(MainActivity.ListActif.size() >= 2){
                    timeLeftTextView.setText(Affichage.tempsRestant(MainActivity.MapIdAlarm.get(MainActivity.ListActif.get(1))));
                }
                else{
                    timeLeftTextView.setText(R.string.tempsRestant0alarm);
                }
            }
            MainActivity.ListActif.remove(currentAlarm.id);
            Trie.ListInactifChange(currentAlarm.id);


            //remove Alarm de AlarmManager
            AlertHelper.remove(currentAlarm, mainActivity);
        }
        //l'alarm devient active
        else {
            currentAlarm.active = true;

            MainActivity.ListInactif.remove(currentAlarm.id);
            Trie.ListActifChange(currentAlarm.id);

            //si l'alarm devient la première, affichage temps restant modifié
            if (currentAlarm.id == MainActivity.ListActif.get(0)){
                timeLeftTextView.setText(Affichage.tempsRestant(currentAlarm));
            }
            //ajout de l'alarm au AlarmManager
            AlertHelper.add(currentAlarm, mainActivity);
        }

        items.remove(MainActivity.ListSortId.indexOf(currentAlarm.id));

        MainActivity.MapIdAlarm.put(currentAlarm.id, currentAlarm);
        Trie.ListSortId();

        activeAlarmTextView.setText(Affichage.NombreAlarmsActives(MainActivity.ListActif.size()));

        //ecriture
        StorageUtils.writeObject(mainActivity, MainActivity.MapIdAlarm, StorageUtils.alarmsFile);

        //maj affichage
        items.add(MainActivity.ListSortId.indexOf(currentAlarm.id), currentAlarm);
        MainFragment.adapter.notifyDataSetChanged();
    }




    public void effacer(Alarm currentAlarm){

        long id = currentAlarm.id;

        //si alarm est la premiere
        if(MainActivity.ListActif.size() > 0){
            if(MainActivity.ListActif.get(0) == id){


                if(MainActivity.ListActif.size() > 1){
                    timeLeftTextView.setText(Affichage.tempsRestant(items.get(1)));
                }
                else {
                    timeLeftTextView.setText(R.string.tempsRestant0alarm);
                }
            }
        }

        MainActivity.MapIdAlarm.remove(id);
        MainActivity.MapIdDate.remove(id);

        if(MainActivity.ListActif.contains(id)){
            MainActivity.ListActif.remove((Object) id);
        }
        else{
            MainActivity.ListInactif.remove((Object) id);
        }

        items.remove(MainActivity.ListSortId.indexOf(id));
        Trie.ListSortId();

        //maj nb alarm active
        activeAlarmTextView.setText(Affichage.NombreAlarmsActives(MainActivity.ListActif.size()));

        //ecriture
        StorageUtils.writeObject(mainActivity, MainActivity.MapIdAlarm, StorageUtils.alarmsFile);

    }

    public void modifier(Alarm currentAlarm, View v){

        MainActivity activity = (MainActivity) v.getContext();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, AddFragment.newInstance(true, currentAlarm))
                .commit();
    }


}
