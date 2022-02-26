package fr.wiiznokes.horloge11.utils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class Trie {

    public Dictionary<Integer, Integer> DicIdPos (List<Object> Array1){
        Dictionary<Integer, Integer> DicIdPos = new Hashtable<Integer, Integer>();
        int i = 0;
        for(Object alarm : Array1){
            Alarm Alarm = (Alarm) alarm;
            DicIdPos.put(Alarm.getId(), i);
        }
        return DicIdPos;
    }
    public Dictionary<Integer, Calendar> DicIdDate(List<Object> Array1){
        Dictionary<Integer, Calendar> DicIdDate = new Hashtable<Integer, Calendar>();
        for(Object alarm : Array1){
            Alarm Alarm = (Alarm) alarm;
            DicIdDate.put(Alarm.getId(), getCalendar(Alarm));
        }
        return DicIdDate;
    }

    public Calendar getCalendar(Alarm Alarme){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Alarme.getHours());
        c.set(Calendar.MINUTE, Alarme.getMinute());
        c.set(Calendar.SECOND, 0);
        return  c;
    }

    public Calendar dateProchaineSonnerie(Alarm Alarme){
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if (Alarme.isWeek()){
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, Alarme.getHours());
            c.set(Calendar.MINUTE, Alarme.getMinute());
            c.set(Calendar.SECOND, 0);
            if(Calendar.getInstance().compareTo(c) < 0){
                return c;
            }
            else {
                c.add(Calendar.DATE, 1);
                return c;
            }
        }
        else{
            List<Integer>daysActifs = new ArrayList<Integer>();
            if(Alarme.isSunday()){
                daysActifs.add(1);
            }
            if(Alarme.isMonday()){
                daysActifs.add(2);
            }
            if(Alarme.isTuesday()){
                daysActifs.add(3);
            }
            if(Alarme.isWednesday()){
                daysActifs.add(4);
            }
            if(Alarme.isThursday()){
                daysActifs.add(5);
            }
            if(Alarme.isFriday()){
                daysActifs.add(6);
            }
            if(Alarme.isSaturday()){
                daysActifs.add(7);
            }
            int jourSonnerie = 9;
            for(int jour : daysActifs){
                if(jour == dayOfWeek){
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.HOUR_OF_DAY, Alarme.getHours());
                    c.set(Calendar.MINUTE, Alarme.getMinute());
                    c.set(Calendar.SECOND, 0);
                    if (Calendar.getInstance().compareTo(c) < 0){
                        return c;
                    }
                }
                if(jour > dayOfWeek && jour < jourSonnerie){
                    jourSonnerie = jour;
                }
            }
            if(jourSonnerie == 9){
                for(int jour : daysActifs){
                    if(jour < dayOfWeek && jour < jourSonnerie){
                        jourSonnerie = jour;
                    }
                }

            }
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, Alarme.getHours());
            c.set(Calendar.MINUTE, Alarme.getMinute());
            c.set(Calendar.SECOND, 0);
            c.add(Calendar.DATE, 7 - dayOfWeek + jourSonnerie);

            return c;

        }

    }
/*
    public List<Integer> ListActifInit(List<Object> Array1, Dictionary<Integer, Calendar> DicIdDate){
        Calendar date = Calendar.getInstance()
        List<Integer> ListActifNonTrie = new ArrayList<Integer>();
        for(Object alarm : Array1){
            Alarm Alarm = (Alarm) alarm;
            List
        }
        return ListActifInit;

    }*/



}
