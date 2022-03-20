package fr.wiiznokes.horloge11.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import java.util.Map;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fr.wiiznokes.horloge11.app.MainActivity;


public class StorageUtils {

    private static final String fileName = "save.txt";


    public static void write(Context context, Map<Long, Alarm> MapIdAlarm){

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


    public static int read(Context context){
        Map<Long, Alarm> MapIdAlarm;
        try {
            FileInputStream input = context.openFileInput(fileName);
            ObjectInputStream in = new ObjectInputStream(input);
            MapIdAlarm = (Map<Long, Alarm>) in.readObject();
            in.close();
            input.close();
            MainActivity.MapIdAlarm = MapIdAlarm;
            return 0;
        } catch (Exception e) {
            System.out.println("erreur dans la lecture");
            return 1;
        }

    }
}
