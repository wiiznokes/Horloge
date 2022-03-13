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

    //dictionnaire key:id valeur:Alarm
    private Map<Integer, Alarm> MapIdAlarm;
    //dictionnaire key:id valeur:dateSonnerie
    private Map<Integer, Calendar> MapIdDate;
    //liste id alarm actif triée
    private List<Integer> ListActif;
    //liste id alarm Inactif triée
    private List<Integer> ListInactif;
    //liste somme de ListActif et Listinactif
    private List<Integer> ListSortId;
    //liste des alarmes triée
    private List<Alarm> ListSortAlarm;
    //element interactif
    private TextView textViewTempsRestant;
    private TextView textViewAlarmeActive;
    private ListView listView;



    private Context context;
    private LayoutInflater inflater;
    private int cheminLayout = R.layout.alarme_affichage;
    private InteractHelper interactHelper;


    public void setMapIdAlarm(Map<Integer, Alarm> mapIdAlarm) {
        MapIdAlarm = mapIdAlarm;
    }

    public void setMapIdDate(Map<Integer, Calendar> mapIdDate) {
        MapIdDate = mapIdDate;
    }

    public void setListActif(List<Integer> listActif) {
        ListActif = listActif;
    }

    public void setListInactif(List<Integer> listInactif) {
        ListInactif = listInactif;
    }

    public void setListSortId(List<Integer> listSortId) {
        ListSortId = listSortId;
    }

    public void setListSortAlarm(List<Alarm> listSortAlarm) {
        ListSortAlarm = listSortAlarm;
    }


    public ModelAlarmeAdapter(Context context, ListView listView, List<Alarm> ListSortAlarm,
                              Map<Integer, Alarm> MapIdAlarm, Map<Integer, Calendar> MapIdDate,
                              List<Integer> ListActif, List<Integer> ListInactif, List<Integer> ListSortId,
                              TextView textViewTempsRestant, TextView textViewAlarmeActive){
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        this.MapIdAlarm = MapIdAlarm;
        this.MapIdDate = MapIdDate;
        this.ListActif = ListActif;
        this.ListInactif = ListInactif;
        this.ListSortId = ListSortId;
        this.ListSortAlarm = ListSortAlarm;
        this.textViewTempsRestant = textViewTempsRestant;
        this.textViewAlarmeActive = textViewAlarmeActive;
        this.listView = listView;

        this.interactHelper = new InteractHelper(textViewTempsRestant, textViewAlarmeActive);


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

            //switch listener
            switchMaterial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            //item Listener
            View finalConvertView = convertView;
            convertView.setOnLongClickListener(v -> {
                AlertDialog.Builder popUp = new AlertDialog.Builder(context);
                popUp.setNegativeButton("EFFACER", (dialog, which) -> {

                    interactHelper.effacer(id, context, ListSortAlarm, listView, finalConvertView,
                            MapIdAlarm, MapIdDate,
                            ListActif, ListInactif, ListSortId);
                    Toast.makeText(context, "effacé", Toast.LENGTH_SHORT).show();


                });
                setListMain();
                MainActivity main;
                main = (MainActivity) context;
                listView.setAdapter(main.modelAlarmeAdapter);
                popUp.setPositiveButton("MODIFIER", (dialog, which) -> Toast.makeText(context, "modifié", Toast.LENGTH_SHORT).show());
                popUp.show();
                return false;
            });

        }





        return convertView;
    }
    private void setListMain(){
        MainActivity main;
        main = (MainActivity) context;
        main.MapIdAlarm = MapIdAlarm;
        main.setMapIdDate(MapIdDate);
        main.setListActif(ListActif);
        main.setListInactif(ListInactif);
        main.setListSortId(ListSortId);
        main.ListSortAlarm = ListSortAlarm;
        main.listView = listView;

    }
}
