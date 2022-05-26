package fr.wiiznokes.horloge11.utils.notif.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.utils.storage.Alarm;


public class AlertHelper {

    static AlarmManager alarmManager;


    public static void add(Alarm currentAlarm, Context context, long time){
        Intent alertIntent = new Intent(
                context,
                AlertReceiver.class
        );
        alertIntent.putExtra("alarm", currentAlarm);
        PendingIntent alertPendingIntent = PendingIntent.getBroadcast(context, (int) currentAlarm.id, alertIntent, PendingIntent.FLAG_IMMUTABLE);

        //intent pour le system
        Intent showIntent = new Intent(
                context,
                MainActivity.class
        );
        PendingIntent showPendingIntent = PendingIntent.getBroadcast(context, (int) currentAlarm.id, showIntent, PendingIntent.FLAG_IMMUTABLE);

        //info sur la date et l'action de modif du system
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(time, showPendingIntent);
        //setAlarm
        getAlarmManager(context).setAlarmClock(alarmClockInfo, alertPendingIntent);
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
