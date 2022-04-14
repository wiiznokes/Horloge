package fr.wiiznokes.horloge11.utils.storage;

import static fr.wiiznokes.horloge11.app.MainActivity.ListActif;
import static fr.wiiznokes.horloge11.app.MainActivity.ListInactif;
import static fr.wiiznokes.horloge11.app.MainActivity.ListSortId;
import static fr.wiiznokes.horloge11.app.MainActivity.MapIdAlarm;
import static fr.wiiznokes.horloge11.app.MainActivity.MapIdDate;
import static fr.wiiznokes.horloge11.app.MainActivity.items;

import android.content.Context;
import android.widget.Toast;

import fr.wiiznokes.horloge11.utils.alert.AlertHelper;

public class AddAlarmHelper {

    public static void addAlarm(Alarm currentAlarm, Context context) {

        MapIdAlarm.put(currentAlarm.id, currentAlarm);
        MapIdDate.put(currentAlarm.id, Trie.dateProchaineSonnerie(currentAlarm));

        Trie.ListActifChange(currentAlarm.id);
        Trie.ListSortId();



        items.add(ListSortId.indexOf(currentAlarm.id), currentAlarm);

        //ajout Alarm a AlarmManger
        AlertHelper.add(currentAlarm, context);

        StorageUtils.writeObject(context, MapIdAlarm, StorageUtils.alarmsFile);

    }

    public static void modifAlarm(Alarm currentAlarm, Context context) {

        items.remove(MapIdAlarm.get(currentAlarm.id));

        MapIdAlarm.put(currentAlarm.id, currentAlarm);
        MapIdDate.put(currentAlarm.id, Trie.dateProchaineSonnerie(currentAlarm));

        ListActif.remove(currentAlarm.id);
        ListInactif.remove(currentAlarm.id);
        Trie.ListActifChange(currentAlarm.id);
        Trie.ListSortId();

        items.add(ListSortId.indexOf(currentAlarm.id), currentAlarm);

        //ajout Alarm a AlarmManger
        AlertHelper.add(currentAlarm, context);

        StorageUtils.writeObject(context, MapIdAlarm, StorageUtils.alarmsFile);
        Toast.makeText(context, "modifié", Toast.LENGTH_SHORT).show();
    }
}
