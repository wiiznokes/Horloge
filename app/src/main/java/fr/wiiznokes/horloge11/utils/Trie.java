package fr.wiiznokes.horloge11.utils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Trie {

    public Map<Integer, Integer> MapIdPos (List<Alarm> Array1){
        Map<Integer, Integer> MapIdPos = new HashMap<>();
        int i = 0;
        for(Alarm Alarm : Array1){
            MapIdPos.put(Alarm.getId(), i);
            i = i + 1;
        }
        return MapIdPos;
    }
    public Map<Integer, Calendar> MapIdDate(List<Alarm> Array1){
        Map<Integer, Calendar> MapIdDate = new HashMap<>();
        for(Alarm Alarm : Array1){
            MapIdDate.put(Alarm.getId(), dateProchaineSonnerie(Alarm));
        }
        return MapIdDate;
    }


    public Calendar dateProchaineSonnerie(Alarm Alarme){
        //def heuure actuelle
        Calendar heureSysteme = Calendar.getInstance();
        long heureSys = System.currentTimeMillis();
        heureSysteme.setTimeInMillis(heureSys);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(heureSys);
        c.set(Calendar.HOUR_OF_DAY, Alarme.getHours());
        c.set(Calendar.MINUTE, Alarme.getMinute());
        c.set(Calendar.SECOND, 0);

        if (Alarme.isWeek()){
            //si alarm est avant heure actuelle
            if(heureSysteme.compareTo(c) > 0){
                c.add(Calendar.DATE, 1);
            }
            return c;
        }
        else{
            List<Integer>daysActifs = new ArrayList<>();
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
            int dayOfWeek = heureSysteme.get(Calendar.DAY_OF_WEEK);
            int jourSonnerie = 9;
            for(int jour : daysActifs){
                //si le jour est ajd
                if(jour == dayOfWeek){
                    //si c est apres
                    if (heureSysteme.compareTo(c) < 0){
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
            if(jourSonnerie == dayOfWeek){
                c.add(Calendar.DATE, 7);
            }
            if(jourSonnerie > dayOfWeek){
                c.add(Calendar.DATE, jourSonnerie - dayOfWeek);

            }
            if(jourSonnerie < dayOfWeek){
                c.add(Calendar.DATE, dayOfWeek - jourSonnerie);
            }
            return c;

        }
    }


    public List<Integer> ListActifInit(List<Alarm> Array1, Map<Integer, Calendar> MapIdDate){

        List<Integer> ListActifInit = new ArrayList<>();

        for (Alarm Alarme : Array1){
            if(Alarme.isActive()){
                Calendar dateAlarm = MapIdDate.get(Alarme.getId());
                if(ListActifInit.size() == 0){
                    ListActifInit.add(Alarme.getId());
                }
                else{
                    int i = 0;
                    boolean insertionChek = true;
                    while (i < ListActifInit.size() && insertionChek){
                        if(dateAlarm.compareTo(MapIdDate.get(ListActifInit.get(i))) < 0){
                            ListActifInit.add(i, Alarme.getId());
                            insertionChek = false;
                        }
                        i = i + 1;
                    }
                    if(insertionChek) {
                        ListActifInit.add(Alarme.getId());
                    }
                }
            }
        }
        return ListActifInit;
    }

    public List<Integer> ListInactifInit(List<Alarm> Array1, Map<Integer, Calendar> MapIdDate){

        List<Integer> ListInactifInit = new ArrayList<>();

        for (Alarm Alarme : Array1){
            if(!Alarme.isActive()){
                Calendar dateAlarm = MapIdDate.get(Alarme.getId());
                if(ListInactifInit.size() == 0){
                    ListInactifInit.add(Alarme.getId());
                }
                else{
                    int i = 0;
                    boolean insertionChek = true;
                    while (i < ListInactifInit.size() && insertionChek){
                        if(dateAlarm.compareTo(MapIdDate.get(ListInactifInit.get(i))) < 0){
                            ListInactifInit.add(i, Alarme.getId());
                            insertionChek = false;
                        }
                        i = i + 1;
                    }
                    if(insertionChek) {
                        ListInactifInit.add(Alarme.getId());
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
    public void ListActifChange(List<Integer> ListActif, int id, Map<Integer, Calendar> MapIdDate){
        Calendar dateSonnerie = MapIdDate.get(id);
        //condition pour savoir si ListActif est vide
        if(ListActif.size() == 0){
            ListActif.add(id);
        }
        else{
            int i = 0;
            boolean insertionChek = true;
            while (i < ListActif.size() && insertionChek){
                if(dateSonnerie.compareTo(MapIdDate.get(ListActif.get(i))) > 0){
                    ListActif.add(i, id);
                    insertionChek = false;
                }
                i = i + 1;
            }
            if(insertionChek) {
                ListActif.add(id);
            }
        }
    }
    //inserer un id au bon endroit dans la ListTrie des alarmes Inactives
    public void ListInactifChange(List<Integer> ListInactif, int id, Map<Integer, Calendar> MapIdDate){
        Calendar dateSonnerie = MapIdDate.get(id);
        //condition pour savoir si ListInactif est vide
        if(ListInactif.size() == 0){
            ListInactif.add(id);
        }
        else{
            int i = 0;
            boolean insertionChek = true;
            while (i < ListInactif.size() && insertionChek){
                if(dateSonnerie.compareTo(MapIdDate.get(ListInactif.get(i))) > 0){
                    ListInactif.add(i, id);
                    insertionChek = false;
                }
                i = i + 1;
            }
            if(insertionChek) {
                ListInactif.add(id);
            }
        }
    }



}
