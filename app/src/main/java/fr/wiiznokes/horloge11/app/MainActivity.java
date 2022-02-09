package fr.wiiznokes.horloge11.app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.*;

public class MainActivity extends AppCompatActivity {

    private final String fileName = "save.txt";

    private ImageButton addAlarm;
    private EditText addAlarmText;
    private final static int ADD_ACTIVITY_CALL_ID = 10;
    private final static int defaultAjoutAlarmResult = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //creation du fichier si il n'existe pas avec un tableau vide
        if(read(fileName)== null){

            List<Object> ArrayInit = new ArrayList<Object>();
            //ecriture
            write(fileName, ArrayInit);
        }

        //lecture du fichier
        List<Object> Array1 = read(fileName);



        //recuperration des views pour ajouter une alarme
        this.addAlarm = findViewById(R.id.floatingActionButton4);
        this.addAlarmText = findViewById(R.id.editTextTextPersonName);


        addAlarm.setOnClickListener(view -> {
            //si l'edit text est deja visible et que l'on click sur le + et que l'on a donné une nom à l'alarme
            if (addAlarmText.getVisibility() == View.VISIBLE) {

                if(addAlarmText.length() != 0){



                    //lancement de AddActivity
                    //creation de l'intention à partir du context et du fichier .class à ouvrir
                    Intent intent = new Intent(
                            MainActivity.this,
                            AddActivity.class
                    );
                    //ajout d'information dans l'intention
                    intent.putExtra("alarmName", addAlarmText.getText().toString());
                    //lancement de addActivity avec un id de lancement
                    startActivityForResult(intent, ADD_ACTIVITY_CALL_ID);
                }

            }
            else{
                //edit text visible
                addAlarmText.setVisibility(View.VISIBLE);
                //demande du focus
                addAlarmText.requestFocus();
                //demande du clavier
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                //on regarde un changement de focus
                addAlarmText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        //si plus de focus
                        addAlarmText.setVisibility(View.INVISIBLE);
                        addAlarmText.setText("");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                    }
                }
            });
            }



        });



    }
    //recuperation resultat d'une activité lancée
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ACTIVITY_CALL_ID){
            addAlarmText.setVisibility(View.INVISIBLE);
        }
        if(requestCode == defaultAjoutAlarmResult){
            addAlarmText.setVisibility(View.INVISIBLE);
        }
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