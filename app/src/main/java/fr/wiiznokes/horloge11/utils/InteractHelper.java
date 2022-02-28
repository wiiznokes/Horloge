package fr.wiiznokes.horloge11.utils;

import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;

public class InteractHelper extends MainActivity {


    public void switchHelper(SwitchMaterial switchView, List<Alarm> Array1,
                             Map<Integer, Integer> MapIdPos, Map<Integer, Calendar> MapIdDate,
                             List<Integer> ListActif, List<Integer> ListInactif, List<Integer> ListSortId,
                             TextView textview4, LinearLayout linearLayout1, TextView textView2){

        Alarm Alarme = Array1.get(MapIdPos.get(switchView.getId()));

        //cas du switch qui devient actif
        if(switchView.isChecked()){
            Alarme.setActive(true);
            //enlever l'id de la liste inactive
            ListInactif.remove(Alarme.getId());
            //appeler la fonction ajoute l'id au bonne endroit dans la liste active
            ListActif = new Trie().ListActifChange(ListActif, Alarme.getId(), MapIdDate);

            //si l'alarm devient la première, mise a jour temps restant
            if(ListActif.get(0) == Alarme.getId()){
                textview4.setText(new Affichage().tempsRestant(Alarme));
            }


        }
        //cas du switch qui devient inactif
        else{
            Alarme.setActive(false);

            ListInactif = new Trie().ListInactifChange(ListInactif, Alarme.getId(), MapIdDate);

            //si l'alarme était la première
            if(ListActif.get(0) == Alarme.getId()){
                try{
                    textview4.setText(new Affichage().tempsRestant(Array1.get(MapIdPos.get(ListActif.get(1)))));
                }
                catch (IndexOutOfBoundsException indexOutOfBoundsException){
                    textview4.setText(R.string.tempsRestant0alarm);
                }
            }
            ListActif.remove((Object) Alarme.getId());
        }



        //maj de ListSortId et enregistrement index
        int indexOfPosBefore = ListSortId.indexOf(Alarme.getId());
        ListSortId = new Trie().ListSortId(ListActif, ListInactif);

        //changer l'index d'affichage dans le LinearLayout si necessaire
        if(ListSortId.indexOf(Alarme.getId()) != indexOfPosBefore) {
            ConstraintLayout constraintLayout = ((ConstraintLayout) (linearLayout1.getChildAt(indexOfPosBefore)));
            //maj de l'affichage
            linearLayout1.removeView(constraintLayout);
            linearLayout1.addView(constraintLayout, ListSortId.indexOf(Alarme.getId()));
        }

        //affichage du nombre d'alarmes actives
        textView2.setText(new Affichage().NombreAlarmsActives(ListActif.size()));


        //ajout Alarm modifié a la liste
        Array1.set(MapIdPos.get(Alarme.getId()), Alarme);


    }

}
