package fr.wiiznokes.horloge11.utils;

import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;

public class InteractHelper extends MainActivity {

    private final String fileName = "save.txt";

    public void switchHelper(Switch switchView, List<Alarm> Array1,
                             Map<Integer, Integer> MapIdPos, Map<Integer, Calendar> MapIdDate,
                             List<Integer> ListActif, List<Integer> ListInactif, List<Integer> ListSortId,
                                TextView textview4, LinearLayout linearLayout1, TextView textView2){

        //cas du switch qui devient actif
        if(switchView.isChecked()){
            Array1.get(MapIdPos.get(switchView.getId())).setActive(true);
            //enlever L'id switchView.getId() de la liste inactive
            ListInactif.remove((Integer) switchView.getId());
            //appeler la fonction ajoute l'id au bonne endroit dans la liste active
            ListActif = new Trie().ListActifChange(ListActif, switchView.getId(), MapIdDate);

            //si l'alarm devient la première mise a jour temps restant
            if(ListActif.get(0) == switchView.getId()){
                textview4.setText(new Affichage().tempsRestant(Array1.get(MapIdPos.get(ListActif.get(0)))));
            }


        }
        //cas du switch qui devient inactif
        else{
            Array1.get(MapIdPos.get(switchView.getId())).setActive(false);


            ListInactif = new Trie().ListInactifChange(ListInactif, switchView.getId(), MapIdDate);

            //si l'alarme était la première
            if(ListActif.get(0) == switchView.getId()){
                if(ListActif.size() > 1) {
                    textview4.setText(new Affichage().tempsRestant(Array1.get(MapIdPos.get(ListActif.get(1)))));
                }
                else {
                    textview4.setText(R.string.tempsRestant0alarm);
                }
            }
            ListActif.remove((Integer) switchView.getId());
        }
        //ecriture du fichier
        new StorageUtils().write(fileName, Array1, this);

        //changer l'index d'affichage dans le LinearLayout

        ConstraintLayout constraintLayout = ((ConstraintLayout)(linearLayout1.getChildAt(ListSortId.indexOf(switchView.getId()))));
        //maj de ListSortId
        ListSortId = new Trie().ListSortId(ListActif, ListInactif);

        //maj de l'affichage
        linearLayout1.removeView(constraintLayout);
        linearLayout1.addView(constraintLayout, ListSortId.indexOf(switchView.getId()));

        //affichage du nombre d'alarmes actives

        textView2.setText(new Affichage().NombreAlarmsActives(ListActif.size()));


    }

}
