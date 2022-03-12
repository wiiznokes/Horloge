package fr.wiiznokes.horloge11.utils;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import fr.wiiznokes.horloge11.R;

public class ModelAlarmeAdapter extends BaseAdapter {

    private Map<Id>
    public List<Alarm> getAlarmList() {
        return AlarmList;
    }
    public void setAlarmList(List<Alarm> alarmList) {
        AlarmList = alarmList;
    }

    private Context context;
    private LayoutInflater inflater;
    private int cheminLayout = R.layout.alarme_affichage;

    public ModelAlarmeAdapter(Context context, List<Alarm> AlarmList){
        this.context = context;
        this.AlarmList = AlarmList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return AlarmList.size();
    }

    @Override
    public Alarm getItem(int position) {
        return AlarmList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return AlarmList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //We must create a View:
            convertView = inflater.inflate(cheminLayout, parent, false);
        }


        return convertView;
    }
}
