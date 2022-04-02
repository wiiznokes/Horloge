package fr.wiiznokes.horloge11.utils.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.utils.storage.Alarm;


public class AlertHelper {

    Context context;
    AlarmManager alarmManager;



    public AlertHelper(Context context){
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    }






    public void add(Alarm currentAlarm){
        //creation du pending intent
        Intent intent = new Intent(
                context,
                AlertReceiver.class
        );
        intent.putExtra("alarm", currentAlarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 11, intent, PendingIntent.FLAG_IMMUTABLE);

        //recuperation de la date de sonnerie
        long time = MainActivity.MapIdDate.get(currentAlarm.id).getTimeInMillis();

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }

    public void remove(Alarm currentAlarm){
        Intent intent = new Intent(
                context,
                AlertReceiver.class
        );
        intent.putExtra("alarm", currentAlarm);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 11, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.cancel(pendingIntent);

    }


}
