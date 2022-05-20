package fr.wiiznokes.horloge11.utils.addAlarmHelper;

import static fr.wiiznokes.horloge11.app.MainActivity.ListActif;
import static fr.wiiznokes.horloge11.app.MainActivity.ListInactif;
import static fr.wiiznokes.horloge11.app.MainActivity.ListSortId;
import static fr.wiiznokes.horloge11.app.MainActivity.MapIdAlarm;
import static fr.wiiznokes.horloge11.app.MainActivity.MapIdDate;
import static fr.wiiznokes.horloge11.app.MainActivity.items;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import fr.wiiznokes.horloge11.utils.alert.AlertHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;
import fr.wiiznokes.horloge11.utils.storage.StorageUtils;
import fr.wiiznokes.horloge11.utils.storage.Trie;

public class AddAlarmHelper{




    public static Bundle alarmHoursHelper(String currentText, int previousHoursLenght){


        String futurHeure = "";
        String futurMinute = "";
        int futurSelection = 0;


        if ((currentText.contains(":"))){

            //init
            futurHeure = currentText.substring(0, currentText.indexOf(':'));
            futurMinute = currentText.substring(currentText.indexOf(':') + 1);
            if(futurHeure.length() > 2)
                futurHeure = currentText.substring(0,2);
            if(futurMinute.length() > 2)
                futurMinute = currentText.substring(currentText.indexOf(':') + 1);



            if(futurHeure.length() > 0){
                //9h -> 09h
                if(Integer.parseInt(futurHeure) < 10 && Integer.parseInt(futurHeure) > 2){
                    futurHeure = '0' + futurHeure;
                    futurSelection = 3;
                }

                //heure < 23
                if(Integer.parseInt(futurHeure) > 23) {
                        futurHeure = "2";
                        //mettre selection avant :
                        futurSelection = 1;
                    }
                }

            //condition si des minutes sont écrites
            if(futurMinute.length() > 0){
                //minute = "9" -> ""          "77" -> ""
                if((Integer.parseInt(futurMinute) < 10 && Integer.parseInt(futurMinute) > 5) || Integer.parseInt(futurMinute) > 59){
                    futurMinute = "";
                    futurSelection = futurMinute.length() + 1;
                }
            }
        }

        //selection a la fin si les heures passe a deux
        if(previousHoursLenght < 2 && futurHeure.length() == 2){
            futurSelection = 3;
        }


        Bundle bundle = new Bundle();
        bundle.putString("futurText", futurHeure + ":" + futurMinute);
        bundle.putInt("futurSelection", futurSelection);
        bundle.putInt("previousHoursLenght", futurHeure.length());

        return bundle;
    }

    public static boolean isWeek(Alarm currentAlarm){
        return (currentAlarm.monday && currentAlarm.tuesday && currentAlarm.wednesday && currentAlarm.thursday && currentAlarm.friday && currentAlarm.saturday && currentAlarm.sunday)
                || (!currentAlarm.monday && !currentAlarm.tuesday && !currentAlarm.wednesday && !currentAlarm.thursday && !currentAlarm.friday && !currentAlarm.saturday && !currentAlarm.sunday);
    }

    public static String daysActives(Alarm currentAlarm, String allDaysText, String[] daysInWeekArray){
        String text = "";
        if(currentAlarm.week){
            text = allDaysText;
        }
        else {
            if(currentAlarm.monday){text = daysInWeekArray[0] + " ";}
            if(currentAlarm.tuesday){text = text + daysInWeekArray[1] + " ";}
            if(currentAlarm.wednesday){text = text + daysInWeekArray[2] + " ";}
            if(currentAlarm.thursday){text = text + daysInWeekArray[3] + " ";}
            if(currentAlarm.friday){text = text + daysInWeekArray[4] + " ";}
            if(currentAlarm.saturday){text = text + daysInWeekArray[5] + " ";}
            if(currentAlarm.sunday){text = text + daysInWeekArray[6] + " ";}
        }
        return text;
    }


    public static void addAlarm(Alarm currentAlarm, Context context) {

        MapIdAlarm.put(currentAlarm.id, currentAlarm);
        MapIdDate.put(currentAlarm.id, Trie.dateProchaineSonnerie(currentAlarm));

        Trie.ListActifChange(currentAlarm.id);
        Trie.ListSortId();



        items.add(ListSortId.indexOf(currentAlarm.id), currentAlarm);

        //ajout Alarm a AlarmManger
        AlertHelper.add(currentAlarm, context);

        StorageUtils.writeObject(context, MapIdAlarm, StorageUtils.alarmsFile);
        Toast.makeText(context, "ajouté", Toast.LENGTH_SHORT).show();

    }

    public static void modifAlarm(Alarm currentAlarm, Context context) {

        items.remove(MapIdAlarm.get(currentAlarm.id));

        MapIdAlarm.put(currentAlarm.id, currentAlarm);
        MapIdDate.put(currentAlarm.id, Trie.dateProchaineSonnerie(currentAlarm));

        ListActif.remove(currentAlarm.id);
        ListInactif.remove(currentAlarm.id);
        Trie.ListActifChange(currentAlarm.id);
        Trie.ListSortId();

        items.add(ListSortId.indexOf(currentAlarm.id), currentAlarm);

        //AlarmManger
        AlertHelper.remove(currentAlarm, context);
        AlertHelper.add(currentAlarm, context);

        StorageUtils.writeObject(context, MapIdAlarm, StorageUtils.alarmsFile);
        Toast.makeText(context, "modifié", Toast.LENGTH_SHORT).show();
    }
}
