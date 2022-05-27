package fr.wiiznokes.horloge.utils.storage;

import java.io.Serializable;
import java.util.List;

public class Setting implements Serializable {
    public Setting(){

    }

    public String defaultUri = null;
    public List<String> uriHistory;
    public boolean silence = true;

    public long timeSnooze = 240000;
    public boolean increaseGradually = false;
    public long timeRing = 240000;
    public boolean buttonToSnooze = true;

    public int themeApp = 0;
    public boolean notif = false;

}
