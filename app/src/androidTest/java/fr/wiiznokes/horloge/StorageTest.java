package fr.wiiznokes.horloge;


import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.net.Uri;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import fr.wiiznokes.horloge.utils.storage.Alarm;
import fr.wiiznokes.horloge.utils.storage.Setting;
import fr.wiiznokes.horloge.utils.storage.StorageUtils;

@RunWith(AndroidJUnit4.class)
public class StorageTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void alarmStorageTest(){
        Alarm alarm = new Alarm();
        alarm.setUri(Uri.parse("hello"));
        alarm.alarmName = "alarm1";
        alarm.id = 22;

        StorageUtils.writeObject(appContext, alarm, StorageUtils.alarmsFile);
        Alarm readAlarm = (Alarm) StorageUtils.readObject(appContext, StorageUtils.alarmsFile);
        assertTrue(equalAlarmTest(readAlarm));
    }

    private boolean equalAlarmTest(Alarm readAlarm){
        if(readAlarm != null) {
            System.out.println(readAlarm.getUri());
            System.out.println(readAlarm.alarmName);
            System.out.println(readAlarm.id);

            return readAlarm.getUri().toString().equals("hello") && readAlarm.alarmName.equals("alarm1") && readAlarm.id == 22;
        }
        else
            System.out.println("readAlarm = null");
            return false;
    }

    @Test
    public void settingStorageTest(){
        Setting setting = new Setting();
        List<String> urisList = new ArrayList<>();
        urisList.add("hello");
        setting.uriHistory = urisList;

        StorageUtils.writeObject(appContext, setting, StorageUtils.settingFile);
        Setting readSetting = (Setting) StorageUtils.readObject(appContext, StorageUtils.settingFile);

        assertTrue(equalSettingTest(readSetting));
    }

    private boolean equalSettingTest(Setting readSetting){
        if(readSetting != null) {
            return true;
        }
        else
            System.out.println("readSetting = null");
        return false;
    }
}
