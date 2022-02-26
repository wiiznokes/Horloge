package fr.wiiznokes.horloge11.utils;

import java.io.Serializable;

public class Alarm implements Serializable {

    public Alarm() {

    }





    private String nameAlarm;
    public String getNameAlarm() {
        return nameAlarm;
    }
    public void setNameAlarm(String nameAlarm) {
        this.nameAlarm = nameAlarm;
    }




    private String hoursText;
    public String getHoursText() {
        return hoursText;
    }
    public void setHoursText(String hoursText) {
        this.hoursText = hoursText;
    }

    private int hours;
    public int getHours() {
        return hours;
    }
    public void setHours(int hours) {
        this.hours = hours;
    }

    private int minute;
    public int getMinute() {
        return minute;
    }
    public void setMinute(int minute) {
        this.minute = minute;
    }

    private int days;
    public int getDays() {
        return days;
    }
    public void setDays(int days) {
        this.days = days;
    }

    private int month;
    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    private int years;
    public int getYears() {
        return years;
    }
    public void setYears(int years) {
        this.years = years;
    }




    private boolean active;
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }



    private String sonnerie;
    public String getSonnerie() {
        return sonnerie;
    }
    public void setSonnerie(String sonnerie) {
        this.sonnerie = sonnerie;
    }



    private boolean week;
    public boolean isWeek() {
        return week;
    }
    public void setWeek(boolean week) {
        this.week = week;
    }

    private boolean monday;
    public boolean isMonday() {
        return monday;
    }
    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    private boolean tuesday;
    public boolean isTuesday() {
        return tuesday;
    }
    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    private boolean wednesday;
    public boolean isWednesday() {
        return wednesday;
    }
    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    private boolean thursday;
    public boolean isThursday() {
        return thursday;
    }
    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    private boolean friday;
    public boolean isFriday() {
        return friday;
    }
    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    private boolean saturday;
    public boolean isSaturday() {
        return saturday;
    }
    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    private boolean sunday;
    public boolean isSunday() {
        return sunday;
    }
    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }


    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }




}
