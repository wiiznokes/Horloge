package fr.wiiznokes.horloge11.temp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;



import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.storage.Alarm;

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

    private Button buttonAddSonnerie;
    private CheckBox vibrate;




    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);











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

            currentAlarm.vibreur = vibrate.isChecked();

            finish();
        });

    }

}