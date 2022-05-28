package fr.wiiznokes.horloge.utils.affichage.addRingFrag;

import android.media.MediaPlayer;
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
import fr.wiiznokes.horloge.utils.helper.SoundHelper;

public class CustomAdapterAddRing extends RecyclerView.Adapter<CustomAdapterAddRing.ViewHolder> {

    private static List<String> dataset;
    private static SoundHelper soundHelper;

    public CustomAdapterAddRing() {
        dataset = AddRingFragment.dataset;
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
        //aficherUriName
        holder.textView.setText(dataset.get(position));

        //play the song
        holder.itemView.setOnClickListener(v -> {
            Uri uri = AddRingFragment.listUri.get(position);
            if(uri != null){
                SoundHelper.setMediaPlayer(holder.itemView.getContext(), uri);
            }
        });

        //select the song
        holder.radioButton.setOnClickListener(v -> {
            AddRingFragment.listSelect.set(position, !AddRingFragment.listSelect.get(position));
            holder.radioButton.setChecked(AddRingFragment.listSelect.get(position));

        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }





    public static void afficherListsTest(){
        System.out.println("dataset :");
        for(String text : dataset){
            System.out.println(text);
        }

        System.out.println("listUri :");
        for(Uri uri : AddRingFragment.listUri){
            System.out.println(uri);
        }

        System.out.println("listSelect :");
        for(boolean bool : AddRingFragment.listSelect){
            System.out.println(bool);
        }


    }


}
