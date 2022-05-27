package fr.wiiznokes.horloge.fragments.app;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.Random;

import fr.wiiznokes.horloge.app.MainActivity;
import fr.wiiznokes.horloge.fragments.helperFrag.AddSonnerieFragment;
import fr.wiiznokes.horloge.utils.addAlarmHelper.AddAlarmHelper;
import fr.wiiznokes.horloge.utils.notif.SoundHelper;
import fr.wiiznokes.horloge.utils.storage.Alarm;
import fr.wiiznokes.horloge.utils.storage.Trie;
import fr.wiiznokes.horloge.R;


public class AddFragment extends Fragment {



    public static Alarm currentAlarm;
    private static boolean isModif;
    //main UI
    private ImageButton returnButton;
    private ImageButton saveButton;

    //alarm UI
    private EditText alarmNameEditText;
    private EditText hoursEditText;
    private EditText minutesEditText;

    private Button addSonnerieButton;
    private CheckBox vibrateCheckBox;

    //play song test
    private SoundHelper soundHelper;
    private Button playButton;
    private TextView ringNameTextView;

    //days UI
    private RadioButton monday;
    private RadioButton tuesday;
    private RadioButton wednesday;
    private RadioButton thursday;
    private RadioButton friday;
    private RadioButton saturday;
    private RadioButton sunday;


    public AddFragment() {}

    public static AddFragment newInstance(boolean modif, @Nullable Alarm alarm) {
        AddFragment fragment = new AddFragment();
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

        //get data for ringTones
        getParentFragmentManager().setFragmentResultListener("data", this, (requestKey, bundle) -> {

            currentAlarm.type = bundle.getInt("type");
            if(currentAlarm.type == 2){
                String uri = bundle.getString("uri");
                if(uri.isEmpty())
                    currentAlarm.uriSonnerie = null;
                else
                    currentAlarm.uriSonnerie = uri;
            }
            //maj Media Player
            SoundHelper.setMediaPlayer(requireContext(), SoundHelper.uriAlarm(currentAlarm));

            //maj ring name
            ringNameTextView.setText(SoundHelper.ringName(currentAlarm));
        });

        soundHelper = new SoundHelper();
        SoundHelper.setSetting(MainActivity.setting);
        SoundHelper.setMediaPlayer(requireContext(), SoundHelper.uriAlarm(currentAlarm));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);
        //findViewById
        init(view);

        if(isModif){
            //set alarm attribute
            modifAlarmHelper();
        }
        else{
            currentAlarm.id = new Random().nextLong();
        }

        //listener
        returnButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        alarmHoursAI();
        initDaysListener();

        //add ring listener
        addSonnerieButton.setOnClickListener(v -> getParentFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentContainerView, AddSonnerieFragment.newInstance())
                .commit());

        //play song test
        ringNameTextView.setText(SoundHelper.ringName(currentAlarm));
        playButton.setOnClickListener(v -> soundHelper.playTest());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        alarmNameEditText.requestFocus();


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



    private void alarmHoursAI(){

        hoursEditText.addTextChangedListener(new TextWatcher() {
            String textBefore;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textBefore = hoursEditText.getText().toString();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
    }

    private boolean saveVerif(){
        if(hoursEditText.length() != 2 && minutesEditText.length() != 2){
            Toast.makeText(getContext(), "Heure de l'alarme invalide", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(currentAlarm.type == 2 && currentAlarm.uriSonnerie == null){
            Toast.makeText(getContext(), "Veuillez choisir un sonnerie", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
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

        //ring
        addSonnerieButton = view.findViewById(R.id.sonnerieButton);
        vibrateCheckBox = view.findViewById(R.id.vibrationCheckBox);
        playButton = view.findViewById(R.id.playButton);
        ringNameTextView = view.findViewById(R.id.ringNameTextView);

        saveButton = view.findViewById(R.id.saveButton);

    }

}