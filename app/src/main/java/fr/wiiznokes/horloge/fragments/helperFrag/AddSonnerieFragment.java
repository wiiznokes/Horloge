package fr.wiiznokes.horloge.fragments.helperFrag;

import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.wiiznokes.horloge.R;
import fr.wiiznokes.horloge.app.MainActivity;
import fr.wiiznokes.horloge.utils.addAlarmHelper.AddRingHelper;
import fr.wiiznokes.horloge.utils.addAlarmHelper.CustomAdapterAddRing;
import fr.wiiznokes.horloge.utils.notif.SoundHelper;
import fr.wiiznokes.horloge.utils.storage.Setting;
import fr.wiiznokes.horloge.utils.storage.StorageUtils;


public class AddSonnerieFragment extends Fragment {

    public static final int settingSource = 0;
    public static final int addAlarmSource = 1;
    private static int source;


    //type utilisation
    //0 -> default
    //1 -> silence
    //2 -> uriSonnerie
    private static int type;

    private static String uri = "";

    //recyclerView
    protected List<String> dataset;
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
                    try {
                        uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI).toString();
                    } catch (Exception e){
                        uri = intent.getData().toString();
                    }
                    type = 2;
                    returnHelper();
                }
            }
    );


    public static AddSonnerieFragment newInstance(int sourceP) {
        source = sourceP;
        return new AddSonnerieFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //default case
        if(source == settingSource){
            dataset.add(AddRingHelper.defaultRingText(MainActivity.setting.type, MainActivity.setting.defaultUri));
            listUri.add(AddRingHelper.defaultRing(MainActivity.setting.type, MainActivity.setting.defaultUri));
            listSelect.add(false);
        }
        //silence case
        dataset.add("Silence");
        listUri.add(null);
        listSelect.add(false);
        //history
        dataset.addAll(MainActivity.setting.ringNameHistory);
        listUri.addAll(MainActivity.setting.uriHistory);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_sonnerie, container, false);

        recyclerView = view.findViewById(R.id.recyclerView2);
        //organise les views
        layoutManager = new LinearLayoutManager(requireContext());
        //relie les datas et les views
        adapter = new CustomAdapterAddRing(dataset, source);
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

        });

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    private void returnHelper(){
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        if(type == 2){
            bundle.putString("uri", uri);
            //save uri for history
            MainActivity.setting.uriHistory.add(uri);
            if(source != settingSource)
                StorageUtils.writeObject(requireContext(), MainActivity.setting, StorageUtils.settingFile);
        }

        getParentFragmentManager().setFragmentResult("data", bundle);
        getParentFragmentManager().popBackStack();
    }


}