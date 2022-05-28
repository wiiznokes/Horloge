package fr.wiiznokes.horloge.utils.helper;

import android.content.Context;
import android.media.AudioAttributes;
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
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setFlags(AudioAttributes.FLAG_AUDIBILITY_ENFORCED)
                        .setLegacyStreamType(AudioManager.STREAM_ALARM)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());
                mediaPlayer.setDataSource(context, uri);
                mediaPlayer.prepare();

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
