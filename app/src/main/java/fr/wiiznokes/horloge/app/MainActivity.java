package fr.wiiznokes.horloge.app;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge.R;
import fr.wiiznokes.horloge.fragments.app.MainFragment;
import fr.wiiznokes.horloge.utils.storage.Alarm;
import fr.wiiznokes.horloge.utils.storage.Setting;
import fr.wiiznokes.horloge.utils.storage.StorageUtils;
import fr.wiiznokes.horloge.utils.storage.Trie;

public class MainActivity extends FragmentActivity {

    //stockage device
    //dictionnaire key:id valeur:Alarm
    static public Map<Long, Alarm> MapIdAlarm;
    static public Setting setting;

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
                .replace(R.id.fragmentContainerView, mainFragment)
                .commit();


    }





    private void initStorage(){
        //creation du fichier si il n'existe pas avec un tableau vide
        if(StorageUtils.readObject(this, StorageUtils.alarmsFile) == null) {
            Map<Long, Alarm> MapIdAlarmInit = new HashMap<>();
            //ecriture
            StorageUtils.writeObject(this, MapIdAlarmInit, StorageUtils.alarmsFile);
        }
        //creation du fichier parametre
        if(StorageUtils.readObject(this, StorageUtils.settingFile) == null) {
            //ecriture
            StorageUtils.writeObject(this, new Setting(), StorageUtils.settingFile);
        }
        MapIdAlarm = (Map<Long, Alarm>) StorageUtils.readObject(this, StorageUtils.alarmsFile);
        setting = (Setting) StorageUtils.readObject(this, StorageUtils.settingFile);

        Trie.mapIdDate();
        Trie.listActifInit();
        Trie.listInactifInit();
        Trie.listSortId();
        Trie.listItems();


    }




}