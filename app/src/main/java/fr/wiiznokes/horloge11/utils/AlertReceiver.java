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
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.app.NotifActivity;

public class AlertReceiver extends BroadcastReceiver {
    private static final String channelID = "channelID";
    private static final String channelName = "Channel Name";
    private NotificationManager nManager;
    private Context context;



    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        //recuperation de l'objet Alarm
        Bundle extras = intent.getExtras();
        Alarm currentAlarm = (Alarm) extras.get("alarm");

        //creation du pending intent vers alarm notif
        Intent intentAlarmNotif = new Intent(context, NotifActivity.class);
        intentAlarmNotif.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent fullScreenPendingIntent  = PendingIntent.getActivity(context, 0, intentAlarmNotif, PendingIntent.FLAG_MUTABLE);

        //creation du channel
        createChannel();

        //creation de la notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelID)
                //ajout de notify pending intent
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(currentAlarm.getNameAlarm())
                .setContentText("Reveille-toi il est " + currentAlarm.getHoursText())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setContentIntent(fullScreenPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);




        //affichage notif
        getManager(nManager).notify(1, builder.build());

    }






    private void createChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //creation de l'objet channel
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Je suis la description du channel");
            //ajout du channel au Notification manager
            getManager(nManager).createNotificationChannel(channel);
        }
    }
    private NotificationManager getManager(NotificationManager nManager){
        if (nManager == null){
            this.nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return this.nManager;
    }


}