package fr.wiiznokes.horloge11.app;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.*;

public class MainActivity extends AppCompatActivity {

    private final String fileName = "save.txt";

    private ImageButton addAlarm;
    private EditText addAlarmText;
    private final static int ADD_ACTIVITY_CALL_ID = 10;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //creation du fichier si il n'existe pas avec un tableau vide
        if(read(fileName)== null){

            List<Object> ArrayInit = new ArrayList<Object>();
            //ecriture
            write(fileName, ArrayInit);
        }

        //lecture du fichier
        List<Object> Array1 = read(fileName);


        //affichage des alarmes crées et récuperation d'une liste de view des ConstraintLayouts, Switchs
        List<List> Ids = afficheAlarmes(Array1);
        //recuperation de la liste des views des switchs
        List<Switch> switchsView = Ids.get(1);


        //recuperatiion de la vue des alarme active et initialisation
        TextView alarmeActive = (TextView) findViewById(R.id.textView2);
        int nbactAlrm = numberActivAlarm(Array1);
        if(nbactAlrm == 0 || nbactAlrm == 1){
            String a = String.valueOf(nbactAlrm);
            alarmeActive.setText(getString(R.string.active_alarme) + a);
        }
        else{
            String a = String.valueOf(nbactAlrm);
            alarmeActive.setText(getString(R.string.active_alarme) + a);
        }

        //boucle pour changer le nombre d'alarme active
        //compteur pour lister tous les objets Alarm
        int i = 0;
        for(Switch switchView : switchsView){
                int finalI = i;
                switchView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(switchView.isChecked()){
                            Alarm alarm = (Alarm) Array1.get(finalI);
                            alarm.setActive(true);
                        }
                        else{
                            Alarm alarm = (Alarm) Array1.get(finalI);
                            alarm.setActive(false);
                        }
                        int nbactAlrm = numberActivAlarm(Array1);
                        if(nbactAlrm == 0 || nbactAlrm == 1){
                            String a = String.valueOf(nbactAlrm);
                            alarmeActive.setText(getString(R.string.active_alarme) + a);
                        }
                        else{
                            String a = String.valueOf(nbactAlrm);
                            alarmeActive.setText(getString(R.string.active_alarme) + a);
                        }
                    }
                });
                i = i+1;
            }












        //recuperration des views pour ajouter une alarme
        this.addAlarm = findViewById(R.id.floatingActionButton4);
        this.addAlarmText = findViewById(R.id.editTextTextPersonName);
        addAlarm.setOnClickListener(view -> {
            //si l'edit text est deja visible et que l'on click sur le + et que l'on a donné une nom à l'alarme
            if (addAlarmText.getVisibility() == View.VISIBLE) {

                if(addAlarmText.length() != 0){



                    //lancement de AddActivity
                    //creation de l'intention à partir du context et du fichier .class à ouvrir
                    Intent intent = new Intent(
                            MainActivity.this,
                            AddActivity.class
                    );
                    //ajout d'information dans l'intention
                    intent.putExtra("alarmName", addAlarmText.getText().toString());
                    //lancement de addActivity avec un id de lancement
                    startActivityForResult(intent, ADD_ACTIVITY_CALL_ID);
                }

            }
            else{
                //edit text visible
                addAlarmText.setVisibility(View.VISIBLE);
                //demande du focus
                addAlarmText.requestFocus();
                //demande du clavier
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                //on regarde un changement de focus
                addAlarmText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        //si plus de focus
                        addAlarmText.setVisibility(View.INVISIBLE);
                        addAlarmText.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    }
                }
            });
            }



        });

    }



    //recuperation resultat d'une activité lancée
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ACTIVITY_CALL_ID){

            //bouton retour
            if(resultCode == 11) {
                addAlarmText.setVisibility(View.INVISIBLE);
            }
            //bouton save
            else if(resultCode == 0){

                addAlarmText.setVisibility(View.INVISIBLE);
                //lecture du fichier
                List<Object> Array1 = read(fileName);
                List<Object> Array2 = new ArrayList<Object>();
                Array2.add(Array1.get(Array1.size()-1));
                //affichage de l'alarme crée
                afficheAlarmes(Array2);

            }
        }
    }

    public void write(String fileName, List<Object> tab){

        try {
            FileOutputStream output = this.openFileOutput(fileName, MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(tab);
            out.close();
            output.close();
        } catch (Exception e) {
            System.out.println("erreur dans l'écriture");
        }

    }

    public List<Object> read(String fileName){
        List<Object> Array1;
        try {
            FileInputStream input = this.openFileInput(fileName);
            ObjectInputStream in = new ObjectInputStream(input);
            Array1 = (List<Object>) in.readObject();
            in.close();
            input.close();
            return Array1;
        } catch (Exception e) {
            System.out.println("erreur dans la lecture");
        }
        return null;
    }

    public List<List> afficheAlarmes(List<Object> listeDesAlarmes){



        //creation list pour les id des constraints layout
        List<ConstraintLayout> constraintsView = new ArrayList<>();

        //creation list pour les id des switchs
        List<Switch> switchsView = new ArrayList<>();



        for (Object AlarmeObject : listeDesAlarmes){

            //cast object Alarm
            Alarm Alarme = (Alarm) AlarmeObject;

            //recupération de linear layout
            LinearLayout linearLayout= findViewById(R.id.linearLayout1);

            //création du constraint Layout
            ConstraintLayout constraintLayout = new ConstraintLayout(this);
            constraintLayout.setId(View.generateViewId());
            constraintsView.add((ConstraintLayout) findViewById(constraintLayout.getId()));

            //objet set pour ajouter des contraintes
            ConstraintSet set = new ConstraintSet();

            //création heure alarme
            TextView textView = new TextView(this);
            textView.setText(Alarme.getHoursText());
            textView.setId(View.generateViewId());

            //création nom alarme
            TextView textView2 = new TextView(this);
            textView2.setText(Alarme.getNameAlarm());
            textView2.setId(View.generateViewId());

            //création switch
            Switch switch2 = new Switch(this);
            switch2.setText("");
            switch2.setChecked(Alarme.isActive());
            switch2.setId(View.generateViewId());

            //ajout de switch a la liste des views
            switchsView.add(switch2);


            //création jour alarme
            TextView textView3 = new TextView(this);
            if (Alarme.isWeek()){
                textView3.setText("Tous les jours");
            }
            else{
                String joursActif = "";
                if(Alarme.isMonday()){
                    joursActif = joursActif + "lun ";
                }
                if (Alarme.isTuesday()){
                    joursActif = joursActif + "mar ";
                }
                if (Alarme.isWednesday()){
                    joursActif = joursActif + "mer ";
                }
                if (Alarme.isThursday()){
                    joursActif = joursActif + "jeu ";
                }
                if (Alarme.isFriday()){
                    joursActif = joursActif + "ven ";
                }
                if (Alarme.isSaturday()){
                    joursActif = joursActif + "sam ";
                }
                if (Alarme.isSunday()){
                    joursActif = joursActif + "dim ";
                }
                textView3.setText(joursActif);
            }

            textView3.setId(View.generateViewId());




            //ajout des view au constraint layout
            constraintLayout.addView(textView);
            constraintLayout.addView(textView2);
            constraintLayout.addView(switch2);
            constraintLayout.addView(textView3);

            //ajout constraint layout au linear layout
            linearLayout.addView(constraintLayout);

            //lien entre set et constraint layout
            set.clone(constraintLayout);

            //constraint pour l'heure de l'alarme
            set.connect(textView.getId(), ConstraintSet.LEFT, ((View) textView.getParent()).getId(), ConstraintSet.LEFT);

            //constraint pour le nom de l'alarme
            set.connect(textView2.getId(), ConstraintSet.LEFT, textView.getId(), ConstraintSet.RIGHT);
            set.connect(textView2.getId(), ConstraintSet.RIGHT, switch2.getId(), ConstraintSet.LEFT);
            set.connect(textView2.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.TOP);

            //consraint pour le switch
            set.connect(switch2.getId(), ConstraintSet.RIGHT, ((View) switch2.getParent()).getId(), ConstraintSet.RIGHT);
            set.connect(switch2.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.TOP);
            set.connect(switch2.getId(), ConstraintSet.BOTTOM, textView3.getId(), ConstraintSet.BOTTOM);

            //constraint pour les jours de sonnerie
            set.connect(textView3.getId(), ConstraintSet.TOP, textView.getId(), ConstraintSet.BOTTOM);
            set.connect(textView3.getId(), ConstraintSet.RIGHT, switch2.getId(), ConstraintSet.LEFT);
            set.connect(textView3.getId(), ConstraintSet.LEFT, textView.getId(), ConstraintSet.LEFT);

            //application des constraints
            set.applyTo(constraintLayout);

        }


        List<List> ListIds = new ArrayList<>();
        ListIds.add(constraintsView);
        ListIds.add(switchsView);


        return ListIds;

    }


    public Integer numberActivAlarm (List<Object> listeDesAlarmes){

        Integer i = 0;

        for (Object AlarmeObject : listeDesAlarmes) {

            //cast object Alarm
            Alarm Alarme = (Alarm) AlarmeObject;

            if(Alarme.isActive()){

                i = i + 1;
            }



        }
        return i;
    }

   //List de list d'index de l'alarme trié en focntion de la date de sonnerie d'une alarme et list de date de sonnerie trié

    /*
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<List> listTriee (List<Alarm> Array1){


        //creation list des index triés
        List<Integer> indexTries = new ArrayList<>();

        //création list des dates triés en focntion des indexs
        List<>

        //recuperation de la date
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm");
        String result = formatter.format(now);

        for(Alarm alrm: Array1){


        }




    }*/



    //fonction qui prend une liste d'index trié en fonction du temps restant et un liste d'objet Alarm et qui renvoie un liste d'index trié en fonction du temps restant avant sonnerie et de l'activation de l'alarme



}