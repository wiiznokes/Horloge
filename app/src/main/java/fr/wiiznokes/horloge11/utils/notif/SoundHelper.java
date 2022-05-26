package fr.wiiznokes.horloge11.utils.notif;

import android.net.Uri;

import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.Setting;

public class SoundHelper {

    public static Uri uriAlarm(Alarm currentAlarm, Setting setting){
        switch (currentAlarm.type){
            //0 -> default
            //1 -> silence
            //2 -> uriSonnerie
            case 0:
                if(setting.silence)
                    return null;
                else
                    return Uri.parse(setting.defaultUri);
            case 2:
                return Uri.parse(currentAlarm.uriSonnerie);
            default:
                return null;
        }
    }

    public static String ringName(Alarm currentAlarm, Setting setting){
        String text = "";
        switch (currentAlarm.type){
            case 0:
                text = "default";
                if(setting.silence)
                    text = text + " : " + "silence";
                else
                    text = text + " : " + setting.defaultUri;
                break;

            case 1:
                text = "silence";
                break;
            case 2:
                text = currentAlarm.uriSonnerie;
                break;
        }
        return text;
    }

    public static void lire
}
