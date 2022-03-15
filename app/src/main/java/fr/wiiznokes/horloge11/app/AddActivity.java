package fr.wiiznokes.horloge11.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;



import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.Alarm;

public class AddActivity extends AppCompatActivity {





    private ImageButton boutonRetour;

    private EditText alarmName;

    private EditText alarmHours;
    private String[] numberList = {"3","4","5","6","7","8","9"};
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

    private EditText sonnerieName;

    private ImageButton save;




    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);



        //bouton retour
        this.boutonRetour = findViewById(R.id.imageButton4);
        boutonRetour.setOnClickListener(v -> {
            setResult(11);
            finish();
        });

        //nom de l'alarme
        this.alarmName = findViewById(R.id.textView12);
        alarmName.setText(this.getIntent().getExtras().getString("alarmName"));

        //heure de l'alarme
        this.alarmHours = findViewById(R.id.textView13);
        alarmHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
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
                                break;
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

        this.sonnerieName = findViewById(R.id.editText29);
        save = findViewById(R.id.floatingActionButton);
        save.setOnClickListener(v -> {

            //si l'alarm a un nom et une date
            if(alarmName.length() != 0 && alarmHours.length() == 5) {

                //creation de l'object alarm
                Alarm Alarm1 = new Alarm();

                Alarm1.setNameAlarm(alarmName.getText().toString());

                Alarm1.setActive(true);

                Alarm1.setHoursText(alarmHours.getText().toString());
                Alarm1.setHours(Integer.parseInt(alarmHours.getText().toString().substring(0, 2)));
                Alarm1.setMinute(Integer.parseInt(alarmHours.getText().toString().substring(3, 5)));

                String joursActif = "";
                if ((mondayState && tuesdayState && wednesdayState && thursdayState && fridayState && saturdayState && sundayState) ||
                        (!mondayState && !tuesdayState && !wednesdayState && !thursdayState && !fridayState && !saturdayState && !sundayState)){
                    Alarm1.setWeek(true);
                    joursActif = "Tous les jours";
                } else {
                    Alarm1.setMonday(mondayState);
                    joursActif = joursActif + "lun ";
                    Alarm1.setTuesday(tuesdayState);
                    joursActif = joursActif + "mar ";
                    Alarm1.setWednesday(wednesdayState);
                    joursActif = joursActif + "mer ";
                    Alarm1.setThursday(thursdayState);
                    joursActif = joursActif + "jeu ";
                    Alarm1.setFriday(fridayState);
                    joursActif = joursActif + "ven ";
                    Alarm1.setSaturday(saturdayState);
                    joursActif = joursActif + "sam ";
                    Alarm1.setSunday(sundayState);
                    joursActif = joursActif + "dim ";
                    Alarm1.setWeek(false);
                }
                Alarm1.setJourSonnerieText(joursActif);


                Alarm1.setSonnerie(sonnerieName.getText().toString());

                //set de l'id de l'alarm
                Alarm1.setId(new Random().nextLong());


                Intent resultIntent = new Intent();
                resultIntent.putExtra("AlarmeAdd", Alarm1);
                setResult(0, resultIntent);
                finish();
            }
        });

    }
}