package fr.wiiznokes.horloge11.utils.notif;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import fr.wiiznokes.horloge11.utils.notif.alert.AlertHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.Setting;
import fr.wiiznokes.horloge11.utils.storage.StorageUtils;

public class boutonNotifReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Setting setting = (Setting) StorageUtils.readObject(context, StorageUtils.settingFile);
        Alarm currentAlarm = (Alarm) intent.getSerializableExtra("alarm");

        //remove notification
        notificationManager.cancel((int) currentAlarm.id);

        String action = intent.getAction();
        switch (action){
            case "snooze":
                assert setting != null;
                AlertHelper.add(currentAlarm, context, System.currentTimeMillis() + setting.timeSnooze);
                Toast.makeText(context, "reporté", Toast.LENGTH_SHORT).show();
                break;

            case "remove":
                Toast.makeText(context, "éteinte", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
