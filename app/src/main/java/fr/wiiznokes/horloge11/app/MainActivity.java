package fr.wiiznokes.horloge11.app;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.fragments.app.MainFragment;
import fr.wiiznokes.horloge11.temp.AddActivity;
import fr.wiiznokes.horloge11.utils.affichage.Affichage;
import fr.wiiznokes.horloge11.utils.affichage.ModelAlarmeAdapter;
import fr.wiiznokes.horloge11.utils.alert.AlertHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.StorageUtils;
import fr.wiiznokes.horloge11.utils.storage.Trie;

public class MainActivity extends AppCompatActivity {


    private MainFragment mainFragment;

    public Affichage affichage;



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

                        //recuperation de l'objet Alarm
                        Alarm currentAlarm = AddActivity.currentAlarm;

                        //creation de tous les objets
                        initAjout(currentAlarm);

                        //maj listView
                        MainActivity.addItem(currentAlarm, ListSortId.indexOf(currentAlarm.id));
                        //ajout Alarm a AlarmManger
                        new AlertHelper(MainActivity.this).add(currentAlarm);

                        StorageUtils.writeObject(MainActivity.this, MapIdAlarm, StorageUtils.alarmsFile);



                        //maj nb alarmes actives
                        textViewAlarmeActive.setText(Affichage.NombreAlarmsActives(ListActif.size()));
                        //maj temps restant
                        textViewTempsRestant.setText(Affichage.tempsRestant(items.get(0)));

                    }

                    if(result.getResultCode() == 1){
                        //recuperation de l'objet Alarm
                        Alarm currentAlarm = AddActivity.currentAlarm;

                        items.remove(MapIdAlarm.get(currentAlarm.id));

                        MapIdAlarm.put(currentAlarm.id, currentAlarm);
                        MapIdDate.put(currentAlarm.id, Trie.dateProchaineSonnerie(currentAlarm));

                        ListActif.remove(currentAlarm.id);
                        ListInactif.remove(currentAlarm.id);
                        Trie.ListActifChange(currentAlarm.id);
                        Trie.ListSortId();

                        MainActivity.addItem(currentAlarm, ListSortId.indexOf(currentAlarm.id));

                        //ajout Alarm a AlarmManger
                        new AlertHelper(MainActivity.this).add(currentAlarm);


                        StorageUtils.writeObject(MainActivity.this, MapIdAlarm, StorageUtils.alarmsFile);


                        //maj nb alarmes actives
                        textViewAlarmeActive.setText(Affichage.NombreAlarmsActives(ListActif.size()));
                        //maj temps restant
                        textViewTempsRestant.setText(Affichage.tempsRestant(items.get(0)));

                        Toast.makeText(MainActivity.this, "modifié", Toast.LENGTH_SHORT).show();
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

        configureAndShowMainFragment();


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
        if(StorageUtils.readObject(this, StorageUtils.alarmsFile) == null) {
            Map<Long, Alarm> MapIdAlarmInit = new HashMap<>();
            //ecriture
            StorageUtils.writeObject(this, MapIdAlarmInit, StorageUtils.alarmsFile);
        }


        MapIdAlarm = (Map<Long, Alarm>) StorageUtils.readObject(this, StorageUtils.alarmsFile);
        Trie.MapIdDate();
        Trie.ListActifInit();
        Trie.ListInactifInit();
        Trie.ListSortId();
        Trie.ListItems();
    }


    private void initAjout(Alarm currentAlarm) {

        MapIdAlarm.put(currentAlarm.id, currentAlarm);
        MapIdDate.put(currentAlarm.id, Trie.dateProchaineSonnerie(currentAlarm));

        Trie.ListActifChange(currentAlarm.id);
        Trie.ListSortId();

    }

    public ActivityResultLauncher<Intent> getactivityResultLauncher(){
        return activityResultLauncher;
    }


    private void configureAndShowMainFragment(){

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);

        if(mainFragment == null) {
            mainFragment = new MainFragment();
            mainFragment.affichage = affichage;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragmentContainerView, mainFragment)
                    .commit();
        }
    }
}