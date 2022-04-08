package fr.wiiznokes.horloge11.utils.interact;


import android.content.Context;
import android.widget.TextView;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.utils.affichage.Affichage;
import fr.wiiznokes.horloge11.utils.alert.AlertHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.StorageUtils;
import fr.wiiznokes.horloge11.utils.storage.Trie;

public class InteractHelper {

    private final TextView textViewTempsRestant;
    private final TextView textViewAlarmeActive;

    private final Context context;
    private final AlertHelper alertHelper;







    public InteractHelper(TextView textViewTempsRestant, TextView textViewAlarmeActive, Context context){
        this.textViewTempsRestant = textViewTempsRestant;
        this.textViewAlarmeActive = textViewAlarmeActive;
        this.context = context;
        this.alertHelper = new AlertHelper(context);
    }

    public void switchHelper(Alarm currentAlarm){

        //l'alarm devient inactive
        if(currentAlarm.active){
            currentAlarm.active = false;

            //si l'alarm etait la première, affichage temps restant modifié
            if (currentAlarm.id == MainActivity.ListActif.get(0)){
                if(MainActivity.ListActif.size() >= 2){
                    textViewTempsRestant.setText(Affichage.tempsRestant(MainActivity.MapIdAlarm.get(MainActivity.ListActif.get(1))));
                }
                else{
                    textViewTempsRestant.setText(R.string.tempsRestant0alarm);
                }
            }
            MainActivity.ListActif.remove(currentAlarm.id);
            Trie.ListInactifChange(currentAlarm.id);
            //remove Alarm de AlarmManager
            alertHelper.remove(currentAlarm);
        }
        //l'alarm devient active
        else {
            currentAlarm.active = true;

            MainActivity.ListInactif.remove(currentAlarm.id);
            Trie.ListActifChange(currentAlarm.id);

            //si l'alarm devient la première, affichage temps restant modifié
            if (currentAlarm.id == MainActivity.ListActif.get(0)){
                textViewTempsRestant.setText(Affichage.tempsRestant(currentAlarm));
            }
            //ajout de l'alarm au AlarmManager
            alertHelper.add(currentAlarm);
        }

        MainActivity.removeItem(MainActivity.ListSortId.indexOf(currentAlarm.id));

        MainActivity.MapIdAlarm.put(currentAlarm.id, currentAlarm);
        Trie.ListSortId();

        textViewAlarmeActive.setText(Affichage.NombreAlarmsActives(MainActivity.ListActif.size()));

        //ecriture
        StorageUtils.writeObject(context, MainActivity.MapIdAlarm, StorageUtils.alarmsFile);

        //maj affichage
        MainActivity.addItem(currentAlarm, MainActivity.ListSortId.indexOf(currentAlarm.id));
    }




    public void effacer(Alarm currentAlarm){

        long id = currentAlarm.id;

        //si alarm est la premiere
        if(MainActivity.ListActif.size() > 0){
            if(MainActivity.ListActif.get(0) == id){


                if(MainActivity.ListActif.size() > 1){
                    textViewTempsRestant.setText(Affichage.tempsRestant(MainActivity.items.get(1)));
                }
                else {
                    textViewTempsRestant.setText(R.string.tempsRestant0alarm);
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

        MainActivity.removeItem(MainActivity.ListSortId.indexOf(id));
        Trie.ListSortId();

        //maj nb alarm active
        textViewAlarmeActive.setText(Affichage.NombreAlarmsActives(MainActivity.ListActif.size()));

        //ecriture
        StorageUtils.writeObject(context, MainActivity.MapIdAlarm, StorageUtils.alarmsFile);

    }


}
