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

    private RadioButton monday;
    private RadioButton tuesday;
    private RadioButton wednesday;
    private RadioButton thursday;
    private RadioButton friday;
    private RadioButton saturday;
    private RadioButton sunday;

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

        initDaysListener();

        //sonnerie
        addSonnerieButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragmentContainerView, AddSonnerieFragment.newInstance(false, currentAlarm))
                    .commit();
        });

        //save
        saveButton.setOnClickListener(v -> {
            if(saveVerif()){
                currentAlarm.alarmName = alarmNameEditText.getText().toString();

                currentAlarm.hoursText = alarmHoursEditText.getText().toString();
                currentAlarm.hours = Integer.parseInt(alarmHoursEditText.getText().toString().substring(0, 2));
                currentAlarm.minute = Integer.parseInt(alarmHoursEditText.getText().toString().substring(3, 5));

                currentAlarm.week = AddAlarmHelper.isWeek(currentAlarm);
                currentAlarm.jourSonnerieText = AddAlarmHelper.daysActives(currentAlarm, getString(R.string.all_days_text), getResources().getStringArray(R.array.days_in_week_text));

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
        if(alarmHoursEditText.length() != 5){
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