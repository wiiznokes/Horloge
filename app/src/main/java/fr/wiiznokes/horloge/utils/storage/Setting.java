package fr.wiiznokes.horloge.utils.storage;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Setting implements Serializable {
    public Setting(){

    }

    //get and setter because Uri does not implement serializable
    private String defaultUri = null;
    public Uri getDefaultUri(){ if (defaultUri != null) return Uri.parse(defaultUri); else return null; }
    public void setDefaultUri(Uri uri){ this.defaultUri = uri.toString(); }

    public boolean silence = true;
    public List<String> ringNameHistory = new ArrayList<>();
    public List<String> uriHistory = new ArrayList<>();

    public long timeSnooze = 240000;
    public boolean increaseGradually = false;
    public long timeRing = 240000;
    public boolean buttonToSnooze = true;

    public int themeApp = 0;
    public boolean notif = false;

}
