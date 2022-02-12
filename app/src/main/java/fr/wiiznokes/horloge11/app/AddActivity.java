package fr.wiiznokes.horloge11.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Space;

import fr.wiiznokes.horloge11.utils.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.List;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.Alarm;

public class AddActivity extends AppCompatActivity {


    private final String fileName = "save.txt";

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
        boutonRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(11);
                finish();
            }
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

        monday = (RadioButton) findViewById(R.id.radioButton);
        monday.setChecked(false);
        monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mondayState = !mondayState;
                monday.setChecked(mondayState);
            }
        });

        tuesday = (RadioButton) findViewById(R.id.radioButton2);
        tuesday.setChecked(false);
        tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tuesdayState = !tuesdayState;
                tuesday.setChecked(tuesdayState);
            }
        });

        wednesday = (RadioButton) findViewById(R.id.radioButton3);
        wednesday.setChecked(false);
        wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wednesdayState = !wednesdayState;
                wednesday.setChecked(wednesdayState);
            }
        });

        thursday = (RadioButton) findViewById(R.id.radioButton4);
        thursday.setChecked(false);
        thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thursdayState = !thursdayState;
                thursday.setChecked(thursdayState);
            }
        });

        friday = (RadioButton) findViewById(R.id.radioButton5);
        friday.setChecked(false);
        friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fridayState = !fridayState;
                friday.setChecked(fridayState);
            }
        });

        saturday = (RadioButton) findViewById(R.id.radioButton6);
        saturday.setChecked(false);
        saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saturdayState = !saturdayState;
                saturday.setChecked(saturdayState);
            }
        });

        sunday = (RadioButton) findViewById(R.id.radioButton7);
        sunday.setChecked(false);
        sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sundayState = !sundayState;
                sunday.setChecked(sundayState);
            }
        });

        this.sonnerieName = (EditText) findViewById(R.id.editText29);
        save = (ImageButton) findViewById(R.id.floatingActionButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(alarmName.length() != 0 && alarmHours.length() == 5) {

                    //creation de l'object alarm
                    Alarm Alarm1 = new Alarm();

                    Alarm1.setNameAlarm(alarmName.getText().toString());

                    Alarm1.setActive(true);

                    Alarm1.setHoursText(alarmHours.getText().toString());
                    Alarm1.setHours(Integer.parseInt(alarmHours.getText().toString().substring(0, 2)));
                    Alarm1.setMinute(Integer.parseInt(alarmHours.getText().toString().substring(3, 5)));

                    if (mondayState || tuesdayState || wednesdayState || thursdayState || fridayState || saturdayState || sundayState) {
                        Alarm1.setMonday(mondayState);
                        Alarm1.setTuesday(tuesdayState);
                        Alarm1.setWednesday(wednesdayState);
                        Alarm1.setThursday(thursdayState);
                        Alarm1.setFriday(fridayState);
                        Alarm1.setSaturday(saturdayState);
                        Alarm1.setSunday(sundayState);
                        Alarm1.setWeek(false);
                    } else {
                        Alarm1.setWeek(true);
                    }

                    Alarm1.setSonnerie(sonnerieName.getText().toString());

                    //lecture du fichier de sauvegarde
                    List<Object> Array1 = read(fileName);
                    //ajout de l'objet Alarm à la liste
                    Array1.add(Alarm1);
                    //ecriture sur le fichier de sauvegarde
                    write(fileName, Array1);

                    setResult(0);
                    finish();


                }
            }
        });

    }







    public void write(String fileName, List<Object> tab){

        try {
            FileOutputStream output = this.openFileOutput(fileName, MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(tab);
            out.close();
            output.close();
        } catch (Exception e) {
            System.out.println("erreur dans l'écriture");
        }

    }

    public List<Object> read(String fileName){
        List<Object> Array1;
        try {
            FileInputStream input = this.openFileInput(fileName);
            ObjectInputStream in = new ObjectInputStream(input);
            Array1 = (List<Object>) in.readObject();
            in.close();
            input.close();
            return Array1;
        } catch (Exception e) {
            System.out.println("erreur dans la lecture");
        }
        return null;
    }
}