package fr.wiiznokes.horloge11.fragments.helperFrag;

import static fr.wiiznokes.horloge11.app.MainActivity.setting;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.fragments.app.AddFragment;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.Setting;
import fr.wiiznokes.horloge11.utils.storage.StorageUtils;


public class AddSonnerieFragment extends Fragment {


    public static boolean sourceSetting;

    private static Alarm currentAlarm;
    private static String uri = "";
    private static boolean silence;


    private ImageButton returnButton;
    private ConstraintLayout mySong;
    private ConstraintLayout androidSong;
    private ConstraintLayout silenceSong;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                if (result.getData() != null) {
                    uri = result.getData().getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI).toString();
                    if (uri.isEmpty()) {
                        uri = result.getData().getData().toString();
                    }
                    returnHelper();
                }
            }
    );

    public AddSonnerieFragment() {
        // Required empty public constructor
    }



    public static AddSonnerieFragment newInstance(boolean setting, @Nullable Alarm alarm) {
        AddSonnerieFragment fragment = new AddSonnerieFragment();

        sourceSetting = setting;
        if(!sourceSetting) {
            currentAlarm = alarm;
        }

        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        returnButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        //mySong
        mySong.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            activityResultLauncher.launch(intent);
        });

        //androidSong
        androidSong.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            activityResultLauncher.launch(intent);
        });

        //silence
        silenceSong.setOnClickListener(v -> {
            silence = true;
            returnHelper();
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_sonnerie, container, false);

        returnButton = view.findViewById(R.id.returnButton);
        mySong = view.findViewById(R.id.mySongCL);
        androidSong = view.findViewById(R.id.androidSongCL);
        silenceSong = view.findViewById(R.id.silenceSongCL);

        return view;
    }

    private void returnHelper(){

        if(sourceSetting){
            Setting.defaultRing = uri;
            Setting.silence = silence;
            StorageUtils.writeObject(requireContext(), new Setting(), StorageUtils.settingFile);
            //getParentFragmentManager().popBackStack();
        }
        else {
            AddFragment.currentAlarm = currentAlarm;
            getParentFragmentManager().popBackStack();
        }
    }


}