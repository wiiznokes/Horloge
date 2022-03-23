package fr.wiiznokes.horloge11.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.NotifActivity;

public class AlertReceiver extends BroadcastReceiver {
    private static final String channelID = "channelID";
    private static final String channelName = "Channel Name";
    private NotificationManager nManager;



    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("je suis dans AlertReceiver");

        //recuperation de l'objet Alarm
        Bundle extras = intent.getExtras();

        Alarm currentAlarm = (Alarm) extras.get("alarm");

        //creation de l'intent alarm notif
        Intent intentAlarmNotif = new Intent(context, NotifActivity.class);
        intentAlarmNotif.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //create the pendingIntent
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent fullScreenPendingIntent  = PendingIntent.getActivity(context, 0, intentAlarmNotif, PendingIntent.FLAG_UPDATE_CURRENT);


        //creation du channel
        createChannel(context);

        //creation de la notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                //ajout de notify pending intent
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(currentAlarm.getNameAlarm())
                .setContentText("Reveille-toi il est " + currentAlarm.getHoursText())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setContentIntent(fullScreenPendingIntent);


        //affichage notif
        nManager = getManager(context, nManager);
        getManager(context, nManager).notify(1, builder.build());

    }
    private void createChannel(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //creation de l'objet channel
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            //importation de l'objet channel
            getManager(context, nManager).createNotificationChannel(channel);

        }


    }
    private NotificationManager getManager(Context context, NotificationManager nManager){
        if (nManager == null){
            nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            System.out.println("je cree nmanager");
        }

        return nManager;
    }
}