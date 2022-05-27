package fr.wiiznokes.horloge.utils.helper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import fr.wiiznokes.horloge.utils.storage.Alarm;
import fr.wiiznokes.horloge.utils.storage.Setting;

public class SoundHelper {
    private static MediaPlayer mediaPlayer;

    public SoundHelper() {
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
