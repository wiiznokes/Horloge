package fr.wiiznokes.horloge11.utils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Trie {


    public Map<Long, Calendar> MapIdDate(Map<Long, Alarm> MapIdAlarm){
        Map<Long, Calendar> MapIdDate = new HashMap<>();
        for(Alarm Alarm : MapIdAlarm.values()){
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


    public List<Long> ListActifInit(Map<Long, Alarm> MapIdAlarm, Map<Long, Calendar> MapIdDate){

        List<Long> ListActifInit = new ArrayList<>();

        for (Alarm Alarme : MapIdAlarm.values()){
            if(Alarme.isActive()){
                Calendar dateSonnerie = MapIdDate.get(Alarme.getId());
                if(ListActifInit.size() == 0){
                    ListActifInit.add(Alarme.getId());
                }
                else{
                    int i = 0;
                    boolean insertionChek = false;
                    while (i < ListActifInit.size() && !insertionChek){
                        if(dateSonnerie.compareTo(MapIdDate.get(ListActifInit.get(i))) < 0){
                            ListActifInit.add(i, Alarme.getId());
                            insertionChek = true;
                        }
                        i = i + 1;
                    }
                    if(!insertionChek) {
                        ListActifInit.add(Alarme.getId());
                    }
                }
            }
        }
        return ListActifInit;
    }

    public List<Long> ListInactifInit(Map<Long, Alarm> MapIdAlarm, Map<Long, Calendar> MapIdDate){

        List<Long> ListInactifInit = new ArrayList<>();

        for (Alarm Alarme : MapIdAlarm.values()){
            if(!Alarme.isActive()){
                Calendar dateSonnerie = MapIdDate.get(Alarme.getId());
                if(ListInactifInit.size() == 0){
                    ListInactifInit.add(Alarme.getId());
                }
                else{
                    int i = 0;
                    boolean insertionChek = false;
                    while (i < ListInactifInit.size() && !insertionChek){
                        if(dateSonnerie.compareTo(MapIdDate.get(ListInactifInit.get(i))) < 0){
                            ListInactifInit.add(i, Alarme.getId());
                            insertionChek = true;
                        }
                        i = i + 1;
                    }
                    if(!insertionChek) {
                        ListInactifInit.add(Alarme.getId());
                    }
                }
            }
        }
        return ListInactifInit;
    }



    public List<Long> ListSortId(List<Long> ListActif, List<Long> ListInactif){
        return Stream.concat(ListActif.stream(), ListInactif.stream()).collect(Collectors.toList());
    }







    //inserer un id au bon endroit dans la ListTrie des alarmes actives
    public void ListActifChange(List<Long> ListActif, Long id, Map<Long, Calendar> MapIdDate){
        Calendar dateSonnerie = MapIdDate.get(id);
        int i = 0;
        //condition pour savoir si ListActif est vide
        if(ListActif.size() == 0){
            ListActif.add(id);
        }
        else{
            boolean insertionChek = false;
            while (i < ListActif.size() && !insertionChek){
                //si la date de sonnerie de l'alarme est avant la date de sonnerie de l'alarm à l'index i de ListActif
                //-> ajouter id de l'alarme à l'index i dans ListActif
                if(dateSonnerie.compareTo(MapIdDate.get(ListActif.get(i))) < 0){
                    ListActif.add(i, id);
                    insertionChek = true;
                }
                else{
                    i = i + 1;
                }

            }
            //ajouter à la fin de ListActif si l'insertion n'a pas pu se faire
            if(!insertionChek) {
                ListActif.add(id);
            }
        }
    }
    //inserer un id au bon endroit dans la ListTrie des alarmes Inactives
    public void ListInactifChange(List<Long> ListInactif, Long id, Map<Long, Calendar> MapIdDate){
        Calendar dateSonnerie = MapIdDate.get(id);
        int i = 0;
        //condition pour savoir si ListInactif est vide
        if(ListInactif.size() == 0){
            ListInactif.add(id);
        }
        else{
            boolean insertionChek = false;
            while (i < ListInactif.size() && !insertionChek){
                if(dateSonnerie.compareTo(MapIdDate.get(ListInactif.get(i))) < 0){
                    ListInactif.add(i, id);
                    insertionChek = true;
                }
                else {
                    i = i + 1;
                }
            }
            if(!insertionChek) {
                ListInactif.add(id);
            }
        }
    }


    public ArrayList<Alarm> ListSortAlarm(List<Long> ListSortId, Map<Long, Alarm> MapIdAlarm){

        ArrayList<Alarm> ListSortAlarm = new ArrayList<>();
        for(Long id : ListSortId){
            ListSortAlarm.add(MapIdAlarm.get(id));
        }

        return ListSortAlarm;
    }
}
