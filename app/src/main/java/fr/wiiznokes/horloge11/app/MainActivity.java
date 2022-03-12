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

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.*;

public class MainActivity extends AppCompatActivity {


    //getter and setter List et Map
    public Map<Integer, Alarm> getMapIdAlarm() {
        return MapIdAlarm;
    }
    public void setMapIdAlarm(Map<Integer, Alarm> mapIdAlarm) {
        MapIdAlarm = mapIdAlarm;
    }

    public Map<Integer, Calendar> getMapIdDate() {
        return MapIdDate;
    }
    public void setMapIdDate(Map<Integer, Calendar> mapIdDate) {
        MapIdDate = mapIdDate;
    }

    public List<Integer> getListActif() {
        return ListActif;
    }
    public void setListActif(List<Integer> listActif) {
        ListActif = listActif;
    }

    public List<Integer> getListInactif() {
        return ListInactif;
    }
    public void setListInactif(List<Integer> listInactif) {
        ListInactif = listInactif;
    }

    public List<Integer> getListSortId() {
        return ListSortId;
    }
    public void setListSortId(List<Integer> listSortId) {
        ListSortId = listSortId;
    }

    public List<Alarm> getListSortAlarm() {
        return ListSortAlarm;
    }
    public void setListSortAlarm(List<Alarm> listSortAlarm) {
        ListSortAlarm = listSortAlarm;
    }

    //dictionnaire key:id valeur:Alarm
    public Map<Integer, Alarm> MapIdAlarm;
    //dictionnaire key:id valeur:dateSonnerie
    public Map<Integer, Calendar> MapIdDate;
    //liste id alarm actif triée
    public List<Integer> ListActif;
    //liste id alarm Inactif triée
    public List<Integer> ListInactif;
    //liste somme de ListActif et Listinactif
    public List<Integer> ListSortId;
    //liste des alarmes triée
    public List<Alarm> ListSortAlarm;


    //element utiles pour la maj d'affichage
    public ImageButton addAlarm;
    public EditText addAlarmText;

    public TextView getTextViewTempsRestant() {
        return textViewTempsRestant;
    }
    public TextView getTextViewAlarmeActive() {
        return textViewAlarmeActive;
    }
    public ListView getListView() {
        return listView;
    }
    public TextView textViewTempsRestant;
    public TextView textViewAlarmeActive;
    public ListView listView;

    private ModelAlarmeAdapter modelAlarmeAdapter;




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

                        new StorageUtils().write(MapIdAlarm, MainActivity.this);




                        //ajout de l'affichage de l'alarme

                        modelAlarmeAdapter.setListSortAlarm(ListSortAlarm);
                        listView.setAdapter(modelAlarmeAdapter);


                        //maj nb alarmes actives
                        textViewAlarmeActive.setText(new Affichage().NombreAlarmsActives(ListActif.size()));

                        //maj temps restant
                        textViewTempsRestant.setText(new Affichage().tempsRestant(ListSortAlarm.get(0)));

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

    private void initAjout(Alarm currentAlarm) {
        MapIdAlarm.put(currentAlarm.getId(), currentAlarm);
        MapIdDate.put(currentAlarm.getId(), new Trie().dateProchaineSonnerie(currentAlarm));
        int index;
        if(currentAlarm.isActive()){
            index = new Trie().ListActifChange(ListActif, currentAlarm.getId(), MapIdDate);
        }
        else {
            index = new Trie().ListInactifChange(ListInactif, currentAlarm.getId(), MapIdDate);
            index = ListActif.size() + index;
        }
        ListSortId = new Trie().ListSortId(ListActif, ListInactif);
        new Trie().ListSortAlarmChange(index, ListSortAlarm, currentAlarm);
    }




    private void init(){
        MapIdAlarm = new StorageUtils().read(this);
        MapIdDate = new Trie().MapIdDate(MapIdAlarm);
        ListActif = new Trie().ListActifInit(MapIdAlarm, MapIdDate);
        ListInactif = new Trie().ListInactifInit(MapIdAlarm, MapIdDate);
        ListSortId = new Trie().ListSortId(ListActif, ListInactif);
        ListSortAlarm = new Trie().ListSortAlarm(ListSortId, MapIdAlarm);
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

        //ajout des data au inflate
        modelAlarmeAdapter = new ModelAlarmeAdapter(context, ListSortAlarm);

        listView.setAdapter(modelAlarmeAdapter);


    }
}