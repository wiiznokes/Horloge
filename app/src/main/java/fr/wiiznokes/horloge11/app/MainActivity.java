package fr.wiiznokes.horloge11.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.*;

public class MainActivity extends AppCompatActivity {




    ImageButton addAlarm;
    EditText addAlarmText;

    //liste object Alarm
    private List<Alarm> Array1;
    //dictionnaire key:id valeur:position dans Array1
    private Map<Integer, Integer> MapIdPos;
    //dictionnaire key:id valeur:dateSonnerie
    private Map<Integer, Calendar> MapIdDate;
    //liste id alarm actif triée
    private List<Integer> ListActif;
    //liste id alarm Inactif triée
    private List<Integer> ListInactif;
    //liste somme de ListActif et Listinactif
    private List<Integer> ListSortId;


    //element utiles pour la maj d'affichage
    public LinearLayout linearLayout1;
    public TextView textViewTempsRestant;
    public TextView textViewAlarmeActive;









    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    //bouton save addActivity
                    if(result.getResultCode() == 0){

                        addAlarmText.setVisibility(View.INVISIBLE);

                        //creation de tous les objets
                        init();

                        //recuperation de l'objet Alarm
                        Alarm Alarme = Array1.get(Array1.size() - 1);

                        //creation de l'affichage de l'alarme
                        ConstraintLayout constraintLayout = new Affichage().newConstaintLayout(Alarme, MainActivity.this);

                        //ajout de l'affichage de l'alarme
                        linearLayout1.addView(constraintLayout, ListSortId.indexOf(Alarme.getId()));

                        //recuperation du switch
                        SwitchMaterial switchView = (SwitchMaterial) constraintLayout.getChildAt(2);
                        switchView.setOnClickListener(v -> {
                            new InteractHelper().switchHelper(switchView, Array1, MapIdPos, MapIdDate,
                                    ListActif, ListInactif, ListSortId,
                                    textViewTempsRestant, linearLayout1, textViewAlarmeActive);
                            //ecriture du fichier
                            new StorageUtils().write(Array1, MainActivity.this);

                        });

                        //maj nb alarmes actives
                        textViewAlarmeActive.setText(new Affichage().NombreAlarmsActives(ListActif.size()));

                        //maj temps restant
                        textViewTempsRestant.setText(new Affichage().tempsRestant(Array1.get(MapIdPos.get(ListActif.get(0)))));

                    }
                    //bouton retour addActivity
                    if(result.getResultCode() == 11){
                        addAlarmText.setVisibility(View.INVISIBLE);
                    }

                }
            }
    );





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //recuperation des vues pour affichage
        addAlarm = findViewById(R.id.floatingActionButton4);
        addAlarmText = findViewById(R.id.editTextTextPersonName);
        linearLayout1 = findViewById(R.id.linearLayout1);
        textViewTempsRestant = findViewById(R.id.textView4);
        textViewAlarmeActive = findViewById(R.id.textView2);


        //creation du fichier si il n'existe pas avec un tableau vide
        if(new StorageUtils().read(this)== null){
            List<Alarm> ArrayInit = new ArrayList<Alarm>();
            //ecriture
            new StorageUtils().write(ArrayInit, this);
        }


        //creation de tous les objets
        init();
        //affichage du nombre d'alarmes actives
        textViewAlarmeActive.setText(new Affichage().NombreAlarmsActives(ListActif.size()));
        //affichage du temps restant
        if(ListActif.size() > 0){
            textViewTempsRestant.setText(new Affichage().tempsRestant(Array1.get(MapIdPos.get(ListActif.get(0)))));
        }
        else{
            textViewTempsRestant.setText(R.string.tempsRestant0alarm);
        }
        //affichage des alarmes crées
        List<List> ListViews = new Affichage().afficheAlarmesInit(Array1, ListSortId, MapIdPos, this, linearLayout1);
        //recuperation de la liste des views des switchs
        List<SwitchMaterial> switchsView = ListViews.get(0);

        //boucle qui recuperer les views des switchs
        for(SwitchMaterial switchView : switchsView){
            switchView.setOnClickListener(v -> {
                new InteractHelper().switchHelper(switchView, Array1, MapIdPos, MapIdDate,
                        ListActif, ListInactif, ListSortId,
                        textViewTempsRestant, linearLayout1, textViewAlarmeActive);
                //ecriture du fichier
                new StorageUtils().write(Array1, MainActivity.this);
            });
        }





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
                    activityResultLauncher.launch(intent);
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
                addAlarmText.setOnFocusChangeListener((view1, hasFocus) -> {
                    if (!hasFocus) {
                        //si plus de focus
                        addAlarmText.setVisibility(View.INVISIBLE);
                        addAlarmText.setText("");
                        InputMethodManager imm1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm1.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    }
                });
            }
        });

    }

    private void init(){
        Array1 = new StorageUtils().read(this);
        MapIdPos = new Trie().MapIdPos(Array1);
        MapIdDate = new Trie().MapIdDate(Array1);
        ListActif = new Trie().ListActifInit(Array1, MapIdDate);
        ListInactif = new Trie().ListInactifInit(Array1, MapIdDate);
        ListSortId = new Trie().ListSortId(ListActif, ListInactif);
    }






    //fonction qui active une alarm
    public void onTimeSet(Alarm Alarme) {
        Calendar c = new Trie().dateProchaineSonnerie(Alarme);
        startAlarm(c, Alarme.getId());
    }
    private void startAlarm(Calendar c, int id){


        Intent intent = new Intent(MainActivity.this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, id, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        System.out.println("hello, je suis dans startAlarm");
        System.out.println(c.getTime());
        System.out.println(id);


    }
    private void cancelAlarm(Alarm Alarm){

        Intent intent = new Intent(MainActivity.this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, Alarm.getId(), intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    private void test(List<Alarm> Array1){

        for(Alarm alarm : Array1){
            System.out.println("L'alarme d'ID " + alarm.getId() + " : " + alarm.getHoursText());
            System.out.println("Active : " + alarm.isActive());
        }
        System.out.println("\n");
    }

    private void testActif(List<Integer> ListActif){
        System.out.println("La taille de ListActif est de " + ListActif.size());
        for(Integer i : ListActif){
            System.out.println(i);
        }
        System.out.println("\n");
    }
    private void testInactif(List<Integer> ListInactif){
        System.out.println("La taille de ListInactif est de " + ListInactif.size());
        for(Integer i : ListInactif){
            System.out.println(i);
        }
        System.out.println("\n");
    }

    private void testMapIdPos(Map<Integer, Integer> MapIdPos){
        System.out.println("MapIdPos :");
    }



}