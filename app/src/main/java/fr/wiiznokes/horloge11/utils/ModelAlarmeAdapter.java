package fr.wiiznokes.horloge11.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import fr.wiiznokes.horloge11.R;

public class ModelAlarmeAdapter extends BaseAdapter {

    private Context context;
    private List<ModelAlarme> modelAlarmeList;
    private LayoutInflater inflater;

    public ModelAlarmeAdapter(Context context, List<ModelAlarme> modelAlarmeList){
        this.context = context;
        this.modelAlarmeList = modelAlarmeList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return modelAlarmeList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelAlarmeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.alarme_affichage, null);

        return convertView;
    }
}
