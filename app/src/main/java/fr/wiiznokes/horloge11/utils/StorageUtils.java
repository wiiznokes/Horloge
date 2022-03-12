package fr.wiiznokes.horloge11.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import java.util.Map;
import java.util.Map.Entry;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;


public class StorageUtils {

    private final String fileNameId = "saveIdNumber.txt";
    private final String fileName = "save.txt";

    public void write(Map<Integer, Alarm> MapIdAlarm, Context context){

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


    public Map<Integer, Alarm> read(Context context){
        Map<Integer, Alarm> MapIdAlarm;
        try {
            FileInputStream input = context.openFileInput(fileName);
            ObjectInputStream in = new ObjectInputStream(input);
            MapIdAlarm = (Map<Integer, Alarm>) in.readObject();
            in.close();
            input.close();
            return MapIdAlarm;
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
            //fichier Id introuvable
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
            System.out.println("erreur dans l'écriture de IntId");
        }

    }

    public static <K extends Parcelable, V extends Parcelable> void writeHashMap(
            Map<K, V> map, Parcel out, int flags) {
        if (map != null) {
            out.writeInt(map.size());

            for (Entry<K, V> entry : map.entrySet()) {
                out.writeParcelable(entry.getKey(), flags);
                out.writeParcelable(entry.getValue(), flags);
            }
        } else {
            out.writeInt(-1);
        }
    }


}
