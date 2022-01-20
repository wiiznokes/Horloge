package fr.wiiznokes.horloge11.utils;

import java.io.Serializable;

public class Alarm implements Serializable {
    private String nameAlarm;

    public String getNameAlarm() {
        return nameAlarm;
    }

    public void setNameAlarm(String nameAlarm) {
        this.nameAlarm = nameAlarm;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSonnerie() {
        return sonnerie;
    }

    public void setSonnerie(String sonnerie) {
        this.sonnerie = sonnerie;
    }

    private int hours;
    private int minute;

    private int days;
    private int month;
    private int years;

    private boolean active;

    private String sonnerie;

    public Alarm() {

    }
}
