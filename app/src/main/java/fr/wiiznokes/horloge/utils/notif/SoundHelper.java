package fr.wiiznokes.horloge.utils.notif;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

import fr.wiiznokes.horloge.utils.storage.Alarm;
import fr.wiiznokes.horloge.utils.storage.Setting;

public class SoundHelper {
    private static MediaPlayer mediaPlayer;
    private static Setting setting;

    public SoundHelper() {
    }


    public static Uri uriAlarm(Alarm currentAlarm){
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

    public static String ringName(Alarm currentAlarm){
        String text = "";
        switch (currentAlarm.type){
            //0 -> default
            //1 -> silence
            //2 -> uriSonnerie
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

    public static void setSetting(Setting setting) {
        SoundHelper.setting = setting;
    }


    public static void setMediaPlayer(Context context, Uri uri) {
        mediaPlayer = new MediaPlayer();
        try {
            if(uri != null && !uri.toString().isEmpty()){
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setDataSource(context, uri);
                mediaPlayer.prepare();
                System.out.println("succes");
                System.out.println(uri);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void playTest() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.prepare();
            } else
                mediaPlayer.start();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
