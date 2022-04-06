package fr.wiiznokes.horloge11.utils.storage;


import java.io.Serializable;

public class Alarm implements Serializable{

    public Alarm() {

    }

    public String alarmName;


    public int hours;
    public int minute;

    public boolean active = true;

    //sonnerie
    public String uriSonnerie = null;
    public boolean vibreur;
    public boolean silence = false;


    public boolean week;
    public boolean monday;
    public boolean tuesday;
    public boolean wednesday;
    public boolean thursday;
    public boolean friday;
    public boolean saturday;
    public boolean sunday;


    public long id;

    public String hoursText;
    public String jourSonnerieText;

}
