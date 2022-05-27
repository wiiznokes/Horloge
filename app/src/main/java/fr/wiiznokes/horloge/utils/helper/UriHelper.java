package fr.wiiznokes.horloge.utils.helper;

import android.net.Uri;

import fr.wiiznokes.horloge.utils.storage.Alarm;
import fr.wiiznokes.horloge.utils.storage.Setting;

public class UriHelper {

    public static String uriName(Uri uri){
        String name = uri.toString();
        return name;
    }


    public static String defaultRingText(int type, Uri defaultUri){
        String text = "Default (";
        switch (type){
            case 1:
                text = text + "silence" + ')';
                break;
            case 2:
                text = text + uriName(defaultUri) + ')';
                break;
        }
        return text;
    }

    public static String alarmRingText(Alarm currentAlarm, int settingType, Uri defaultUri){
        String text = "";
        switch (currentAlarm.type){
            //0 -> default
            //1 -> silence
            //2 -> uriSonnerie
            case 0:
                text = defaultRingText(settingType, defaultUri);
                break;
            case 1:
                text = "silence";
                break;
            case 2:
                text = uriName(currentAlarm.uri);
                break;
        }
        return text;
    }

    public static Uri alarmToUri(Alarm currentAlarm, int settingType, Uri defaultUri){
        switch (currentAlarm.type){
            //0 -> default
            //1 -> silence
            //2 -> uriSonnerie
            case 0:
                if(settingType == 1)
                    return null;
                if(settingType == 2)
                    return defaultUri;
            case 2:
                return currentAlarm.uri;
            default:
                return null;
        }
    }

}
