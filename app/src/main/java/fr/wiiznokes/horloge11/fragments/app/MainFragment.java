package fr.wiiznokes.horloge11.fragments.app;

import static fr.wiiznokes.horloge11.app.MainActivity.ListActif;
import static fr.wiiznokes.horloge11.app.MainActivity.MapIdAlarm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.utils.affichage.Affichage;
import fr.wiiznokes.horloge11.utils.affichage.ModelAlarmeAdapter;
import fr.wiiznokes.horloge11.utils.storage.Alarm;


public class MainFragment extends Fragment {


    public ImageButton settingButton;
    public ImageButton addAlarmButton;


    public TextView activeAlarmTextView;
    public TextView timeLeftTextView;

    public ListView listView;
    public static ModelAlarmeAdapter adapter;



    public MainFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initAffichage();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_main, container, false);

        this.settingButton = view.findViewById(R.id.settingButton);
        this.addAlarmButton = view.findViewById(R.id.addAlarmButton);

        this.activeAlarmTextView = view.findViewById(R.id.activeAlarmTextView);
        this.timeLeftTextView = view.findViewById(R.id.timeLeftTextView);
        this.listView = view.findViewById(R.id.list_view);

        return view;
    }

    public void initAffichage(){

        //number active alarm
        activeAlarmTextView.setText(Affichage.NombreAlarmsActives(MainActivity.ListActif.size()));

        //time left
        try {
            timeLeftTextView.setText(Affichage.tempsRestant(MapIdAlarm.get(ListActif.get(0))));
        }catch (Exception e){
            timeLeftTextView.setText(Affichage.tempsRestant(null));
        }

        //list view
        adapter = new ModelAlarmeAdapter((MainActivity) requireContext(), activeAlarmTextView, timeLeftTextView);
        listView.setAdapter(adapter);


        settingButton.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.fragmentContainerView, new SettingFragment())
                        .commit());


        addAlarmButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragmentContainerView, AddFragment.newInstance(false, null))
                    .commit();
        });
    }
}