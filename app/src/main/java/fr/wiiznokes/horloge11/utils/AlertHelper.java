package fr.wiiznokes.horloge11.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import fr.wiiznokes.horloge11.app.AddActivity;
import fr.wiiznokes.horloge11.app.MainActivity;


public class AlertHelper {

    Context context;


    public AlertHelper(Context context){
        this.context = context;
    }






    public static void add(Alarm currentAlarm, Context context){

        Intent intent = new Intent(
                context,
                AlertReceiver.class
        );
        intent.putExtra("alarm", currentAlarm);

        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 11, intent, PendingIntent.FLAG_IMMUTABLE);



        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        long time = MainActivity.MapIdDate.get(currentAlarm.getId()).getTimeInMillis();
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }


}
