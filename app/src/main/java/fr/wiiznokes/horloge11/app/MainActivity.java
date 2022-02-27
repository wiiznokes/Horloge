package fr.wiiznokes.horloge11.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.*;

public class MainActivity extends AppCompatActivity {

    private final String fileName = "save.txt";


    private ImageButton addAlarm;
    private EditText addAlarmText;
    private final static int ADD_ACTIVITY_CALL_ID = 10;

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








    //lancement d'une nouvelle activité
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    //bouton save addActivity
                    if(result.getResultCode() == 0){

                        addAlarmText.setVisibility(View.INVISIBLE);
                        //lecture du fichier
                        Array1 = new StorageUtils().read(fileName, MainActivity.this);
                        MapIdPos = new Trie().MapIdPos(Array1);
                        MapIdDate = new Trie().MapIdDate(Array1);

                        Alarm Alarm = Array1.get(Array1.size() - 1);
                        ListActif = new Trie().ListActifChange(ListActif, Alarm.getId(), MapIdDate);
                        ListSortId = new Trie().ListSortId(ListActif, ListInactif);
                        //maj affichage
                        ConstraintLayout constraintLayout = new Affichage().newConstaintLayout(Alarm.getId(), Alarm, MainActivity.this);
                        ((LinearLayout) findViewById(R.id.linearLayout1)).addView(constraintLayout, ListSortId.indexOf(Alarm.getId()));

                        //switch view
                        new InteractHelper().switchHelper(((Switch) constraintLayout.getChildAt(2)), Array1, MapIdPos, MapIdDate,
                                ListActif, ListInactif, ListSortId,
                                findViewById(R.id.textView4), findViewById(R.id.linearLayout1), findViewById(R.id.textView2));

                        //maj nb alarmes actives
                        TextView alarmeActive = findViewById(R.id.textView2);
                        alarmeActive.setText(new Affichage().NombreAlarmsActives(ListActif.size()));

                        //maj temps restant
                        if(ListActif.size() > 0){
                            ((TextView) findViewById(R.id.textView4)).setText(
                                    new Affichage().tempsRestant(Array1.get(MapIdPos.get(ListActif.get(0)))));
                        }
                        else{
                            ((TextView) findViewById(R.id.textView4)).setText(R.string.tempsRestant0alarm);
                        }




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



        //creation du fichier si il n'existe pas avec un tableau vide
        if(new StorageUtils().read(fileName, this)== null){

            List<Alarm> ArrayInit = new ArrayList<Alarm>();
            //ecriture
            new StorageUtils().write(fileName, ArrayInit, this);
        }
        //lecture du fichier
        Array1 = new StorageUtils().read(fileName, this);
        MapIdPos = new Trie().MapIdPos(Array1);
        MapIdDate = new Trie().MapIdDate(Array1);
        ListActif = new Trie().ListActifInit(Array1, MapIdDate, MapIdPos);
        ListInactif = new Trie().ListInactifInit(Array1, MapIdDate, MapIdPos);
        ListSortId = new Trie().ListSortId(ListActif, ListInactif);




        //affichage des alarmes crées
        List<List> ListViews = new Affichage().afficheAlarmesInit(Array1, ListSortId, MapIdPos, this, findViewById(R.id.linearLayout1));



        //recuperation de la liste des views des switchs
        List<Switch> switchsView = ListViews.get(0);

        //affichage du nombre d'alarmes actives
        TextView alarmeActive = findViewById(R.id.textView2);
        alarmeActive.setText(new Affichage().NombreAlarmsActives(ListActif.size()));
        //affichage du temps restant
        if(ListActif.size() > 0){
            ((TextView) findViewById(R.id.textView4)).setText(
                    new Affichage().tempsRestant(Array1.get(MapIdPos.get(ListActif.get(0)))));
        }
        else{
            ((TextView) findViewById(R.id.textView4)).setText(R.string.tempsRestant0alarm);
        }



        //boucle qui recuperer les views des switchs

        for(Switch switchView : switchsView){
                switchView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new InteractHelper().switchHelper(switchView, Array1, MapIdPos, MapIdDate,
                                ListActif, ListInactif, ListSortId,
                                findViewById(R.id.textView4), findViewById(R.id.linearLayout1), findViewById(R.id.textView2));

                    }
                });

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



}