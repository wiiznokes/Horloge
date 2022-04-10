package fr.wiiznokes.horloge11.fragments.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Objects;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.fragments.helperFrag.AddSonnerieFragment;
import fr.wiiznokes.horloge11.utils.affichage.Affichage;
import fr.wiiznokes.horloge11.utils.affichage.ModelAlarmeAdapter;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.Trie;


public class MainFragment extends Fragment {


    public ImageButton settingButton;
    public ImageButton addAlarmButton;


    public TextView activeAlarmTextView;
    public TextView timeLeftTextView;

    public ListView listView;
    public ModelAlarmeAdapter adapter;



    public MainFragment() {

    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void initAffichage(){

        this.settingButton = requireView().findViewById(R.id.settingButton);
        this.addAlarmButton = requireView().findViewById(R.id.addAlarmButton);

        this.activeAlarmTextView = requireView().findViewById(R.id.activeAlarmTextView);
        this.timeLeftTextView = requireView().findViewById(R.id.timeLeftTextView);
        this.listView = requireView().findViewById(R.id.list_view);



        activeAlarmTextView.setText(Affichage.NombreAlarmsActives(MainActivity.ListActif.size()));
        if(MainActivity.ListActif.size() > 0){
            timeLeftTextView.setText(Affichage.tempsRestant(MainActivity.MapIdAlarm.get(MainActivity.ListActif.get(0))));
        }
        else{
            timeLeftTextView.setText(R.string.tempsRestant0alarm);
        }
        

        adapter = new ModelAlarmeAdapter(getContext(), Trie.ListItems(), activeAlarmTextView, timeLeftTextView, listView);
        listView.setAdapter(adapter);

        settingButton.setOnClickListener(v ->
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new SettingFragment())
                        .commit());

        initAffichage();

        addAlarmButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new AddFragment())
                    .commit();
        });
    }
}