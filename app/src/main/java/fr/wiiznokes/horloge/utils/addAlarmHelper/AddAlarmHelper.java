package fr.wiiznokes.horloge.utils.addAlarmHelper;

import static fr.wiiznokes.horloge.app.MainActivity.MapIdAlarm;

import android.content.Context;
import android.widget.Toast;

import fr.wiiznokes.horloge.utils.notif.alert.AlertHelper;
import fr.wiiznokes.horloge.utils.storage.Alarm;
import fr.wiiznokes.horloge.utils.storage.StorageUtils;
import fr.wiiznokes.horloge.utils.storage.Trie;

public class AddAlarmHelper{



    public static boolean isWeek(Alarm currentAlarm){
        return (currentAlarm.monday && currentAlarm.tuesday && currentAlarm.wednesday && currentAlarm.thursday && currentAlarm.friday && currentAlarm.saturday && currentAlarm.sunday)
                || (!currentAlarm.monday && !currentAlarm.tuesday && !currentAlarm.wednesday && !currentAlarm.thursday && !currentAlarm.friday && !currentAlarm.saturday && !currentAlarm.sunday);
    }

    public static String daysActives(Alarm currentAlarm, String allDaysText, String[] daysInWeekArray){
        String text = "";
        if(currentAlarm.week){
            text = allDaysText;
        }
        else {
            if(currentAlarm.monday){text = daysInWeekArray[0] + " ";}
            if(currentAlarm.tuesday){text = text + daysInWeekArray[1] + " ";}
            if(currentAlarm.wednesday){text = text + daysInWeekArray[2] + " ";}
            if(currentAlarm.thursday){text = text + daysInWeekArray[3] + " ";}
            if(currentAlarm.friday){text = text + daysInWeekArray[4] + " ";}
            if(currentAlarm.saturday){text = text + daysInWeekArray[5] + " ";}
            if(currentAlarm.sunday){text = text + daysInWeekArray[6] + " ";}
        }
        return text;
    }


    public static void addAlarm(Alarm currentAlarm, Context context) {

        MapIdAlarm.put(currentAlarm.id, currentAlarm);

        //ajout Alarm a AlarmManger
        AlertHelper.add(currentAlarm, context, Trie.dateProchaineSonnerie(currentAlarm).getTimeInMillis());

        StorageUtils.writeObject(context, MapIdAlarm, StorageUtils.alarmsFile);

        Toast.makeText(context, "ajouté", Toast.LENGTH_SHORT).show();

    }

    public static void modifAlarm(Alarm currentAlarm, Context context) {

        MapIdAlarm.put(currentAlarm.id, currentAlarm);

        //AlarmManger
        AlertHelper.remove(currentAlarm, context);
        AlertHelper.add(currentAlarm, context, Trie.dateProchaineSonnerie(currentAlarm).getTimeInMillis());

        StorageUtils.writeObject(context, MapIdAlarm, StorageUtils.alarmsFile);
        Toast.makeText(context, "modifié", Toast.LENGTH_SHORT).show();
    }
}
