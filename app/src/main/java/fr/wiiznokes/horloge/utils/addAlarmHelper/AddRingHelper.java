package fr.wiiznokes.horloge.utils.addAlarmHelper;

import android.net.Uri;

import fr.wiiznokes.horloge.utils.notif.SoundHelper;

public class AddRingHelper {

    public static String defaultRingText(int type, Uri uri){
        String text = "Default (";
        switch (type){
            case 1:
                text = text + "silence" + ')';
                break;
            case 2:
                text = text + SoundHelper.uriName(uri) + ')';
                break;
        }
        return text;
    }

    public static Uri defaultRing(int type, Uri uri){
        Uri finalUri = null;
        if(type != 1){
            finalUri = uri;
        }
        return finalUri;
    }
}
