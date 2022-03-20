package fr.wiiznokes.horloge11.utils;


import android.content.Context;
import android.widget.TextView;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;

public class InteractHelper {

    private final TextView textViewTempsRestant;
    private final TextView textViewAlarmeActive;

    private final Context context;







    public InteractHelper(TextView textViewTempsRestant, TextView textViewAlarmeActive, Context context){
        this.textViewTempsRestant = textViewTempsRestant;
        this.textViewAlarmeActive = textViewAlarmeActive;
        this.context = context;
    }

    public void switchHelper(Alarm currentAlarm){


        if(currentAlarm.isActive()){
            currentAlarm.setActive(false);

            //si l'alarm etait la première, affichage temps restant modifié
            if (currentAlarm.getId() == MainActivity.ListActif.get(0)){
                if(MainActivity.ListActif.size() >= 2){
                    textViewTempsRestant.setText(Affichage.tempsRestant(MainActivity.MapIdAlarm.get(MainActivity.ListActif.get(1))));
                }
                else{
                    textViewTempsRestant.setText(R.string.tempsRestant0alarm);
                }
            }
            MainActivity.ListActif.remove(currentAlarm.getId());
            Trie.ListInactifChange(MainActivity.ListInactif, currentAlarm.getId(), MainActivity.MapIdDate);
        }
        else {
            currentAlarm.setActive(true);

            MainActivity.ListInactif.remove(currentAlarm.getId());
            Trie.ListActifChange(MainActivity.ListActif, currentAlarm.getId(), MainActivity.MapIdDate);

            //si l'alarm devient la première, affichage temps restant modifié
            if (currentAlarm.getId() == MainActivity.ListActif.get(0)){
                textViewTempsRestant.setText(Affichage.tempsRestant(MainActivity.MapIdAlarm.get(MainActivity.ListActif.get(0))));
            }
        }

        MainActivity.MapIdAlarm.put(currentAlarm.getId(), currentAlarm);
        MainActivity.ListSortId = Trie.ListSortId(MainActivity.ListActif, MainActivity.ListInactif);

        //MainActivity.changePosItem(positionBefore, currentAlarm, positionAfter);

        textViewAlarmeActive.setText(Affichage.NombreAlarmsActives(MainActivity.ListActif.size()));

        //ecriture
        StorageUtils.write(MainActivity.MapIdAlarm, context);
    }




    public void effacer(Alarm currentAlarm){

        long id = currentAlarm.getId();


        //si alarm est la premiere
        if(MainActivity.ListActif.size() > 0){
            if(MainActivity.ListActif.get(0) == id){
                try {
                    MainActivity.ListActif.get(1);
                    textViewTempsRestant.setText(Affichage.tempsRestant(MainActivity.items.get(1)));
                }
                catch (IndexOutOfBoundsException indexOutOfBoundsException){
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
        MainActivity.ListSortId = Trie.ListSortId(MainActivity.ListActif, MainActivity.ListInactif);


        //maj nb alarm active
        textViewAlarmeActive.setText(Affichage.NombreAlarmsActives(MainActivity.ListActif.size()));


        //ecriture
        StorageUtils.write(MainActivity.MapIdAlarm, context);

    }


}
