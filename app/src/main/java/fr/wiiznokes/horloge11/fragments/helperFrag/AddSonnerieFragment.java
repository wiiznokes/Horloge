package fr.wiiznokes.horloge11.fragments.helperFrag;

import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import fr.wiiznokes.horloge11.R;


public class AddSonnerieFragment extends Fragment {
    //type utilisation
    //0 -> default
    //1 -> silence
    //2 -> uriSonnerie
    private static int type;

    private static String uri = "";

    //UI
    private ImageButton returnButton;
    private ConstraintLayout mySong;
    private ConstraintLayout androidSong;
    private ConstraintLayout silenceSong;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                Intent data = result.getData();
                try {
                    System.out.println("hello" + data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI));
                }catch (Exception ignored){

                }
                try {

                }catch (Exception ignored){

                }

            }
    );

    public AddSonnerieFragment() {}

    public static AddSonnerieFragment newInstance() {
        return new AddSonnerieFragment();
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
            type = 1;
            returnHelper();
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void returnHelper(){
        System.out.println("type = " + type);
        System.out.println("uri = " + uri);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        if(type == 2)
            bundle.putString("uri", uri);

        getParentFragmentManager().setFragmentResult("data", bundle);
        getParentFragmentManager().popBackStack();
    }


}