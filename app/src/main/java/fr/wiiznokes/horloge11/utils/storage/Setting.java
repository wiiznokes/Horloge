package fr.wiiznokes.horloge11.utils.storage;

import java.io.Serializable;

public class Setting implements Serializable {
    public Setting(){

    }

    public static String defaultRing;
    public static boolean silence = false;
    public static int increaseGradually = 0;
    public static int timeRing = 300;
    public static boolean buttonToSnooze = true;

    public static int themeApp = 0;
    public static boolean notif = false;

}
