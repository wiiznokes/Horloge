package fr.wiiznokes.horloge11.fragments.app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import java.util.Random;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.fragments.helperFrag.AddSonnerieFragment;
import fr.wiiznokes.horloge11.utils.addAlarmHelper.AddAlarmHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.Trie;


public class AddFragment extends Fragment {

    public static Alarm currentAlarm;

    private ImageButton returnButton;
    private EditText alarmNameEditText;
    private EditText hoursEditText;
    private EditText minutesEditText;
    private ImageButton saveButton;
    private Button addSonnerieButton;
    private CheckBox vibrateCheckBox;

    private RadioButton monday;
    private RadioButton tuesday;
    private RadioButton wednesday;
    private RadioButton thursday;
    private RadioButton friday;
    private RadioButton saturday;
    private RadioButton sunday;

    private static boolean isModif;



    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance(boolean modif, @Nullable Alarm alarm) {
        AddFragment fragment = new AddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        isModif = modif;
        if(isModif){
            currentAlarm = alarm;
        }
        else {
            currentAlarm = new Alarm();
        }

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getParentFragmentManager().setFragmentResultListener("data", this, (requestKey, bundle) -> {

            Log.d("log Bundle after", String.valueOf(bundle.getBoolean("silence")));

            currentAlarm.silence = bundle.getBoolean("silence");
            String uri = bundle.getString("uri");

            if(!uri.isEmpty())
                currentAlarm.uriSonnerie = uri;
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        if(isModif){
            modifAlarmHelper();
        }
        else{
            currentAlarm.id = new Random().nextLong();
        }

        returnButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        alarmNameEditText.requestFocus();



        hoursEditText.addTextChangedListener(new TextWatcher() {
            String textBefore;


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textBefore = hoursEditText.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String futureText = hoursEditText.getText().toString();

                if(!futureText.isEmpty()){
                    int hoursINint = Integer.parseInt(futureText);

                    if(hoursEditText.length() > 2) {
                        futureText = textBefore;
                    }
                    //9h -> 09h
                    if(hoursINint < 10 && hoursINint > 2 && hoursEditText.length() < 2){
                        futureText = '0' + hoursEditText.getText().toString();
                        minutesEditText.requestFocus();
                    }

                    //heure < 23
                    if(hoursINint > 23) {
                        futureText = "2";
                    }
                }

                if(!hoursEditText.getText().toString().equals(futureText))
                    hoursEditText.setText(futureText);

                hoursEditText.setSelection(hoursEditText.length());
            }
        });


        minutesEditText.addTextChangedListener(new TextWatcher() {

            String textBefore;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textBefore = minutesEditText.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String futureText = minutesEditText.getText().toString();

                if(!futureText.isEmpty()){
                    int minuteInint = Integer.parseInt(futureText);


                    if(minutesEditText.length() > 2) {
                        futureText = textBefore;
                    }

                    //minute = "9" -> ""          "77" -> ""
                    if(minuteInint < 10 && minuteInint > 5){
                        futureText = "";
                    }
                }

                if(!minutesEditText.getText().toString().equals(futureText))
                    minutesEditText.setText(futureText);

                minutesEditText.setSelection(minutesEditText.length());

            }
        });


        initDaysListener();

        //sonnerie
        addSonnerieButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragmentContainerView, AddSonnerieFragment.newInstance())
                    .commit();
        });

        //save
        saveButton.setOnClickListener(v -> {
            if(saveVerif()){

                getDataView();

                if(isModif){
                    AddAlarmHelper.modifAlarm(currentAlarm, requireContext());
                }
                else{
                    AddAlarmHelper.addAlarm(currentAlarm, requireContext());
                }

                Trie.actualiser();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, new MainFragment())
                        .commit();

            }
        });
        }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);
        init(view);

        return view;

    }

    private void getDataView(){
        currentAlarm.alarmName = alarmNameEditText.getText().toString();

        currentAlarm.hours = Integer.parseInt(hoursEditText.getText().toString());
        currentAlarm.minute = Integer.parseInt(minutesEditText.getText().toString());

        currentAlarm.active = true;

        currentAlarm.vibreur = vibrateCheckBox.isChecked();
        currentAlarm.week = AddAlarmHelper.isWeek(currentAlarm);
        currentAlarm.monday = monday.isChecked();
        currentAlarm.tuesday = monday.isChecked();
        currentAlarm.wednesday = monday.isChecked();
        currentAlarm.thursday = monday.isChecked();
        currentAlarm.friday = monday.isChecked();
        currentAlarm.saturday = monday.isChecked();
        currentAlarm.sunday = monday.isChecked();

        currentAlarm.jourSonnerieText = AddAlarmHelper.daysActives(currentAlarm, getString(R.string.all_days_text), getResources().getStringArray(R.array.days_in_week_text));
    }

    private void initDaysListener(){
        monday.setOnClickListener(v -> {
            currentAlarm.monday = !currentAlarm.monday;
            monday.setChecked(currentAlarm.monday);
        });

        tuesday.setOnClickListener(v -> {
            currentAlarm.tuesday = !currentAlarm.tuesday;
            tuesday.setChecked(currentAlarm.tuesday);
        });

        wednesday.setOnClickListener(v -> {
            currentAlarm.wednesday = !currentAlarm.wednesday;
            wednesday.setChecked(currentAlarm.wednesday);
        });

        thursday.setOnClickListener(v -> {
            currentAlarm.thursday = !currentAlarm.thursday;
            thursday.setChecked(currentAlarm.thursday);
        });

        friday.setOnClickListener(v -> {
            currentAlarm.friday = !currentAlarm.friday;
            friday.setChecked(currentAlarm.friday);
        });

        saturday.setOnClickListener(v -> {
            currentAlarm.saturday = !currentAlarm.saturday;
            saturday.setChecked(currentAlarm.saturday);
        });

        sunday.setOnClickListener(v -> {
            currentAlarm.sunday = !currentAlarm.sunday;
            sunday.setChecked(currentAlarm.sunday);
        });
    }

    private boolean saveVerif(){
        if(hoursEditText.length() != 2 && minutesEditText.length() != 2){
            Toast.makeText(getContext(), "Heure de l'alarme invalide", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!currentAlarm.silence && currentAlarm.uriSonnerie == null){
            Toast.makeText(getContext(), "Veuillez choisir un sonnerie", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void init(View view){
        returnButton = view.findViewById(R.id.returnButton);

        alarmNameEditText = view.findViewById(R.id.alarmNameEditText);
        hoursEditText = view.findViewById(R.id.hoursEditText);
        minutesEditText = view.findViewById(R.id.minutesEditText);

        monday = view.findViewById(R.id.radioButton);
        tuesday = view.findViewById(R.id.radioButton2);
        wednesday = view.findViewById(R.id.radioButton3);
        thursday = view.findViewById(R.id.radioButton4);
        friday = view.findViewById(R.id.radioButton5);
        saturday = view.findViewById(R.id.radioButton6);
        sunday = view.findViewById(R.id.radioButton7);

        addSonnerieButton = view.findViewById(R.id.sonnerieButton);
        vibrateCheckBox = view.findViewById(R.id.vibrationCheckBox);

        saveButton = view.findViewById(R.id.saveButton);

    }

    private void modifAlarmHelper(){
        alarmNameEditText.setText(currentAlarm.alarmName);
        hoursEditText.setText(String.valueOf(currentAlarm.hours));
        minutesEditText.setText(String.valueOf(currentAlarm.minute));
        monday.setChecked(currentAlarm.monday);
        tuesday.setChecked(currentAlarm.tuesday);
        wednesday.setChecked(currentAlarm.wednesday);
        thursday.setChecked(currentAlarm.thursday);
        friday.setChecked(currentAlarm.friday);
        saturday.setChecked(currentAlarm.saturday);
        sunday.setChecked(currentAlarm.sunday);

        vibrateCheckBox.setChecked(currentAlarm.vibreur);
    }

}