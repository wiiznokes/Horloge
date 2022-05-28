package fr.wiiznokes.horloge.utils.storage;


import android.net.Uri;

import java.io.Serializable;

public class Alarm implements Serializable{

    public Alarm() {

    }

    public String alarmName;


    public int hours;
    public int minute;

    public boolean active = true;

    //sonnerie
    //get and setter because Uri does not implement serializable
    private String uri = null;
    public Uri getUri(){ if(uri != null) return Uri.parse(uri); else return null; }
    public void setUri(Uri uri){ this.uri = uri.toString(); }

    public boolean vibreur = false;

    //0 -> default
    //1 -> silence
    //2 -> uriSonnerie
    public int type = 0;

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
