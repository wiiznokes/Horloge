package fr.wiiznokes.horloge11.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;


public class StorageUtils {

    public static void afficher(String[][] tab){
        for(int i=0; i<tab.length; i++){
            for(int j=0; j<tab[i].length; j++){
                System.out.println("Array["+i+"]["+j+"]="+tab[i][j]);
            }
        };
    }
    public void write(String fileName, List<Alarm> tab, Context context){

        try {
            FileOutputStream output = context.openFileOutput(fileName, MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(tab);
            out.close();
            output.close();
        } catch (Exception e) {
            System.out.println("erreur dans l'écriture");
        }
    }


    public List<Alarm> read(String fileName, Context context){
        List<Alarm> Array1;
        try {
            FileInputStream input = context.openFileInput(fileName);
            ObjectInputStream in = new ObjectInputStream(input);
            Array1 = (List<Alarm>) in.readObject();
            in.close();
            input.close();
            return Array1;
        } catch (Exception e) {
            System.out.println("erreur dans la lecture");
        }
        return null;
    }


}
