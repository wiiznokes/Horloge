package fr.wiiznokes.horloge11.temp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.temp.AddActivity;

public class addSonnerieActivity extends AppCompatActivity {


    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {



                        if (result.getData() != null) {
                            Uri uri;
                            uri = result.getData().getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                            if (uri == null) {
                                uri = result.getData().getData();
                            }
                            AddActivity.currentAlarm.uriSonnerie = uri.toString();
                            AddActivity.currentAlarm.silence = false;
                        }
                        finish();

                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sonnerie);


        ImageButton boutonRetour = findViewById(R.id.boutonRetour);
        boutonRetour.setOnClickListener(v -> {
            finish();
        });


        //choisir ses sons
        View fragMySong = findViewById(R.id.fragMySong);
        fragMySong.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            activityResultLauncher.launch(intent);

        });


        //choisir dans la bank de sons
        View fragAppareilSong = findViewById(R.id.fragAppareilSong);
        fragAppareilSong.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);

            activityResultLauncher.launch(intent);
        });

        View fragSilence = findViewById(R.id.fragSilence);
        fragSilence.setOnClickListener(v -> {

            AddActivity.currentAlarm.silence = true;
            finish();
        });

    }
}