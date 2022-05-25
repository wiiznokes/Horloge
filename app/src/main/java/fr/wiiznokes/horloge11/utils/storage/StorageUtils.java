package fr.wiiznokes.horloge11.utils.storage;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class StorageUtils {

    public static final String alarmsFile = "alarms.txt";
    public static final String settingFile = "setting.txt";


    public static void writeObject(Context context, Object object, String fileName){
        try {
            FileOutputStream output = context.openFileOutput(fileName, MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(output);
            out.writeObject(object);
            out.close();
            output.close();
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    public static Object readObject(Context context, String fileName){
        Object object;
        try {
            FileInputStream input = context.openFileInput(fileName);
            ObjectInputStream in = new ObjectInputStream(input);
            object = in.readObject();
            in.close();
            input.close();
            return object;
        } catch (IOException | ClassNotFoundException ioException) {
            ioException.printStackTrace();
            return null;
        }
    }
}

