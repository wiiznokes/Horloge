package fr.wiiznokes.horloge11.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import fr.wiiznokes.horloge11.R;
import fr.wiiznokes.horloge11.app.MainActivity;

public class ModelAlarmeAdapter extends BaseAdapter {

    private List<Alarm> ListSortAlarm;
    public void setListSortAlarm(List<Alarm> listSortAlarm) {
        ListSortAlarm = listSortAlarm;
    }

    private Context context;
    private LayoutInflater inflater;
    private int cheminLayout = R.layout.alarme_affichage;



    InteractHelper interactHelper;


    public ModelAlarmeAdapter(Context context, List<Alarm> ListSortAlarm, TextView textViewTempsRestant, TextView textViewAlarmeActive){
        this.context = context;
        this.ListSortAlarm = ListSortAlarm;
        this.inflater = LayoutInflater.from(context);
        interactHelper = new InteractHelper(textViewTempsRestant, textViewAlarmeActive);
    }

    @Override
    public int getCount() {
        return ListSortAlarm.size();
    }

    @Override
    public Alarm getItem(int position) {
        return ListSortAlarm.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ListSortAlarm.get(position).getId();
    }


    @Override
    public View getView(int id, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //We must create a View:
            convertView = inflater.inflate(cheminLayout, parent, false);

            Alarm currentAlarm = getItem(id);
            TextView heureAlarm = convertView.findViewById(R.id.textView);
            SwitchMaterial switchMaterial = convertView.findViewById(R.id.switch1);
            TextView alarmName = convertView.findViewById(R.id.textView3);
            TextView jourSonnerie = convertView.findViewById(R.id.textView5);

            heureAlarm.setText(currentAlarm.getHoursText());
            switchMaterial.setChecked(currentAlarm.isActive());
            alarmName.setText(currentAlarm.getNameAlarm());
            jourSonnerie.setText(currentAlarm.getJourSonnerieText());

            convertView.setId(id);

            switchMaterial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder popUp = new AlertDialog.Builder(context);
                    popUp.setNegativeButton("EFFACER", (dialog, which) -> {

                        interactHelper.effacer(id, context, listView,
                                MapIdAlarm, MapIdDate,
                                ListActif, ListInactif, ListSortId,
                                ListSortAlarm);
                        Toast.makeText(context, "effacé", Toast.LENGTH_SHORT).show()


                    });
                    popUp.setPositiveButton("MODIFIER", (dialog, which) -> Toast.makeText(context, "modifié", Toast.LENGTH_SHORT).show());
                    popUp.show();
                    return false;
                }
            });

        }



        return convertView;
    }
}
