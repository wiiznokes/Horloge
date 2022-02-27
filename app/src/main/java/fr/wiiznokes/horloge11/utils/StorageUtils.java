package fr.wiiznokes.horloge11.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;


public class StorageUtils {

    private final String fileNameId = "saveIdNumber.txt";
    private final String fileName = "save.txt";

    public void write(List<Alarm> tab, Context context){

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


    public List<Alarm> read(Context context){
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

    public int readAndIncId(Context context){
        int numberOfId;
        try {
            FileInputStream input = context.openFileInput(fileNameId);
            ObjectInputStream in = new ObjectInputStream(input);
            numberOfId = (int) in.readObject();

            in.close();
            input.close();
            incId(numberOfId, context);
            return numberOfId;
        } catch (Exception e) {
            System.out.println("erreur dans la lecture");
            return -1;
        }

    }
    public void incId(int numberOfId, Context context){

        try {
            FileOutputStream output = context.openFileOutput(fileNameId, MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(numberOfId+1);
            out.close();
            output.close();
        } catch (Exception e) {
            System.out.println("erreur dans l'écriture");
        }

    }


}
