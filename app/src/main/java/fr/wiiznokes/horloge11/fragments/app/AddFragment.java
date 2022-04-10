package fr.wiiznokes.horloge11.fragments.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import java.util.Objects;
import java.util.Random;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.fragments.helperFrag.AddSonnerieFragment;
import fr.wiiznokes.horloge11.utils.storage.AddAlarmHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.StorageUtils;


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


    private static boolean isModif = false;

    public AddFragment() {
        // Required empty public constructor
    }

    public static AlarmFragment newInstance(boolean modifSouce, Alarm alarm) {
        AlarmFragment fragment = new AlarmFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        isModif = modifSouce;
        currentAlarm = alarm;

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
            currentAlarm = new Alarm();
            currentAlarm.id = new Random().nextLong();
        }

        returnButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        alarmNameEditText.requestFocus();

        alarmHoursEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                alarmHoursHelper();
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
                    .replace(R.id.fragmentContainerView, AddSonnerieFragment.newInstance("addAlarm", currentAlarm))
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
                AddAlarmHelper.replace(currentAlarm);
            }
            else{
                AddAlarmHelper.add(currentAlarm);
            }

            MainActivity.MapIdAlarm.put(currentAlarm.id, currentAlarm);
            StorageUtils.writeObject(requireContext(), MainActivity.MapIdAlarm, StorageUtils.alarmsFile);

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

    private void alarmHoursHelper(){

        //recuperation du texte sous type String
        String alarmHoursTxt = alarmHoursEditText.getText().toString();

        //si suppression de ":", remise à 0
        if (!(alarmHoursTxt.contains(":"))) {
            alarmHoursEditText.setText(":");
        }
        else{
            //remise a 0 si taille depasse 5
            if (alarmHoursTxt.length() > 5) {
                alarmHoursEditText.setText(":");
            }

            //remplacement de la premiere case par 0 quand elle est pas égale a 1 ou 2 et quand il y a au moins un caractère avant ":"
            if (alarmHoursTxt.indexOf(":") > 0) {
                //verif
                for (String number:numberList) {
                    if (alarmHoursTxt.substring(0, 1).contains(number)) {
                        alarmHoursEditText.setText("0" + alarmHoursTxt.substring(0, 1) + ":" + alarmHoursTxt.substring(alarmHoursTxt.indexOf(":") + 1, alarmHoursTxt.length()));

                        //mettre la selection à la fin
                        alarmHoursTxt = alarmHoursEditText.getText().toString();
                        alarmHoursEditText.setSelection(alarmHoursTxt.length());
                    }
                }
            }



            //heure < 23
            if (alarmHoursTxt.indexOf(":") > 0) {
                if (Integer.parseInt(alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":"))) > 23){
                    alarmHoursEditText.setText("2:"+alarmHoursTxt.substring(alarmHoursTxt.indexOf(":")+1, alarmHoursTxt.length()));
                    //mettre selection avant :
                    alarmHoursTxt = alarmHoursEditText.getText().toString();
                    alarmHoursEditText.setSelection(alarmHoursTxt.indexOf(":"));
                }
            }
            //condition si des minutes sont écrites
            if(alarmHoursTxt.length() - alarmHoursTxt.indexOf(":") -1 > 0){
                //chiffre des dizaine < 5
                if (Integer.parseInt(alarmHoursTxt.substring(alarmHoursTxt.indexOf(":")+1, alarmHoursTxt.indexOf(":")+2)) > 5){
                    alarmHoursEditText.setText(alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":")) + ":");
                    //mettre selection après ":"
                    alarmHoursTxt = alarmHoursEditText.getText().toString();
                    alarmHoursEditText.setSelection(alarmHoursTxt.indexOf(":")+1);
                }
            }

            //si nombre de minutes > 2 chiffres, supprime dernier chiffre
            if(alarmHoursTxt.length() - alarmHoursTxt.indexOf(":") -1 > 2) {
                alarmHoursEditText.setText(alarmHoursTxt.substring(0, alarmHoursTxt.length() - 1));
            }

            //selcetion à la fin si heure = 2 chiffres et si changement
            if(alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":")).length() == 2 && nbChiffreDesHeures < 2){
                alarmHoursEditText.setSelection(alarmHoursTxt.length());
            }
            nbChiffreDesHeures = alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":")).length();
        }
    }
}