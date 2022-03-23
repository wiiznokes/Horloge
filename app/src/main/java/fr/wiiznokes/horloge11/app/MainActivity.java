package fr.wiiznokes.horloge11.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.*;

public class MainActivity extends AppCompatActivity {



    //dictionnaire key:id valeur:Alarm
    static public Map<Long, Alarm> MapIdAlarm;
    //dictionnaire key:id valeur:dateSonnerie
    static public Map<Long, Calendar> MapIdDate;
    //liste id alarm actif triée
    static public List<Long> ListActif;
    //liste id alarm Inactif triée
    static public List<Long> ListInactif;
    //liste somme de ListActif et Listinactif
    static public List<Long> ListSortId;


    //ajout alarme
    public ImageButton addAlarm;
    public EditText addAlarmText;

    //element interactif
    public TextView textViewTempsRestant;
    public TextView textViewAlarmeActive;


    //listview
    public static ArrayList<Alarm> items;
    public static ListView listView;
    public static ModelAlarmeAdapter adapter;





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
                        Alarm currentAlarme = (Alarm) result.getData().getSerializableExtra("alarm");

                        //creation de tous les objets
                        initAjout(currentAlarme);


                        MainActivity.addItem(currentAlarme, ListSortId.indexOf(currentAlarme.getId()));

                        StorageUtils.write(MainActivity.this, MapIdAlarm);



                        //maj nb alarmes actives
                        textViewAlarmeActive.setText(Affichage.NombreAlarmsActives(ListActif.size()));
                        //maj temps restant
                        textViewTempsRestant.setText(Affichage.tempsRestant(items.get(0)));


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

        adapter = new ModelAlarmeAdapter(this, items, textViewTempsRestant, textViewAlarmeActive, listView);
        listView.setAdapter(adapter);






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


    public static void addItem(Alarm alarm, int position){
        items.add(position, alarm);
        listView.setAdapter(adapter);
    }

    public static void removeItem(int position){
        items.remove(position);
        listView.setAdapter(adapter);
    }






    private void initStorage(){
        //creation du fichier si il n'existe pas avec un tableau vide
        if(StorageUtils.read(this)== 1) {
            Map<Long, Alarm> MapIdAlarmInit = new HashMap<>();
            //ecriture
            StorageUtils.write(this, MapIdAlarmInit);
        }


        StorageUtils.read(this);
        Trie.MapIdDate();
        Trie.ListActifInit();
        Trie.ListInactifInit();
        Trie.ListSortId();
        Trie.ListItems();
    }

    private void initAffichage(){
        //recuperation des vues pour affichage
        this.addAlarm = findViewById(R.id.floatingActionButton4);
        this.addAlarmText = findViewById(R.id.editTextTextPersonName);
        this.textViewTempsRestant = findViewById(R.id.textView4);
        this.textViewAlarmeActive = findViewById(R.id.textView2);
        listView = findViewById(R.id.list_view1);


        //affichage du nombre d'alarmes actives
        textViewAlarmeActive.setText(Affichage.NombreAlarmsActives(ListActif.size()));
        //affichage du temps restant
        if(ListActif.size() > 0){
            textViewTempsRestant.setText(Affichage.tempsRestant(MapIdAlarm.get(ListActif.get(0))));
        }
        else{
            textViewTempsRestant.setText(R.string.tempsRestant0alarm);
        }
    }

    private void initAjout(Alarm currentAlarm) {
        MapIdAlarm.put(currentAlarm.getId(), currentAlarm);
        MapIdDate.put(currentAlarm.getId(), Trie.dateProchaineSonnerie(currentAlarm));

        Trie.ListActifChange(currentAlarm.getId());
        Trie.ListSortId();

    }
}