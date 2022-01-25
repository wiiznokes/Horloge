package fr.wiiznokes.horloge11.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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

    private String[] numberList = {"3","4","5","6","7","8","9"};

    private EditText alarmName;
    private EditText alarmHours;
    private RadioButton radioMonday;

    int nbChiffreDesHeures = 0;
    StringBuilder alarmHoursTxtCopy = new StringBuilder(":");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //lecture et recuperation du dernier objet Alarm du fichier
        List<Object> Array1 = read(fileName);
        Alarm AlarmNew = (Alarm) Array1.get(Array1.size()-1);

        //nom de l'alarme
        this.alarmName = findViewById(R.id.textView12);
        alarmName.setText(AlarmNew.getNameAlarm());

        //heure de l'alarme

        this.alarmHours = findViewById(R.id.textView13);
        this.radioMonday = findViewById(R.id.radioButton);


        alarmHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //recuperation du texte
                String alarmHoursTxt = alarmHours.getText().toString();



                //remise a zero si taille depasse 5
                if (alarmHoursTxt.length() > 5) {
                    alarmHours.setText(":");
                }
                //

                //remplacement de la premiere case par 0 quand elle est pas égale a 1 ou 2 et quand il y a au moins un caractère avant ":"
                if (alarmHoursTxt.indexOf(":") > 0) {
                    //verif
                    for (String number:numberList) {
                        if (alarmHoursTxt.substring(0, 1).contains(number)) {
                            //
                            alarmHours.setText("0" + alarmHoursTxt.substring(0, 1) + ":" + alarmHoursTxt.substring(alarmHoursTxt.indexOf(":") + 1, alarmHoursTxt.length()));

                            //mettre la selection à la fin
                            alarmHoursTxt = alarmHours.getText().toString();
                            alarmHours.setSelection(alarmHoursTxt.length());
                            break;
                        }
                    }
                }


                //si suppression de ":"
                if (!(alarmHoursTxt.contains(":"))){
                    //on remet le texte precedent en enlevant le caractère avant les :
                    alarmHours.setText(alarmHoursTxtCopy.deleteCharAt(alarmHoursTxtCopy.indexOf(":") - 1));
                    alarmHours.setSelection(alarmHoursTxtCopy.indexOf(":") - 1);
                }
                //condition pour savoir si une heure est écrite
                if (alarmHoursTxt.indexOf(":") > 0) {
                //heure > 23
                if (Integer.parseInt(alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":"))) > 23){
                    alarmHours.setText("2:"+alarmHoursTxt.substring(alarmHoursTxt.indexOf(":")+1, alarmHoursTxt.length()));
                    //mettre selection avant :
                    alarmHoursTxt = alarmHours.getText().toString();
                    alarmHours.setSelection(alarmHoursTxt.indexOf(":"));
                }}

                //condition si des minutes sont écrites
                if(alarmHoursTxt.length() - alarmHoursTxt.indexOf(":") -1 > 0){
                //minute
                //chiffre des dizaine > 5
                if (Integer.parseInt(alarmHoursTxt.substring(alarmHoursTxt.indexOf(":")+1, alarmHoursTxt.indexOf(":")+2)) > 5){
                    alarmHours.setText(alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":")) + ":");
                    //mettre selection après ":"
                    alarmHoursTxt = alarmHours.getText().toString();
                    alarmHours.setSelection(alarmHoursTxt.indexOf(":")+1);
                }}

                //si nombre de minutes > 2 chiffres, supprime dernier chiffre
                if(alarmHoursTxt.length() - alarmHoursTxt.indexOf(":") -1 > 2) {
                    alarmHours.setText(alarmHoursTxt.substring(0, alarmHoursTxt.length() - 1));

                }
                //selcetion à la fin dès le nombre de chiffre des heures = 2
                if(alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":")).length() == 2 && nbChiffreDesHeures < 2){
                    alarmHours.setSelection(alarmHoursTxt.length());
                }

                nbChiffreDesHeures = alarmHoursTxt.substring(0, alarmHoursTxt.indexOf(":")).length();
                alarmHoursTxtCopy.replace(0, alarmHoursTxtCopy.length(), alarmHours.getText().toString());

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