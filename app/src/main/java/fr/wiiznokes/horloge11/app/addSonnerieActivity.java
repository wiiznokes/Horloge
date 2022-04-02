package fr.wiiznokes.horloge11.app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.storage.Alarm;

public class addSonnerieActivity extends AppCompatActivity {

    public Uri uri;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getData() != null) {
                        uri = result.getData().getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("silence", false);
                        resultIntent.putExtra("uri", uri);
                        setResult(0, resultIntent);
                        finish();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sonnerie);

        ImageButton boutonRetour = findViewById(R.id.boutonRetour);
        boutonRetour.setOnClickListener(v -> {
            setResult(1);
            finish();
        });


        Alarm currentAlarm = (Alarm) this.getIntent().getExtras().get("alarm");

        //choisir ses sons
        View fragMySong = findViewById(R.id.fragMySong);
        fragMySong.setOnClickListener(v -> {



        });


        //choisir dans la bank de sons
        View fragAppareilSong = findViewById(R.id.fragAppareilSong);
        fragAppareilSong.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

            activityResultLauncher.launch(intent);
        });

        View fragSilence = findViewById(R.id.fragSilence);
        fragSilence.setOnClickListener(v -> {
            currentAlarm.silence = true;

            Intent resultIntent = new Intent();
            resultIntent.putExtra("silence", true);
            setResult(0, resultIntent);
            finish();
        });

    }
}