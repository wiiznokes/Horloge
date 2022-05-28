package fr.wiiznokes.horloge.fragments.helperFrag;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import fr.wiiznokes.horloge.R;
import fr.wiiznokes.horloge.app.MainActivity;
import fr.wiiznokes.horloge.fragments.app.AddFragment;
import fr.wiiznokes.horloge.utils.helper.UriHelper;
import fr.wiiznokes.horloge.utils.affichage.addRingFrag.CustomAdapterAddRing;
import fr.wiiznokes.horloge.utils.helper.SoundHelper;
import fr.wiiznokes.horloge.utils.storage.StorageUtils;


public class AddRingFragment extends Fragment {

    public static final int settingSource = 0;
    public static final int addAlarmSource = 1;
    private static int source;


    //recyclerView
    public static List<String> dataset;
    public static List<Uri> listUri;
    public static List<Boolean> listSelect;
    protected RecyclerView recyclerView;
    protected CustomAdapterAddRing adapter;
    protected RecyclerView.LayoutManager layoutManager;

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                Intent intent = result.getData();
                if(intent != null){
                    Uri uri;
                    try {
                        uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    } catch (Exception e){
                        uri = intent.getData();
                    }
                    
                    if(uri != null)
                        returnNewUriHelper(uri);
                    else
                        Toast.makeText(requireContext(), "impossible de trouver le son", Toast.LENGTH_SHORT).show();
                }

            }
    );


    public static AddRingFragment newInstance(int sourceP) {
        source = sourceP;
        return new AddRingFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataset = new ArrayList<>();
        listUri = new ArrayList<>();
        listSelect = new ArrayList<>();

        //default case
        if(source == addAlarmSource){
            dataset.add(UriHelper.defaultRingText(MainActivity.setting.silence, MainActivity.setting.getDefaultUri()));
            listUri.add(MainActivity.setting.getDefaultUri());
        }
        //silence case
        dataset.add("Silence");
        listUri.add(null);

        //history
        dataset.addAll(MainActivity.setting.ringNameHistory);
        for(String uri : MainActivity.setting.uriHistory)
            listUri.add(Uri.parse(uri));
        for(int i = 0; i < dataset.size(); i++)
            listSelect.add(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_sonnerie, container, false);

        recyclerView = view.findViewById(R.id.recyclerView2);
        //organise les views
        layoutManager = new LinearLayoutManager(requireContext());
        //relie les datas et les views
        adapter = new CustomAdapterAddRing();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        //return
        //UI
        ImageButton returnButton = view.findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        //mySong
        ConstraintLayout mySong = view.findViewById(R.id.mySongCL);
        mySong.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("audio/*");
            activityResultLauncher.launch(intent);
        });

        //androidSong
        ConstraintLayout androidSong = view.findViewById(R.id.androidSongCL);
        androidSong.setOnClickListener(v -> {
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            activityResultLauncher.launch(intent);
        });

        //save
        ImageButton saveRingButton = view.findViewById(R.id.saveRingButton);
        saveRingButton.setOnClickListener(v -> {
            returnHelper();
        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    private void returnHelper(){

        int position = -1;
        int i = 0;
        while (position == -1 && i < listSelect.size()){
            if(listSelect.get(i))
                position = i;
            i++;
        }

        if(position != -1){

            if(source == settingSource){
                if(position == 0){
                    MainActivity.setting.silence = true;
                }
                else {
                    MainActivity.setting.setDefaultUri(listUri.get(position));
                    MainActivity.setting.silence = false;
                }
                StorageUtils.writeObject(requireContext(), MainActivity.setting, StorageUtils.settingFile);
            }
            else{
                if(position < 2){
                    AddFragment.currentAlarm.type = position;
                }
                else{
                    AddFragment.currentAlarm.type = 2;
                    AddFragment.currentAlarm.setUri(listUri.get(position));
                }
            }
            getParentFragmentManager().setFragmentResult("data", null);
            getParentFragmentManager().popBackStack();
        }
        else
            Toast.makeText(requireContext(), "aucun choix selectionné", Toast.LENGTH_SHORT).show();
    }


    private void returnNewUriHelper(Uri uri){
        if(source == settingSource){
            MainActivity.setting.silence = false;
            MainActivity.setting.setDefaultUri(uri);
            StorageUtils.writeObject(requireContext(), MainActivity.setting, StorageUtils.settingFile);
        }
        else {
            AddFragment.currentAlarm.type = 2;
            AddFragment.currentAlarm.setUri(uri);
        }

        //for uri history
        MainActivity.setting.uriHistory.add(uri.toString());
        MainActivity.setting.ringNameHistory.add(UriHelper.uriName(uri));
        StorageUtils.writeObject(requireContext(), MainActivity.setting, StorageUtils.settingFile);
        getParentFragmentManager().setFragmentResult("data", null);
        getParentFragmentManager().popBackStack();
    }


}