package fr.wiiznokes.horloge11.fragments.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;


public class MainFragment extends Fragment {

    public ImageButton settingButton;
    public ImageButton addAlarmButton;

    public TextView timeLeftTextView;
    public TextView activeAlarmTextView;



    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        this.settingButton = view.findViewById(R.id.settingButton);
        this.addAlarmButton = view.findViewById(R.id.addAlarmButton);

        this.timeLeftTextView = view.findViewById(R.id.timeLeftTextView);
        this.activeAlarmTextView = view.findViewById(R.id.activeAlarmTextView);

        return view;
    }
}