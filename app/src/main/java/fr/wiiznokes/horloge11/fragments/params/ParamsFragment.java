package fr.wiiznokes.horloge11.fragments.params;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.fragments.helperFrag.HeaderFragment;


public class ParamsFragment extends Fragment {



    public ParamsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        HeaderFragment.newInstance("setting");
        return inflater.inflate(R.layout.fragment_params, container, false);
    }
}