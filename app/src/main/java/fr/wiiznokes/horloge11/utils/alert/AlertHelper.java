package fr.wiiznokes.horloge11.utils.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.utils.storage.Alarm;


public class AlertHelper {

    static AlarmManager alarmManager;


    public static void add(Alarm currentAlarm, Context context){
        //creation du pending intent
        Intent intent = new Intent(
                context,
                AlertReceiver.class
        );
        intent.putExtra("alarm", currentAlarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) currentAlarm.id, intent, PendingIntent.FLAG_IMMUTABLE);

        //recuperation de la date de sonnerie
        long time = MainActivity.MapIdDate.get(currentAlarm.id).getTimeInMillis();

        getAlarmManager(context).setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }



    public static void remove(Alarm currentAlarm, Context context){
        Intent intent = new Intent(
                context,
                AlertReceiver.class
        );
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) currentAlarm.id, intent, PendingIntent.FLAG_IMMUTABLE);

        getAlarmManager(context).cancel(pendingIntent);

    }

    public static AlarmManager getAlarmManager(Context context) {
        if(alarmManager == null){
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        }
        return alarmManager;
    }


}
