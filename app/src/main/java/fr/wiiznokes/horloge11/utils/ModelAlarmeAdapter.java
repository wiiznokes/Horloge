package fr.wiiznokes.horloge11.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.media.midi.MidiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;

public class ModelAlarmeAdapter extends ArrayAdapter<Alarm> {





    public Context context;

    public InteractHelper interactHelper;

    public ArrayList<Alarm> list;

    public ListView listView;





    public ModelAlarmeAdapter(Context context, ArrayList<Alarm> items, TextView textViewTempsRestant, TextView textViewAlarmeActive, ListView listView){

        super(context, R.layout.alarme_affichage, items);

        this.context = context;
        this.list = items;

        this.listView = listView;

        this.interactHelper = new InteractHelper(textViewTempsRestant, textViewAlarmeActive, context);


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


            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

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
                interactHelper.switchHelper(currentAlarm);

                MainActivity.items = Trie.ListItems(MainActivity.ListSortId, MainActivity.MapIdAlarm);
                this.notifyDataSetChanged();


            });

            //item Listener
            convertView.setOnLongClickListener(v -> {
                AlertDialog.Builder popUp = new AlertDialog.Builder(context);

                popUp.setPositiveButton("MODIFIER", ((dialog, which) -> {
                    Toast.makeText(context, "modifié", Toast.LENGTH_SHORT).show();
                }));


                popUp.setNegativeButton("EFFACER", (dialog, which) -> {

                    interactHelper.effacer(currentAlarm);

                    MainActivity.items = Trie.ListItems(MainActivity.ListSortId, MainActivity.MapIdAlarm);
                    this.notifyDataSetChanged();

                    Toast.makeText(context, "effacé", Toast.LENGTH_SHORT).show();


                });

                popUp.show();
                return true;
            });




        }
        return convertView;
    }

}
