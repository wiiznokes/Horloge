package fr.wiiznokes.horloge11.app;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
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
import fr.wiiznokes.horloge11.fragments.helperFrag.AddSonnerieFragment;
import fr.wiiznokes.horloge11.utils.affichage.Affichage;
import fr.wiiznokes.horloge11.utils.affichage.ModelAlarmeAdapter;
import fr.wiiznokes.horloge11.utils.alert.AlertHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.StorageUtils;
import fr.wiiznokes.horloge11.utils.storage.Trie;

public class MainActivity extends FragmentActivity {


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


    //list pour le listView
    public static ArrayList<Alarm> items;





    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initStorage();

        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .add(R.id.fragmentContainerView, mainFragment)
                .commit();


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
        items = Trie.ListItems();
    }


    public void addAlarm(Alarm currentAlarm) {

        MapIdAlarm.put(currentAlarm.id, currentAlarm);
        MapIdDate.put(currentAlarm.id, Trie.dateProchaineSonnerie(currentAlarm));

        Trie.ListActifChange(currentAlarm.id);
        Trie.ListSortId();



        items.add(ListSortId.indexOf(currentAlarm.id), currentAlarm);

        //ajout Alarm a AlarmManger
        AlertHelper.add(currentAlarm, this);

        StorageUtils.writeObject(MainActivity.this, MapIdAlarm, StorageUtils.alarmsFile);

    }

    public void modifAlarm(Alarm currentAlarm) {

        items.remove(MapIdAlarm.get(currentAlarm.id));

        MapIdAlarm.put(currentAlarm.id, currentAlarm);
        MapIdDate.put(currentAlarm.id, Trie.dateProchaineSonnerie(currentAlarm));

        ListActif.remove(currentAlarm.id);
        ListInactif.remove(currentAlarm.id);
        Trie.ListActifChange(currentAlarm.id);
        Trie.ListSortId();

        items.add(ListSortId.indexOf(currentAlarm.id), currentAlarm);

        //ajout Alarm a AlarmManger
        AlertHelper.add(currentAlarm, this);

        StorageUtils.writeObject(MainActivity.this, MapIdAlarm, StorageUtils.alarmsFile);
        Toast.makeText(MainActivity.this, "modifié", Toast.LENGTH_SHORT).show();
    }

}