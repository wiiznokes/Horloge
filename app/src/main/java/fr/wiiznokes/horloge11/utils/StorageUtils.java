package fr.wiiznokes.horloge11.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import java.util.Map;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class StorageUtils {

    private static final String fileName = "save.txt";

    public static void write(Map<Long, Alarm> MapIdAlarm, Context context){

        try {
            FileOutputStream output = context.openFileOutput(fileName, MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(MapIdAlarm);
            out.close();
            output.close();
        } catch (Exception e) {
            System.out.println("erreur dans l'écriture");
        }
    }


    public static Map<Long, Alarm> read(Context context){
        Map<Long, Alarm> MapIdAlarm;
        try {
            FileInputStream input = context.openFileInput(fileName);
            ObjectInputStream in = new ObjectInputStream(input);
            MapIdAlarm = (Map<Long, Alarm>) in.readObject();
            in.close();
            input.close();
            return MapIdAlarm;
        } catch (Exception e) {
            System.out.println("erreur dans la lecture");
        }
        return null;
    }
}
