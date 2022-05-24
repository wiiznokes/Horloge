package fr.wiiznokes.horloge11.utils.interact;


import static fr.wiiznokes.horloge11.app.MainActivity.ListActif;
import static fr.wiiznokes.horloge11.app.MainActivity.ListInactif;
import static fr.wiiznokes.horloge11.app.MainActivity.ListSortId;
import static fr.wiiznokes.horloge11.app.MainActivity.MapIdAlarm;
import static fr.wiiznokes.horloge11.app.MainActivity.MapIdDate;
import static fr.wiiznokes.horloge11.app.MainActivity.items;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

            ListActif.remove(currentAlarm.id);
            Trie.listInactifChange(currentAlarm.id);

            //remove Alarm de AlarmManager
            AlertHelper.remove(currentAlarm, mainActivity);
        }
        //l'alarm devient active
        else {
            currentAlarm.active = true;

            ListInactif.remove(currentAlarm.id);
            Trie.listActifChange(currentAlarm.id);


            //ajout de l'alarm au AlarmManager
            AlertHelper.add(currentAlarm, mainActivity, MapIdDate.get(currentAlarm.id).getTimeInMillis());
        }


        MapIdAlarm.put(currentAlarm.id, currentAlarm);
        Trie.listSortId();

        //time left
        try {
            timeLeftTextView.setText(Affichage.tempsRestant(MapIdAlarm.get(ListActif.get(0))));
        }catch (Exception e){
            timeLeftTextView.setText(Affichage.tempsRestant(null));
        }
        activeAlarmTextView.setText(Affichage.NombreAlarmsActives(ListActif.size()));

        //ecriture
        StorageUtils.writeObject(mainActivity, MapIdAlarm, StorageUtils.alarmsFile);



        //maj affichage
        Trie.listItems();
        for(int i = 0; i < MainActivity.items.size(); i++){
            System.out.println(items.get(i).id);
        }
        MainFragment.adapter.notifyDataSetChanged();
    }




    public void effacer(Alarm currentAlarm){

        long id = currentAlarm.id;


        MapIdAlarm.remove(id);
        MapIdDate.remove(id);

        if(ListActif.contains(id)){
            ListActif.remove(id);
        }
        else{
            ListInactif.remove(id);
        }



        Trie.listSortId();

        //maj nb alarm active
        activeAlarmTextView.setText(Affichage.NombreAlarmsActives(ListActif.size()));

        //time left
        try {
            timeLeftTextView.setText(Affichage.tempsRestant(MapIdAlarm.get(ListActif.get(0))));
        }catch (Exception e){
            timeLeftTextView.setText(Affichage.tempsRestant(null));
        }

        //ecriture
        StorageUtils.writeObject(mainActivity, MapIdAlarm, StorageUtils.alarmsFile);

        //maj affichage
        Trie.itemsRemove(currentAlarm.id);
        MainFragment.adapter.notifyDataSetChanged();
        
        AlertHelper.remove(currentAlarm, mainActivity);

        Toast.makeText(mainActivity, "effacé", Toast.LENGTH_SHORT).show();

    }

    public void modifier(Alarm currentAlarm, View v){

        MainActivity activity = (MainActivity) v.getContext();
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerView, AddFragment.newInstance(true, currentAlarm))
                .commit();
    }

    public void actualiser(){
        Trie.actualiser();

        //number active alarm
        activeAlarmTextView.setText(Affichage.NombreAlarmsActives(ListActif.size()));

        //time left
        try {
            timeLeftTextView.setText(Affichage.tempsRestant(MapIdAlarm.get(ListActif.get(0))));
        }catch (Exception e){
            timeLeftTextView.setText(Affichage.tempsRestant(null));
        }

        MainFragment.adapter.notifyDataSetChanged();
    }


}
