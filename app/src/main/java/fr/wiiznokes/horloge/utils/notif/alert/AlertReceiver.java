package fr.wiiznokes.horloge.utils.notif.alert;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import fr.wiiznokes.horloge.R;
import fr.wiiznokes.horloge.app.AlarmActivity;
import fr.wiiznokes.horloge.utils.helper.SoundHelper;
import fr.wiiznokes.horloge.utils.notif.boutonNotifReceiver;
import fr.wiiznokes.horloge.utils.storage.Alarm;
import fr.wiiznokes.horloge.utils.storage.Setting;
import fr.wiiznokes.horloge.utils.storage.StorageUtils;

public class AlertReceiver extends BroadcastReceiver {
    private static final String channelID = "channelID";
    private static final String channelName = "Channel Name";
    private static NotificationManager notificationManager;


    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public void onReceive(Context context, Intent intent) {

        //recuperation de l'objet Alarm
        Bundle extras = intent.getExtras();
        Alarm currentAlarm = (Alarm) extras.get("alarm");

        //intent notif click
        Intent intentAlarmNotif = new Intent(context, AlarmActivity.class);
        intentAlarmNotif.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent fullScreenPendingIntent  = PendingIntent.getActivity(context, 0, intentAlarmNotif, PendingIntent.FLAG_MUTABLE);

        //intent snooze action
        Intent snoozeIntent = new Intent(context, boutonNotifReceiver.class);
        snoozeIntent.setAction("snooze");
        snoozeIntent.putExtra("alarm", currentAlarm);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent, 0);

        //intent remove action
        Intent removeIntent = new Intent(context, boutonNotifReceiver.class);
        removeIntent.setAction("remove");
        removeIntent.putExtra("alarm", currentAlarm);
        PendingIntent removePendingIntent = PendingIntent.getBroadcast(context, 1, removeIntent, 0);

        if (notificationManager == null){
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        //creation du channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Setting setting = (Setting) StorageUtils.readObject(context, StorageUtils.settingFile);
        SoundHelper.setSetting(setting);
        Uri uri = SoundHelper.alarmToUri(currentAlarm);

        //creation de la notification
        assert setting != null;
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(currentAlarm.alarmName)
                .setContentText(currentAlarm.alarmName)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setContentIntent(fullScreenPendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(uri)
                .setTimeoutAfter(setting.timeRing)
                .addAction(R.drawable.ic_launcher_foreground, "reporter", snoozePendingIntent)
                .addAction(R.drawable.ic_launcher_foreground, "effacer", removePendingIntent);


        //affichage notif
        notificationManager.notify((int) currentAlarm.id, notificationBuilder.build());

    }
}