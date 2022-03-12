package fr.wiiznokes.horloge11.utils;

public class ModelAlarme {

    private String alarmName;
    private String alarmHours;
    private String jourSonnerie;

    public void init(Alarm Alarme){

        alarmName = Alarme.getNameAlarm();
        alarmHours = Alarme.getHoursText();
        jourSonnerie = Alarme.getSonnerie();
    }
}
