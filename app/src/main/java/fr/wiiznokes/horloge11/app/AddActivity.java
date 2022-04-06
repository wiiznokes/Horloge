package fr.wiiznokes.horloge11.app;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;



import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.affichage.Affichage;
import fr.wiiznokes.horloge11.utils.alert.AlertHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.StorageUtils;

public class AddActivity extends AppCompatActivity {

    public static Alarm currentAlarm;


    private EditText alarmName;

    private EditText alarmHours;
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




    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        currentAlarm = new Alarm();

        //bouton retour
        ImageButton returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            setResult(11);
            finish();
        });


        //nom de l'alarme
        this.alarmName = findViewById(R.id.alarmNameEditText);
        alarmName.requestFocus();

        //heure de l'alarme
        this.alarmHours = findViewById(R.id.alarmHoursEditText);

        alarmHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                alarmHoursHelper();
        }
        });

        monday = findViewById(R.id.radioButton);
        monday.setChecked(false);
        monday.setOnClickListener(v -> {
            mondayState = !mondayState;
            monday.setChecked(mondayState);
        });

        tuesday = findViewById(R.id.radioButton2);
        tuesday.setChecked(false);
        tuesday.setOnClickListener(v -> {
            tuesdayState = !tuesdayState;
            tuesday.setChecked(tuesdayState);
        });

        wednesday = findViewById(R.id.radioButton3);
        wednesday.setChecked(false);
        wednesday.setOnClickListener(v -> {
            wednesdayState = !wednesdayState;
            wednesday.setChecked(wednesdayState);
        });

        thursday = findViewById(R.id.radioButton4);
        thursday.setChecked(false);
        thursday.setOnClickListener(v -> {
            thursdayState = !thursdayState;
            thursday.setChecked(thursdayState);
        });

        friday = findViewById(R.id.radioButton5);
        friday.setChecked(false);
        friday.setOnClickListener(v -> {
            fridayState = !fridayState;
            friday.setChecked(fridayState);
        });

        saturday = findViewById(R.id.radioButton6);
        saturday.setChecked(false);
        saturday.setOnClickListener(v -> {
            saturdayState = !saturdayState;
            saturday.setChecked(saturdayState);
        });

        sunday = findViewById(R.id.radioButton7);
        sunday.setChecked(false);
        sunday.setOnClickListener(v -> {
            sundayState = !sundayState;
            sunday.setChecked(sundayState);
        });

        //sonnerie
        Button buttonAddSonnerie = findViewById(R.id.sonnerieButton);
        buttonAddSonnerie.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this, addSonnerieActivity.class);
            intent.putExtra("currentAlarm", currentAlarm);
            startActivity(intent);
        });






        ImageButton save = findViewById(R.id.saveButton);
        save.setOnClickListener(v -> {

            if(alarmHours.length() != 5){
                Toast.makeText(this, "Heure de l'alarme invalide", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!currentAlarm.silence && currentAlarm.uriSonnerie == null){
                Toast.makeText(this, "Veuillez choisir un sonnerie", Toast.LENGTH_SHORT).show();
                return;
            }


            currentAlarm.alarmName = alarmName.getText().toString();

            currentAlarm.hoursText = alarmHours.getText().toString();
            currentAlarm.hours = Integer.parseInt(alarmHours.getText().toString().substring(0, 2));
            currentAlarm.minute = Integer.parseInt(alarmHours.getText().toString().substring(3, 5));

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

            currentAlarm.vibreur = ((CheckBox)findViewById(R.id.vibrationCheckBox)).isChecked();

            //set de l'id de l'alarm
            currentAlarm.id = new Random().nextLong();

            setResult(0);
            finish();
        });

    }




    private void alarmHoursHelper(){

        //recuperation du texte sous type String
        String alarmHoursTxt = alarmHours.getText().toString();

        //si suppression de ":", remise à 0
        if (!(alarmHoursTxt.contains(":"))) {
            alarmHours.setText(":");
        }
        else{
            //remise a 0 si taille depasse 5
            if (alarmHoursTxt.length() > 5) {
                alarmHours.setText(":");
            }

            //remplacement de la premiere case par 0 quand elle est pas égale a 1 ou 2 et quand il y a au moins un caractère avant ":"
            if (alarmHoursTxt.indexOf(":") > 0) {
                //verif
                for (String number:numberList) {
                    if (alarmHoursTxt.substring(0, 1).contains(number)) {
                        alarmHours.setText("0" + alarmHoursTxt.substring(0, 1) + ":" + alarmHoursTxt.substring(alarmHoursTxt.indexOf(":") + 1, alarmHoursTxt.length()));

                        //mettre la selection à la fin
                        alarmHoursTxt = alarmHours.getText().toString();
                        alarmHours.setSelection(alarmHoursTxt.length());
                    }
                }
            }



            //heure < 23
            if (alarmHoursTxt.indexOf(":") > 0) {
                if (Integer.parseInt(alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":"))) > 23){
                    alarmHours.setText("2:"+alarmHoursTxt.substring(alarmHoursTxt.indexOf(":")+1, alarmHoursTxt.length()));
                    //mettre selection avant :
                    alarmHoursTxt = alarmHours.getText().toString();
                    alarmHours.setSelection(alarmHoursTxt.indexOf(":"));
                }
            }
            //condition si des minutes sont écrites
            if(alarmHoursTxt.length() - alarmHoursTxt.indexOf(":") -1 > 0){
                //chiffre des dizaine < 5
                if (Integer.parseInt(alarmHoursTxt.substring(alarmHoursTxt.indexOf(":")+1, alarmHoursTxt.indexOf(":")+2)) > 5){
                    alarmHours.setText(alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":")) + ":");
                    //mettre selection après ":"
                    alarmHoursTxt = alarmHours.getText().toString();
                    alarmHours.setSelection(alarmHoursTxt.indexOf(":")+1);
                }
            }

            //si nombre de minutes > 2 chiffres, supprime dernier chiffre
            if(alarmHoursTxt.length() - alarmHoursTxt.indexOf(":") -1 > 2) {
                alarmHours.setText(alarmHoursTxt.substring(0, alarmHoursTxt.length() - 1));
            }

            //selcetion à la fin si heure = 2 chiffres et si changement
            if(alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":")).length() == 2 && nbChiffreDesHeures < 2){
                alarmHours.setSelection(alarmHoursTxt.length());
            }
            nbChiffreDesHeures = alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":")).length();
        }
    }
}