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





    private MainActivity mainActivity;
    private LayoutInflater inflater;
    private int cheminLayout = R.layout.alarme_affichage;
    private InteractHelper interactHelper;

    public List<Alarm> ListSortAlarm;





    public ModelAlarmeAdapter(Context context){
        this.mainActivity = (MainActivity) context;
        this.inflater = LayoutInflater.from(context);
        this.ListSortAlarm = mainActivity.ListSortAlarm;

        this.interactHelper = new InteractHelper(mainActivity.textViewTempsRestant, mainActivity.textViewAlarmeActive);


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
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder popUp = new AlertDialog.Builder(mainActivity);
                    popUp.setNegativeButton("EFFACER", (dialog, which) -> {

                        interactHelper.effacer(id, mainActivity);
                        Toast.makeText(mainActivity, "effacé", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                    });
                    popUp.show();
                    return true;
                }
            });
        }
        return convertView;
    }

}
