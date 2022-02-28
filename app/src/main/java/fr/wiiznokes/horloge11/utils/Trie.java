package fr.wiiznokes.horloge11.utils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Trie {

    public Map<Integer, Integer> MapIdPos (List<Alarm> Array1){
        Map<Integer, Integer> MapIdPos = new Hashtable<Integer, Integer>();
        int i = 0;
        for(Alarm Alarm : Array1){
            MapIdPos.put(Alarm.getId(), i);
        }
        return MapIdPos;
    }
    public Map<Integer, Calendar> MapIdDate(List<Alarm> Array1){
        Map<Integer, Calendar> MapIdDate = new Hashtable<Integer, Calendar>();
        for(Alarm Alarm : Array1){
            MapIdDate.put(Alarm.getId(), dateProchaineSonnerie(Alarm));
        }
        return MapIdDate;
    }


    public Calendar dateProchaineSonnerie(Alarm Alarme){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, Alarme.getHours());
        c.set(Calendar.MINUTE, Alarme.getMinute());
        c.set(Calendar.SECOND, 0);

        if (Alarme.isWeek()){
            if(Calendar.getInstance().compareTo(c) < 0){
                c.add(Calendar.DATE, 1);
                return c;
            }
            else {
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
            int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            int jourSonnerie = 9;
            for(int jour : daysActifs){
                //si le jour est ajd
                if(jour == dayOfWeek){
                    if (Calendar.getInstance().compareTo(c) > 0){
                        return c;
                    }
                }
                else if(jour > dayOfWeek && jour < jourSonnerie){
                    jourSonnerie = jour;
                }
            }
            if(jourSonnerie == 9){
                for(int jour : daysActifs){
                    if(jour <= dayOfWeek && jour < jourSonnerie){
                        jourSonnerie = jour;
                    }
                }

            }
            c.add(Calendar.DATE, 7 - dayOfWeek + jourSonnerie);
            return c;
        }
    }


    public List<Integer> ListActifInit(List<Alarm> Array1, Map<Integer, Calendar> MapIdDate, Map<Integer, Integer> MapIdPos){

        List<Integer> ListActifInit = new ArrayList<Integer>();
        //boucle sur MapIdDate
        for(Map.Entry<Integer, Calendar> entry : MapIdDate.entrySet()){
            Integer id = entry.getKey();
            //condition pour savoir si l'alarm est active
            if(Array1.get(MapIdPos.get(id)).isActive()){
                Calendar dateAlarm = entry.getValue();
                //si la taille ListActifInit == 0, ajouter la key
                if (ListActifInit.size() == 0) {
                    ListActifInit.add(id);
                } else {
                    int i = 0;
                    boolean insertionChek = false;
                    //parcourir la ListActifInit
                    for (Integer idListTri : ListActifInit) {
                        //placer l'id de Map avant l'id de List si
                        if (dateAlarm.compareTo(MapIdDate.get(idListTri)) < 0) {
                            ListActifInit.add(i, id);
                            insertionChek = true;
                            break;

                        }
                        i = i + 1;
                    }
                    if(!insertionChek){
                        ListActifInit.add(id);
                    }
                }
            }
        }
        return ListActifInit;
    }
    public List<Integer> ListInactifInit(List<Alarm> Array1, Map<Integer, Calendar> MapIdDate, Map<Integer, Integer> MapIdPos){

        List<Integer> ListInactifInit = new ArrayList<Integer>();
        //boucle sur MapIdDate
        for(Map.Entry<Integer, Calendar> entry : MapIdDate.entrySet()){
            Integer id = entry.getKey();
            //condition pour savoir si l'alarm est inactive
            if(!Array1.get(MapIdPos.get(id)).isActive()){
                Calendar dateAlarm = entry.getValue();
                //si la taille ListActifInit == 0, ajouter la key
                if (ListInactifInit.size() == 0) {
                    ListInactifInit.add(id);
                } else {
                    int i = 0;
                    boolean insertionChek = false;
                    //parcourir la ListActifInit
                    for (Integer idListTri : ListInactifInit) {
                        //placer l'id de Map avant l'id de List si
                        if (dateAlarm.compareTo(MapIdDate.get(idListTri)) < 0) {
                            ListInactifInit.add(i, id);
                            insertionChek = true;
                            break;

                        }
                        i = i + 1;
                    }
                    if(!insertionChek){
                        ListInactifInit.add(id);
                    }
                }
            }
        }
        return ListInactifInit;
    }
    public List<Integer> ListSortId(List<Integer> ListActif, List<Integer> ListInactif){
        return Stream.concat(ListActif.stream(), ListInactif.stream()).collect(Collectors.toList());
    }







    //inserer un id au bon endroit dans la ListTrie des alarmes actives
    public List<Integer> ListActifChange(List<Integer> ListActif, int id, Map<Integer, Calendar> MapIdDate){
        Calendar dateSonnerie = MapIdDate.get(id);
        //condition pour savoir si ListActif est vide
        if(ListActif.size() == 0){
            ListActif.add(id);
            return ListActif;
        }
        else{
            int i = 0;
            for(int idListActif : ListActif){
                if(MapIdDate.get(idListActif).compareTo(dateSonnerie) > 0){
                    ListActif.add(i, id);
                    return ListActif;
                }
            }
            ListActif.add(id);
            return ListActif;
        }
    }
    //inserer un id au bon endroit dans la ListTrie des alarmes Inactives
    public List<Integer> ListInactifChange(List<Integer> ListInactif, int id, Map<Integer, Calendar> MapIdDate){
        Calendar dateSonnerie = MapIdDate.get(id);
        //condition pour savoir si ListInactif est vide
        if(ListInactif.size() == 0){
            ListInactif.add(id);
            return ListInactif;
        }
        else{
            int i = 0;
            for(int idListActif : ListInactif){
                if(MapIdDate.get(idListActif).compareTo(dateSonnerie) > 0){
                    ListInactif.add(i, id);
                    return ListInactif;
                }
            }
            ListInactif.add(id);
            return ListInactif;
        }
    }



}
