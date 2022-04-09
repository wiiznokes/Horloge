package fr.wiiznokes.horloge11.fragments.helperFrag;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.wiiznokes.horloge11.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HeaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeaderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private static String arg1;


    public HeaderFragment() {
        // Required empty public constructor
    }

    public static HeaderFragment newInstance(String param1) {
        HeaderFragment fragment = new HeaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        arg1 = param1;
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
        View view = inflater.inflate(R.layout.fragment_header, container, false);



        TextView headerName = view.findViewById(R.id.settingTextView);
        if(arg1.equals("setting")){
            headerName.setText(getResources().getString(R.string.settingTV));
        }
        if(arg1.equals("sonnerie")){
            headerName.setText(getResources().getString(R.string.chooseSonnerie));
        }

        return view;
    }
}