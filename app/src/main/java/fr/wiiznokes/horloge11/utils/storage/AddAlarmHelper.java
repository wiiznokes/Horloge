package fr.wiiznokes.horloge11.utils.storage;

import fr.wiiznokes.horloge11.app.MainActivity;

public class AddAlarmHelper {

    public AddAlarmHelper(){

    }

    public static void add(Alarm currentAlarm) {

        MainActivity.MapIdAlarm.put(currentAlarm.id, currentAlarm);
        MainActivity.MapIdDate.put(currentAlarm.id, Trie.dateProchaineSonnerie(currentAlarm));

        Trie.ListActifChange(currentAlarm.id);
        Trie.ListSortId();
    }

    public static void replace(Alarm currentAlarm){


    }




}
