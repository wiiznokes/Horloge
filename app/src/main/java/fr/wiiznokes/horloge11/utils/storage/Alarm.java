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
    public boolean vibreur = false;
    public boolean silence = false;


    public boolean week = true;
    public boolean monday = false;
    public boolean tuesday = false;
    public boolean wednesday = false;
    public boolean thursday = false;
    public boolean friday = false;
    public boolean saturday = false;
    public boolean sunday = false;


    public long id;


    public String jourSonnerieText;

}
