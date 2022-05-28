package fr.wiiznokes.horloge.utils.helper;

import android.net.Uri;

import fr.wiiznokes.horloge.utils.storage.Alarm;
import fr.wiiznokes.horloge.utils.storage.Setting;

public class UriHelper {

    public static String uriName(Uri uri){
        String name = uri.toString();
        return name;
    }


    public static String defaultRingText(boolean settingSilence, Uri defaultUri){
        String text;
        if(settingSilence)
            text = "Default (silence)";
        else
            text = "Default (" + uriName(defaultUri) + ')';
        return text;
    }

    public static String alarmRingText(Alarm currentAlarm, boolean settingSilence, Uri defaultUri){
        String text = "";
        switch (currentAlarm.type){
            //0 -> default
            //1 -> silence
            //2 -> uriSonnerie
            case 0:
                text = defaultRingText(settingSilence, defaultUri);
                break;
            case 1:
                text = "silence";
                break;
            case 2:
                text = uriName(currentAlarm.getUri());
                break;
        }
        return text;
    }

    public static Uri alarmToUri(Alarm currentAlarm, boolean settingSilence, Uri defaultUri){
        switch (currentAlarm.type){
            //0 -> default
            //1 -> silence
            //2 -> uriSonnerie
            case 0:
                if(settingSilence)
                    return null;
                else
                    return defaultUri;
            case 2:
                return currentAlarm.getUri();
            default:
                return null;
        }
    }

}
