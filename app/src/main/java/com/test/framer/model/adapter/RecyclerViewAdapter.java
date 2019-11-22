package com.test.framer.model.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.test.framer.R;
import com.test.framer.model.profile;
import java.util.List;

/**
 * http://github.com/codepath/android_guides/wiki/Using-the-RecyclerView
 *
 */

/**
 * Bind the recycleview to the data
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<profile> ProfileList;

    public RecyclerViewAdapter(Context context, List<profile> profileList) {
        this.context = context;
        this.ProfileList = profileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // make the brew_profile_row a view
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.brew_profile_row, viewGroup, false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        profile profil = ProfileList.get(position); // each contact object in our list
        viewHolder.BName.setText(profil.getName());
        viewHolder.gravValueTV.setText(String.valueOf(profil.getGravity()));
        viewHolder.tempValueTV.setText(String.valueOf(profil.getTemperature()));
        viewHolder.descTV.setText(profil.getDescription());

    }

    @Override
    public int getItemCount() {
        return ProfileList.size();
    }
// bind the view with the data
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView BName;
        public TextView gravValueTV;
        public TextView tempValueTV;
        public TextView descTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            BName=itemView.findViewById(R.id.beerName);
            gravValueTV = itemView.findViewById(R.id.gravText);
            tempValueTV= itemView.findViewById((R.id.tempText));
            descTV = itemView.findViewById(R.id.descriptionBeer);

        }
    }
}
