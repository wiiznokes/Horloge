package fr.wiiznokes.horloge11.utils;


import android.widget.TextView;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;

public class InteractHelper {

    private final TextView textViewTempsRestant;
    private final TextView textViewAlarmeActive;





    public InteractHelper(TextView textViewTempsRestant, TextView textViewAlarmeActive){
        this.textViewTempsRestant = textViewTempsRestant;
        this.textViewAlarmeActive = textViewAlarmeActive;

    }

    public void switchHelper(long id, MainActivity mainActivity){
        Alarm currentAlarm = mainActivity.MapIdAlarm.get(id);


        if(currentAlarm.isActive()){
            currentAlarm.setActive(false);

            //si l'alarm etait la première, affichage temps restant modifié
            if (currentAlarm.getId() == mainActivity.ListActif.get(0)){
                if(mainActivity.ListActif.size() >= 2){
                    textViewTempsRestant.setText(mainActivity.affichage.tempsRestant(mainActivity.MapIdAlarm.get(mainActivity.ListActif.get(1))));
                }
                else{
                    textViewTempsRestant.setText(R.string.tempsRestant0alarm);
                }
            }
            mainActivity.ListActif.remove(currentAlarm.getId());
            mainActivity.trie.ListInactifChange(mainActivity.ListInactif, currentAlarm.getId(), mainActivity.MapIdDate);
        }
        else {
            currentAlarm.setActive(true);

            mainActivity.ListInactif.remove(currentAlarm.getId());
            mainActivity.trie.ListActifChange(mainActivity.ListActif, currentAlarm.getId(), mainActivity.MapIdDate);

            //si l'alarm devient la première, affichage temps restant modifié
            if (currentAlarm.getId() == mainActivity.ListActif.get(0)){
                textViewTempsRestant.setText(mainActivity.affichage.tempsRestant(mainActivity.MapIdAlarm.get(mainActivity.ListActif.get(0))));
            }
        }

        mainActivity.MapIdAlarm.put(currentAlarm.getId(), currentAlarm);
        mainActivity.ListSortId = mainActivity.trie.ListSortId(mainActivity.ListActif, mainActivity.ListInactif);
        textViewAlarmeActive.setText(mainActivity.affichage.NombreAlarmsActives(mainActivity.ListActif.size()));

        //ecriture
        new StorageUtils().write(mainActivity.MapIdAlarm, mainActivity);
    }




    public void effacer(long id, MainActivity mainActivity){

        int index = mainActivity.ListSortId.indexOf(id);

        //si alarm est la premiere
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
        mainActivity.ListSortId = mainActivity.trie.ListSortId(mainActivity.ListActif, mainActivity.ListInactif);


        //maj nb alarm active
        textViewAlarmeActive.setText(new Affichage().NombreAlarmsActives(mainActivity.ListActif.size()));

        //ecriture
        new StorageUtils().write(mainActivity.MapIdAlarm, mainActivity);

    }


}
