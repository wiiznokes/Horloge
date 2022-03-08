package fr.wiiznokes.horloge11.utils;

import android.annotation.SuppressLint;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;

public class InteractHelper {


    public void switchHelper(SwitchMaterial switchView, List<Alarm> Array1, Map<Integer, Integer> MapIdPos, Map<Integer, Calendar> MapIdDate,
                             List<Integer> ListActif, List<Integer> ListInactif, List<Integer> ListSortId,
                             LinearLayout linearLayout, TextView textViewTempsRestant, TextView textViewAlarmeActive){

        Alarm Alarme = Array1.get(MapIdPos.get(switchView.getId()));


        //cas du switch qui devient actif
        if(switchView.isChecked()){
            Alarme.setActive(true);
            //enlever l'id de la liste inactive
            ListInactif.remove((Object) Alarme.getId());
            //appeler la fonction ajoute l'id au bonne endroit dans la liste active
            new Trie().ListActifChange(ListActif, Alarme.getId(), MapIdDate);

            //si l'alarm devient la première, mise a jour temps restant
            if(ListActif.get(0) == Alarme.getId()){
                textViewTempsRestant.setText(new Affichage().tempsRestant(Alarme));
            }


        }
        //cas du switch qui devient inactif
        else{
            Alarme.setActive(false);

            new Trie().ListInactifChange(ListInactif, Alarme.getId(), MapIdDate);

            //si l'alarme était la première
            if(ListActif.get(0) == Alarme.getId()){
                try{
                    textViewTempsRestant.setText(new Affichage().tempsRestant(Array1.get(MapIdPos.get(ListActif.get(1)))));
                }
                catch (IndexOutOfBoundsException indexOutOfBoundsException){
                    textViewTempsRestant.setText(R.string.tempsRestant0alarm);
                }
            }
            ListActif.remove((Object) Alarme.getId());
        }



        //maj de ListSortId et enregistrement index
        int indexOfPosBefore = ListSortId.indexOf(Alarme.getId());
        ListSortId = new Trie().ListSortId(ListActif, ListInactif);

        //changer l'index d'affichage dans le LinearLayout si necessaire
        if(ListSortId.indexOf(Alarme.getId()) != indexOfPosBefore) {
            ConstraintLayout constraintLayout = ((ConstraintLayout) (linearLayout.getChildAt(indexOfPosBefore)));
            //maj de l'affichage
            linearLayout.removeView(constraintLayout);
            linearLayout.addView(constraintLayout, ListSortId.indexOf(Alarme.getId()));

        }

        //affichage du nombre d'alarmes actives
        textViewAlarmeActive.setText(new Affichage().NombreAlarmsActives(ListActif.size()));


        //ajout Alarm modifié a la liste
        Array1.set(MapIdPos.get(Alarme.getId()), Alarme);


    }

    public void effacer(ConstraintLayout constraintLayout, List<Alarm> Array1, Map<Integer, Integer> MapIdPos, Map<Integer, Calendar> MapIdDate,
                        List<Integer> ListActif, List<Integer> ListInactif,
                        LinearLayout linearLayout, TextView textViewTempsRestant, TextView textViewAlarmeActive){
        @SuppressLint("ResourceType") int id = constraintLayout.getId()-10000;

        //affichage temps restant
        if(ListActif.size() > 0 && id == ListActif.get(0)){
            try{
                textViewTempsRestant.setText(new Affichage().tempsRestant(Array1.get(MapIdPos.get(ListActif.get(1)))));
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException){
                textViewTempsRestant.setText(R.string.tempsRestant0alarm);
            }
        }

        Alarm Alarme = Array1.get(MapIdPos.get(id));
        if(Alarme.isActive()){
            ListActif.remove(MapIdPos.get(id));
        }
        else{
            ListInactif.remove(MapIdPos.get(id));
        }
        Array1.remove(Alarme);
        MapIdPos.remove(MapIdPos.get(id));
        MapIdDate.remove(MapIdPos.get(id));
        new Trie().ListSortId(ListActif, ListInactif);

        //affichage du nombre d'alarmes actives
        textViewAlarmeActive.setText(new Affichage().NombreAlarmsActives(ListActif.size()));

        linearLayout.removeView(constraintLayout);


    }


}
