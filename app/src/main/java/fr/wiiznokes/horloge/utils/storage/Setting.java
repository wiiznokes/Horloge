package fr.wiiznokes.horloge.utils.storage;

import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Setting implements Serializable {
    public Setting(){

    }

    public Uri defaultUri = null;
    public boolean silence = true;
    public List<String> ringNameHistory = new ArrayList<>();
    public List<Uri> uriHistory = new ArrayList<>();

    public long timeSnooze = 240000;
    public boolean increaseGradually = false;
    public long timeRing = 240000;
    public boolean buttonToSnooze = true;

    public int themeApp = 0;
    public boolean notif = false;

}
