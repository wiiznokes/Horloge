package fr.wiiznokes.horloge11.fragments.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import fr.wiiznokes.horloge11.R;


public class SettingFragment extends Fragment {

    ImageButton returnButton;


    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        returnButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_setting, container, false);

        returnButton = view.findViewById(R.id.returnButton);

        return view;
    }
}