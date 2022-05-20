package fr.wiiznokes.horloge11.fragments.app;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.fragments.helperFrag.AddSonnerieFragment;
import fr.wiiznokes.horloge11.utils.addAlarmHelper.AddAlarmHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;


public class AddFragment extends Fragment {

    public static Alarm currentAlarm;

    private ImageButton returnButton;

    private EditText alarmNameEditText;

    private EditText alarmHoursEditText;
    private final String[] numberList = {"3","4","5","6","7","8","9"};
    int nbChiffreDesHeures = 0;

    private RadioButton monday;
    private Boolean mondayState = false;
    private RadioButton tuesday;
    private Boolean tuesdayState = false;
    private RadioButton wednesday;
    private Boolean wednesdayState = false;
    private RadioButton thursday;
    private Boolean thursdayState = false;
    private RadioButton friday;
    private Boolean fridayState = false;
    private RadioButton saturday;
    private Boolean saturdayState = false;
    private RadioButton sunday;
    private Boolean sundayState = false;

    private Button addSonnerieButton;
    private CheckBox vibrateCheckBox;

    private ImageButton saveButton;


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

        alarmHoursEditText.addTextChangedListener(new TextWatcher() {
            Bundle bundle;
            int previousHoursLenght = 0;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                bundle = AddAlarmHelper.alarmHoursHelper(alarmHoursEditText.getText().toString(), previousHoursLenght);
                alarmHoursEditText.setText(bundle.getString("futurText"));
                alarmHoursEditText.setSelection(bundle.getInt("futurSelection"));
                previousHoursLenght = bundle.getInt("previousHoursLenght");
            }
        });

        monday.setOnClickListener(v -> {
            mondayState = !mondayState;
            monday.setChecked(mondayState);
        });

        tuesday.setOnClickListener(v -> {
            tuesdayState = !tuesdayState;
            tuesday.setChecked(tuesdayState);
        });

        wednesday.setOnClickListener(v -> {
            wednesdayState = !wednesdayState;
            wednesday.setChecked(wednesdayState);
        });

        thursday.setOnClickListener(v -> {
            thursdayState = !thursdayState;
            thursday.setChecked(thursdayState);
        });

        friday.setOnClickListener(v -> {
            fridayState = !fridayState;
            friday.setChecked(fridayState);
        });

        saturday.setOnClickListener(v -> {
            saturdayState = !saturdayState;
            saturday.setChecked(saturdayState);
        });

        sunday.setOnClickListener(v -> {
            sundayState = !sundayState;
            sunday.setChecked(sundayState);
        });

        //sonnerie
        addSonnerieButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragmentContainerView, AddSonnerieFragment.newInstance(false, currentAlarm))
                    .commit();
        });

        //save
        saveButton.setOnClickListener(v -> {
            if(alarmHoursEditText.length() != 5){
                Toast.makeText(getContext(), "Heure de l'alarme invalide", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!currentAlarm.silence && currentAlarm.uriSonnerie == null){
                Toast.makeText(getContext(), "Veuillez choisir un sonnerie", Toast.LENGTH_SHORT).show();
                return;
            }


            currentAlarm.alarmName = alarmNameEditText.getText().toString();

            currentAlarm.hoursText = alarmHoursEditText.getText().toString();
            currentAlarm.hours = Integer.parseInt(alarmHoursEditText.getText().toString().substring(0, 2));
            currentAlarm.minute = Integer.parseInt(alarmHoursEditText.getText().toString().substring(3, 5));

            //texte jours actifs
            String joursActif = "";
            if ((mondayState && tuesdayState && wednesdayState && thursdayState && fridayState && saturdayState && sundayState) ||
                    (!mondayState && !tuesdayState && !wednesdayState && !thursdayState && !fridayState && !saturdayState && !sundayState)){
                currentAlarm.week = true;
                joursActif = this.getString(R.string.all_days_text);
            } else {
                String[] daysInWeekArray = getResources().getStringArray(R.array.days_in_week_text);
                if(mondayState){joursActif = joursActif + daysInWeekArray[0] + " ";}
                if(tuesdayState){joursActif = joursActif + daysInWeekArray[1] + " ";}
                if(wednesdayState){joursActif = joursActif + daysInWeekArray[2] + " ";}
                if(thursdayState){joursActif = joursActif + daysInWeekArray[3] + " ";}
                if(fridayState){joursActif = joursActif + daysInWeekArray[4] + " ";}
                if(saturdayState){joursActif = joursActif + daysInWeekArray[5] + " ";}
                if(sundayState){joursActif = joursActif + daysInWeekArray[6] + " ";}
                currentAlarm.week = false;
            }
            currentAlarm.monday = mondayState;
            currentAlarm.tuesday = tuesdayState;
            currentAlarm.wednesday = wednesdayState;
            currentAlarm.thursday = thursdayState;
            currentAlarm.friday = fridayState;
            currentAlarm.saturday = saturdayState;
            currentAlarm.sunday = sundayState;
            currentAlarm.jourSonnerieText = joursActif;

            currentAlarm.vibreur = vibrateCheckBox.isChecked();

            if(isModif){
                AddAlarmHelper.modifAlarm(currentAlarm, requireContext());
            }
            else{
                AddAlarmHelper.addAlarm(currentAlarm, requireContext());
            }

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new MainFragment())
                    .commit();

        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);
        init(view);

        return view;

    }


    private void init(View view){
        returnButton = view.findViewById(R.id.returnButton);

        alarmNameEditText = view.findViewById(R.id.alarmNameEditText);
        alarmHoursEditText = view.findViewById(R.id.alarmHoursEditText);

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
        alarmHoursEditText.setText(currentAlarm.hoursText);
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