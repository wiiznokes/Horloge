package fr.wiiznokes.horloge11.utils.affichage;

import static fr.wiiznokes.horloge11.app.MainActivity.items;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;
import fr.wiiznokes.horloge11.utils.interact.InteractHelper;
import fr.wiiznokes.horloge11.utils.storage.Alarm;

public class ModelAlarmeAdapter extends ArrayAdapter<Alarm> {

    public MainActivity mainActivity;
    public InteractHelper interactHelper;


    public ModelAlarmeAdapter(MainActivity mainActivity, TextView activeAlarmTextView, TextView timeLeftTextView){
        super(mainActivity, R.layout.alarme_affichage, items);
        this.mainActivity = mainActivity;
        this.interactHelper = new InteractHelper(mainActivity, activeAlarmTextView, timeLeftTextView);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Alarm getItem(int index) {
        return items.get(index);
    }

    @Override
    public long getItemId(int index) {
        return items.get(index).id;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) mainActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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
            convertView.setOnLongClickListener((View v) -> {
                AlertDialog.Builder popUp = new AlertDialog.Builder(mainActivity);
                popUp.setPositiveButton("MODIFIER", ((dialog, which) -> {
                    interactHelper.modifier(currentAlarm, v);
                    Toast.makeText(mainActivity, "modifié", Toast.LENGTH_SHORT).show();
                }));

                popUp.setNegativeButton("EFFACER", (dialog, which) -> {
                    interactHelper.effacer(currentAlarm);
                    Toast.makeText(mainActivity, "effacé", Toast.LENGTH_SHORT).show();
                });
                popUp.show();
                return true;
            });
        }
        return convertView;
    }
}
