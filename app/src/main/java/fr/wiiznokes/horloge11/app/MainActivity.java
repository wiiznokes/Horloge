package fr.wiiznokes.horloge11.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.*;

public class MainActivity extends AppCompatActivity {




    //dictionnaire key:id valeur:position dans Array1
    public Map<Integer, Alarm> MapIdAlarm;
    //dictionnaire key:id valeur:dateSonnerie
    public Map<Integer, Calendar> MapIdDate;
    //liste id alarm actif triée
    public List<Integer> ListActif;
    //liste id alarm Inactif triée
    public List<Integer> ListInactif;
    //liste somme de ListActif et Listinactif
    public List<Integer> ListSortId;


    //element utiles pour la maj d'affichage
    public ImageButton addAlarm;
    public EditText addAlarmText;

    public LinearLayout linearLayout;
    public TextView textViewTempsRestant;
    public TextView textViewAlarmeActive;

    public ListView listView;




    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public void onActivityResult(ActivityResult result) {

                    //bouton save addActivity
                    if(result.getResultCode() == 0){

                        addAlarmText.setVisibility(View.INVISIBLE);

                        //recuperation de l'objet Alarm
                        Alarm Alarme = (Alarm) result.getData().getSerializableExtra("AlarmeAdd");

                        //creation de tous les objets
                        initAjout(Alarme);

                        new StorageUtils().write(MapIdAlarm, MainActivity.this);




                        //ajout de l'affichage de l'alarme

                        /*
                        //switch
                        SwitchMaterial switchView = (SwitchMaterial) constraintLayout.getChildAt(1);
                        switchView.setOnClickListener(v -> {
                            new InteractHelper().switchHelper(switchView, Array1, MapIdPos, MapIdDate, ListActif, ListInactif, ListSortId, linearLayout, textViewTempsRestant, textViewAlarmeActive);
                            //ecriture du fichier
                            new StorageUtils().write(Array1, MainActivity.this);

                        });

                        //suppression alarm
                        constraintLayout.setOnLongClickListener(v -> {
                            AlertDialog.Builder popUp = new AlertDialog.Builder(MainActivity.this);
                            popUp.setNegativeButton("EFFACER", (dialog, which) -> {
                                Toast.makeText(MainActivity.this, "effacé", Toast.LENGTH_SHORT).show();
                                new InteractHelper().effacer(constraintLayout, Array1, MapIdPos, MapIdDate, ListActif, ListInactif, linearLayout, textViewTempsRestant, textViewAlarmeActive);
                                //ecriture du fichier
                                new StorageUtils().write(Array1, MainActivity.this);

                            });
                            popUp.setPositiveButton("MODIFIER", (dialog, which) -> Toast.makeText(MainActivity.this, "modifié", Toast.LENGTH_SHORT).show());
                            popUp.show();
                            return false;
                        });

                        //maj nb alarmes actives
                        textViewAlarmeActive.setText(new Affichage().NombreAlarmsActives(ListActif.size()));

                        //maj temps restant
                        textViewTempsRestant.setText(new Affichage().tempsRestant(Array1.get(MapIdPos.get(ListActif.get(0)))));

                         */

                    }
                    //bouton retour addActivity
                    if(result.getResultCode() == 11){
                        addAlarmText.setVisibility(View.INVISIBLE);
                    }

                }
            }
    );





    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initApp();

        //affichage des alarmes crées
        afficheAlarmesInit(this);



/*
        //boucle qui recuperer les views des switchs
        for(SwitchMaterial switchView : switchsView){
            switchView.setOnClickListener(v -> {
                new InteractHelper().switchHelper(switchView, Array1, MapIdPos, MapIdDate, ListActif, ListInactif, ListSortId, linearLayout, textViewTempsRestant, textViewAlarmeActive);
                //ecriture du fichier
                new StorageUtils().write(Array1, MainActivity.this);
            });
        }

        //recuperation de la liste des views des constaintLayout
        List<ConstraintLayout> constraintLayoutViews = ListViews.get(1);
        for (ConstraintLayout constraintLayout : constraintLayoutViews){
            //suppression alarm
            constraintLayout.setOnLongClickListener(v -> {

                AlertDialog.Builder popUp = new AlertDialog.Builder(MainActivity.this);

                //test
                popUp.setMessage(Array1.get(MapIdPos.get(constraintLayout.getId()-10000)).getNameAlarm());

                popUp.setNegativeButton("EFFACER", (dialog, which) -> {
                    Toast.makeText(MainActivity.this, "effacé", Toast.LENGTH_SHORT).show();
                    new InteractHelper().effacer(constraintLayout, Array1, MapIdPos, MapIdDate, ListActif, ListInactif, linearLayout, textViewTempsRestant, textViewAlarmeActive);
                    //ecriture du fichier
                    new StorageUtils().write(Array1, MainActivity.this);

                });
                popUp.setPositiveButton("MODIFIER", (dialog, which) -> Toast.makeText(MainActivity.this, "modifié", Toast.LENGTH_SHORT).show());
                popUp.show();
                return false;
            });
        }
*/


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

    private void initAjout(Alarm Alarme){
        MapIdAlarm.put(Alarme.getId(), Alarme);
        MapIdDate.put(Alarme.getId(), new Trie().dateProchaineSonnerie(Alarme));
        new Trie().ListActifChange(ListActif, Alarme.getId(), MapIdDate);
        new Trie().ListInactifChange(ListInactif, Alarme.getId(), MapIdDate);
        new Trie().ListSortId(ListActif, ListInactif);
    }





    private void init(){
        MapIdAlarm = new StorageUtils().read(this);
        MapIdDate = new Trie().MapIdDate(MapIdAlarm);
        ListActif = new Trie().ListActifInit(MapIdAlarm, MapIdDate);
        ListInactif = new Trie().ListInactifInit(MapIdAlarm, MapIdDate);
        ListSortId = new Trie().ListSortId(ListActif, ListInactif);
    }
    private void initApp(){
        //recuperation des vues pour affichage
        this.addAlarm = findViewById(R.id.floatingActionButton4);
        this.addAlarmText = findViewById(R.id.editTextTextPersonName);
        this.textViewTempsRestant = findViewById(R.id.textView4);
        this.textViewAlarmeActive = findViewById(R.id.textView2);
        this.listView = findViewById(R.id.list_view1);

        //creation du fichier si il n'existe pas avec un tableau vide
        if(new StorageUtils().read(this)== null) {
            Map<Integer, Alarm> MapIdAlarmInit = new HashMap<>();
            //ecriture
            new StorageUtils().write(MapIdAlarmInit, this);
        }
        init();
        //affichage du nombre d'alarmes actives
        textViewAlarmeActive.setText(new Affichage().NombreAlarmsActives(ListActif.size()));
        //affichage du temps restant
        if(ListActif.size() > 0){
            textViewTempsRestant.setText(new Affichage().tempsRestant(MapIdAlarm.get(ListActif.get(0))));
        }
        else{
            textViewTempsRestant.setText(R.string.tempsRestant0alarm);
        }
    }


    private void afficheAlarmesInit(Context context){

        List<Alarm> AlarmList = new ArrayList<>();
        AlarmList.addAll(MapIdAlarm.values());
        //ajout des data au inflate
        ModelAlarmeAdapter modelAlarmeAdapter = new ModelAlarmeAdapter(context, AlarmList);

        listView.setAdapter(modelAlarmeAdapter);



    }
}