package fr.wiiznokes.horloge11.utils;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.wiiznokes.horloge11.app.MainActivity;

public class Trie {


    public static void MapIdDate(){
        Map<Long, Calendar> MapIdDate = new HashMap<>();
        for(Alarm Alarm : MainActivity.MapIdAlarm.values()){
            MapIdDate.put(Alarm.id, dateProchaineSonnerie(Alarm));
        }
        MainActivity.MapIdDate = MapIdDate;
    }


    public static Calendar dateProchaineSonnerie(Alarm Alarme){
        //def heuure actuelle
        Calendar heureSysteme = Calendar.getInstance();
        long heureSys = System.currentTimeMillis();
        heureSysteme.setTimeInMillis(heureSys);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(heureSys);
        c.set(Calendar.HOUR_OF_DAY, Alarme.hours);
        c.set(Calendar.MINUTE, Alarme.minute);
        c.set(Calendar.SECOND, 0);

        if (Alarme.week){
            //si alarm est avant heure actuelle
            if(heureSysteme.compareTo(c) > 0){
                c.add(Calendar.DATE, 1);
            }
            return c;
        }
        else{
            List<Integer>daysActifs = new ArrayList<>();
            if(Alarme.sunday){
                daysActifs.add(1);
            }
            if(Alarme.monday){
                daysActifs.add(2);
            }
            if(Alarme.tuesday){
                daysActifs.add(3);
            }
            if(Alarme.wednesday){
                daysActifs.add(4);
            }
            if(Alarme.thursday){
                daysActifs.add(5);
            }
            if(Alarme.friday){
                daysActifs.add(6);
            }
            if(Alarme.saturday){
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


    public static void ListActifInit(){

        List<Long> ListActifInit = new ArrayList<>();

        for (Alarm Alarme : MainActivity.MapIdAlarm.values()){
            if(Alarme.active){
                Calendar dateSonnerie = MainActivity.MapIdDate.get(Alarme.id);
                if(ListActifInit.size() == 0){
                    ListActifInit.add(Alarme.id);
                }
                else{
                    int i = 0;
                    boolean insertionChek = false;
                    while (i < ListActifInit.size() && !insertionChek){
                        if(dateSonnerie.compareTo(MainActivity.MapIdDate.get(ListActifInit.get(i))) < 0){
                            ListActifInit.add(i, Alarme.id);
                            insertionChek = true;
                        }
                        i = i + 1;
                    }
                    if(!insertionChek) {
                        ListActifInit.add(Alarme.id);
                    }
                }
            }
        }
        MainActivity.ListActif = ListActifInit;
    }

    public static void ListInactifInit(){

        List<Long> ListInactifInit = new ArrayList<>();

        for (Alarm Alarme : MainActivity.MapIdAlarm.values()){
            if(!Alarme.active){
                Calendar dateSonnerie = MainActivity.MapIdDate.get(Alarme.id);
                if(ListInactifInit.size() == 0){
                    ListInactifInit.add(Alarme.id);
                }
                else{
                    int i = 0;
                    boolean insertionChek = false;
                    while (i < ListInactifInit.size() && !insertionChek){
                        if(dateSonnerie.compareTo(MainActivity.MapIdDate.get(ListInactifInit.get(i))) < 0){
                            ListInactifInit.add(i, Alarme.id);
                            insertionChek = true;
                        }
                        i = i + 1;
                    }
                    if(!insertionChek) {
                        ListInactifInit.add(Alarme.id);
                    }
                }
            }
        }
        MainActivity.ListInactif = ListInactifInit;
    }



    public static void ListSortId(){
        MainActivity.ListSortId = Stream.concat(MainActivity.ListActif.stream(), MainActivity.ListInactif.stream()).collect(Collectors.toList());
    }







    //inserer un id au bon endroit dans la ListTrie des alarmes actives
    public static void ListActifChange(Long id){
        Calendar dateSonnerie = MainActivity.MapIdDate.get(id);
        int i = 0;
        //condition pour savoir si ListActif est vide
        if(MainActivity.ListActif.size() == 0){
            MainActivity.ListActif.add(id);
        }
        else{
            boolean insertionChek = false;
            while (i < MainActivity.ListActif.size() && !insertionChek){
                //si la date de sonnerie de l'alarme est avant la date de sonnerie de l'alarm à l'index i de ListActif
                //-> ajouter id de l'alarme à l'index i dans ListActif
                if(dateSonnerie.compareTo(MainActivity.MapIdDate.get(MainActivity.ListActif.get(i))) < 0){
                    MainActivity.ListActif.add(i, id);
                    insertionChek = true;
                }
                else{
                    i = i + 1;
                }

            }
            //ajouter à la fin de ListActif si l'insertion n'a pas pu se faire
            if(!insertionChek) {
                MainActivity.ListActif.add(id);
            }
        }
    }
    //inserer un id au bon endroit dans la ListTrie des alarmes Inactives
    public static void ListInactifChange(Long id){
        Calendar dateSonnerie = MainActivity.MapIdDate.get(id);
        int i = 0;
        //condition pour savoir si ListInactif est vide
        if(MainActivity.ListInactif.size() == 0){
            MainActivity.ListInactif.add(id);
        }
        else{
            boolean insertionChek = false;
            while (i < MainActivity.ListInactif.size() && !insertionChek){
                if(dateSonnerie.compareTo(MainActivity.MapIdDate.get(MainActivity.ListInactif.get(i))) < 0){
                    MainActivity.ListInactif.add(i, id);
                    insertionChek = true;
                }
                else {
                    i = i + 1;
                }
            }
            if(!insertionChek) {
                MainActivity.ListInactif.add(id);
            }
        }
    }


    public static void ListItems(){

        ArrayList<Alarm> ListSortAlarm = new ArrayList<>();
        for(Long id : MainActivity.ListSortId){
            ListSortAlarm.add(MainActivity.MapIdAlarm.get(id));
        }

        MainActivity.items = ListSortAlarm;
    }
}
