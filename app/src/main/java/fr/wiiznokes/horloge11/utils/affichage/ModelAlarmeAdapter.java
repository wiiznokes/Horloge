package fr.wiiznokes.horloge11.utils.affichage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.utils.interact.InteractHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;

public class ModelAlarmeAdapter extends ArrayAdapter<Alarm> {

    public Context context;
    public InteractHelper interactHelper;

    public static ArrayList<Alarm> list;

    public ListView listView;

    public ModelAlarmeAdapter(Context context, ArrayList<Alarm> items, TextView activeAlarmTextView, TextView timeLeftTextView, ListView listView){
        super(context, R.layout.alarme_affichage, items);
        this.context = context;
        list = items;
        this.listView = listView;
        this.interactHelper = new InteractHelper(context, activeAlarmTextView, timeLeftTextView);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Alarm getItem(int index) {
        return list.get(index);
    }

    @Override
    public long getItemId(int index) {
        return list.get(index).id;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.alarme_affichage, null);
            Alarm currentAlarm = getItem(position);

            TextView hours = convertView.findViewById(R.id.hours);
            hours.setText(currentAlarm.hoursText);
            SwitchMaterial switch1 = convertView.findViewById(R.id.switch1);
            switch1.setChecked(currentAlarm.active);
            TextView alarmName = convertView.findViewById(R.id.alarmeName);
            alarmName.setText(currentAlarm.alarmName);
            TextView joursSonneries = convertView.findViewById(R.id.jours);
            joursSonneries.setText(currentAlarm.jourSonnerieText);

            //switch listener
            switch1.setOnClickListener(v -> {
                interactHelper.switchHelper(currentAlarm);

            });

            //item Listener
            convertView.setOnLongClickListener(v -> {
                AlertDialog.Builder popUp = new AlertDialog.Builder(context);
                popUp.setPositiveButton("MODIFIER", ((dialog, which) -> {
                    interactHelper.modifier(currentAlarm);
                }));

                popUp.setNegativeButton("EFFACER", (dialog, which) -> {

                    interactHelper.effacer(currentAlarm);
                    Toast.makeText(context, "effacé", Toast.LENGTH_SHORT).show();
                });
                popUp.show();
                return true;
            });
        }
        return convertView;
    }
}
