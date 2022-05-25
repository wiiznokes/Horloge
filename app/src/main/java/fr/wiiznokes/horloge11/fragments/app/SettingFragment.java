package fr.wiiznokes.horloge11.fragments.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.fragments.helperFrag.AddSonnerieFragment;
import fr.wiiznokes.horloge11.utils.storage.Setting;
import fr.wiiznokes.horloge11.utils.storage.StorageUtils;


public class SettingFragment extends Fragment {


    ImageButton returnButton;

    ConstraintLayout addSonnerieDefault;
    ConstraintLayout increaseTemporaly;
    ConstraintLayout timeRing;
    ConstraintLayout apparenceApp;

    CheckBox buttonVolume;



    public SettingFragment() {    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, bundle) -> {
            boolean silence = bundle.getBoolean("silence");
            String uri = bundle.getString("uri");

            Setting.silence = silence;
            if(!uri.isEmpty())
                Setting.defaultUri = uri;
        });

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
                    .replace(R.id.fragmentContainerView, AddSonnerieFragment.newInstance())
                    .commit();
        });

        buttonVolume.setChecked(Setting.buttonToSnooze);

        buttonVolume.setOnClickListener(v -> {
            Setting.buttonToSnooze = buttonVolume.isChecked();
            StorageUtils.writeObject(requireContext(), new Setting(), StorageUtils.settingFile);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_setting, container, false);

        returnButton = view.findViewById(R.id.returnButton);
        addSonnerieDefault = view.findViewById(R.id.addSonnerieDefaultCL);
        increaseTemporaly = view.findViewById(R.id.increaseTemporalyCL);
        timeRing = view.findViewById(R.id.timeRingCL);
        buttonVolume = view.findViewById(R.id.checkBox);
        apparenceApp = view.findViewById(R.id.apparenceAppCL);

        return view;
    }
}