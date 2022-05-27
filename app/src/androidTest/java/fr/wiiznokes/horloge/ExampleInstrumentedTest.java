package fr.wiiznokes.horloge;

import android.app.AlarmManager;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import fr.wiiznokes.horloge.utils.notif.alert.AlertHelper;
import fr.wiiznokes.horloge.utils.storage.Alarm;
import fr.wiiznokes.horloge.utils.storage.Trie;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
    AlarmManager.AlarmClockInfo alarmClockInfo;

    Alarm alarm1 = newAlarm(11,31);
    Alarm alarm2 = newAlarm(5,56);

    long time1 = Trie.dateProchaineSonnerie(alarm1).getTimeInMillis();
    long time2 = Trie.dateProchaineSonnerie(alarm2).getTimeInMillis();


    @Test
    public void testNull(){
        alarmClockInfo = alarmManager.getNextAlarmClock();
        assertNull(alarmClockInfo);
    }

    @Test
    public void alarmeAjout(){
        //ajout
        AlertHelper.add(alarm1, appContext, time1);
        //recuperation derniere alarme
        alarmClockInfo = alarmManager.getNextAlarmClock();

        assertEquals(alarmClockInfo.getTriggerTime(), time1);
    }

    @Test
    public void doubleAlarmesIdentiques(){
        AlertHelper.add(alarm1, appContext, time1);
        AlertHelper.add(alarm2, appContext, time1);

        AlertHelper.remove(alarm2, appContext);
        //derniere alarme
        alarmClockInfo = alarmManager.getNextAlarmClock();
        assertEquals(alarmClockInfo.getTriggerTime(), time1);

        AlertHelper.remove(alarm1, appContext);
        alarmClockInfo = alarmManager.getNextAlarmClock();
        assertNull(alarmClockInfo);
    }

    @Test
    public void suppression(){
        AlertHelper.add(alarm1, appContext, time1);
        AlertHelper.add(alarm2, appContext, time2);

        AlertHelper.remove(alarm2, appContext);

        //derniere alarme
        alarmClockInfo = alarmManager.getNextAlarmClock();

        assertEquals(alarmClockInfo.getTriggerTime(), time1);
    }


    private Alarm newAlarm(int heure, int minute){
        Alarm currentAlarm = new Alarm();
        currentAlarm.id = new Random().nextLong();
        currentAlarm.hours = heure;
        currentAlarm.minute = minute;
        return currentAlarm;
    }

}