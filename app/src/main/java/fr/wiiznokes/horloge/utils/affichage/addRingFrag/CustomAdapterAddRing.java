package fr.wiiznokes.horloge.utils.affichage.addRingFrag;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fr.wiiznokes.horloge.R;
import fr.wiiznokes.horloge.fragments.helperFrag.AddRingFragment;

public class CustomAdapterAddRing extends RecyclerView.Adapter<CustomAdapterAddRing.ViewHolder> {
    private static int source;

    private List<String> dataset;
    private List<Uri> listUris;
    private List<Boolean> listSelects;

    public CustomAdapterAddRing(List<String> data, int sourceP) {
        dataset = data;
        source = sourceP;


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;
        private final RadioButton radioButton;

        public ViewHolder(View v) {
            super(v);

            this.textView = v.findViewById(R.id.ringNameTextView2);
            this.radioButton = v.findViewById(R.id.ringRadioButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.model_add_ring, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //play the song or choose silence/default
        if(position == 0 && source == AddRingFragment.settingSource){

        }
        if(position == 0 && source == AddRingFragment.addAlarmSource){

        }
        if(position == 1 && source == AddRingFragment.addAlarmSource){

        }
        holder.itemView.setOnClickListener(v -> {});

        //select the song
        holder.radioButton.setOnClickListener(v -> {});
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }


}
