package fr.wiiznokes.horloge11.fragments.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.fragments.helperFrag.AddSonnerieFragment;


public class SettingFragment extends Fragment {

    private static boolean modifchek = false;

    ImageButton returnButton;

    ConstraintLayout addSonnerieDefault;
    ConstraintLayout increaseTemporaly;
    ConstraintLayout timeRing;
    ConstraintLayout apparenceApp;



    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        returnButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        addSonnerieDefault.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragmentContainerView, AddSonnerieFragment.newInstance(true, null))
                    .commit();
        });

        boolean silence = false;

        silence = getArguments().getBoolean("silence");

        if(silence){
            Toast.makeText(requireContext(), "helloooo", Toast.LENGTH_SHORT).show();
        }
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
        addSonnerieDefault = view.findViewById(R.id.addSonnerieDefaultCL);
        increaseTemporaly = view.findViewById(R.id.increaseTemporalyCL);
        timeRing = view.findViewById(R.id.timeRingCL);
        apparenceApp = view.findViewById(R.id.apparenceAppCL);

        return view;
    }
}