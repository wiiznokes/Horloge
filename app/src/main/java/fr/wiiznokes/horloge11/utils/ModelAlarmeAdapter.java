package fr.wiiznokes.horloge11.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;

public class ModelAlarmeAdapter extends ArrayAdapter<Alarm> {





    public MainActivity mainActivity;


    public InteractHelper interactHelper;

    public ArrayList<Alarm> list;





    public ModelAlarmeAdapter(Context context, ArrayList<Alarm> items){

        super(context, R.layout.alarme_affichage, items);

        this.mainActivity = (MainActivity) context;

        this.list = items;
        this.interactHelper = new InteractHelper(mainActivity.textViewTempsRestant, mainActivity.textViewAlarmeActive);


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
        return list.get(index).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //We must create a View:


            LayoutInflater layoutInflater = (LayoutInflater) mainActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.alarme_affichage, null);

            Alarm currentAlarm = getItem(position);

            TextView hours = convertView.findViewById(R.id.hours);
            hours.setText(currentAlarm.getHoursText());
            SwitchMaterial switch1 = convertView.findViewById(R.id.switch1);
            switch1.setChecked(currentAlarm.isActive());
            TextView alarmName = convertView.findViewById(R.id.alarmeName);
            alarmName.setText(currentAlarm.getNameAlarm());
            TextView joursSonneries = convertView.findViewById(R.id.jours);
            joursSonneries.setText(currentAlarm.getJourSonnerieText());

            //switch listener
            switch1.setOnClickListener(v -> {
                interactHelper.switchHelper(currentAlarm, mainActivity);
                notifyDataSetChanged();
            });

            //item Listener
            convertView.setOnLongClickListener(v -> {
                AlertDialog.Builder popUp = new AlertDialog.Builder(mainActivity);

                popUp.setPositiveButton("MODIFIER", ((dialog, which) -> {
                    Toast.makeText(mainActivity, "modifié", Toast.LENGTH_SHORT).show();
                }));


                popUp.setNegativeButton("EFFACER", (dialog, which) -> {

                    interactHelper.effacer(idAlarm, mainActivity);
                    Toast.makeText(mainActivity, "effacé", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                });

                popUp.show();
                return true;
            });




        }
        return convertView;
    }

}
