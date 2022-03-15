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
import android.widget.AdapterView;
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
import java.util.Random;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.*;

public class MainActivity extends AppCompatActivity {



    //dictionnaire key:id valeur:Alarm
    public Map<Long, Alarm> MapIdAlarm;
    //dictionnaire key:id valeur:dateSonnerie
    public Map<Long, Calendar> MapIdDate;
    //liste id alarm actif triée
    public List<Long> ListActif;
    //liste id alarm Inactif triée
    public List<Long> ListInactif;
    //liste somme de ListActif et Listinactif
    public List<Long> ListSortId;
    //liste des alarmes triée
    public List<Alarm> ListSortAlarm;


    //ajout alarme
    public ImageButton addAlarm;
    public EditText addAlarmText;

    //element interactif
    public TextView textViewTempsRestant;
    public TextView textViewAlarmeActive;
    public ListView listView;

    public ModelAlarmeAdapter modelAlarmeAdapter;
    public StorageUtils storageUtils;
    public Trie trie;
    public Affichage affichage;




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
                        Alarm currentAlarme = (Alarm) result.getData().getSerializableExtra("AlarmeAdd");

                        //creation de tous les objets
                        initAjout(currentAlarme);

                        storageUtils.write(MapIdAlarm, MainActivity.this);


                        //ajout de l'affichage de l'alarme

                        modelAlarmeAdapter.ListSortAlarm = ListSortAlarm;

                        modelAlarmeAdapter.notifyDataSetChanged();


                        //maj nb alarmes actives
                        textViewAlarmeActive.setText(affichage.NombreAlarmsActives(ListActif.size()));
                        //maj temps restant
                        textViewTempsRestant.setText(affichage.tempsRestant(ListSortAlarm.get(0)));


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

        initStorage();

        initAffichage();


        //ajout des data au inflate
        modelAlarmeAdapter = new ModelAlarmeAdapter(this);
        //affichage des alarmes crées
        listView.setAdapter(modelAlarmeAdapter);






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




    private void initStorage(){
        //creation du fichier si il n'existe pas avec un tableau vide
        if(new StorageUtils().read(this)== null) {
            Map<Long, Alarm> MapIdAlarmInit = new HashMap<>();
            //ecriture
            new StorageUtils().write(MapIdAlarmInit, this);
        }

        storageUtils = new StorageUtils();
        trie = new Trie();

        this.MapIdAlarm = storageUtils.read(MainActivity.this);
        this.MapIdDate = trie.MapIdDate(MapIdAlarm);
        this.ListActif = trie.ListActifInit(MapIdAlarm, MapIdDate);
        this.ListInactif = trie.ListInactifInit(MapIdAlarm, MapIdDate);
        this.ListSortId = trie.ListSortId(ListActif, ListInactif);
        this.ListSortAlarm = trie.ListSortAlarm(ListSortId, MapIdAlarm);
    }

    private void initAffichage(){
        //recuperation des vues pour affichage
        this.addAlarm = findViewById(R.id.floatingActionButton4);
        this.addAlarmText = findViewById(R.id.editTextTextPersonName);
        this.textViewTempsRestant = findViewById(R.id.textView4);
        this.textViewAlarmeActive = findViewById(R.id.textView2);
        this.listView = findViewById(R.id.list_view1);

        affichage = new Affichage();
        //affichage du nombre d'alarmes actives
        textViewAlarmeActive.setText(affichage.NombreAlarmsActives(ListActif.size()));
        //affichage du temps restant
        if(ListActif.size() > 0){
            textViewTempsRestant.setText(affichage.tempsRestant(MapIdAlarm.get(ListActif.get(0))));
        }
        else{
            textViewTempsRestant.setText(R.string.tempsRestant0alarm);
        }
    }

    private void initAjout(Alarm currentAlarm) {
        MapIdAlarm.put(currentAlarm.getId(), currentAlarm);
        MapIdDate.put(currentAlarm.getId(), new Trie().dateProchaineSonnerie(currentAlarm));

        int index = trie.ListActifChange(ListActif, currentAlarm.getId(), MapIdDate);

        ListSortId = trie.ListSortId(ListActif, ListInactif);
        ListSortAlarm.add(index, currentAlarm);
    }
}