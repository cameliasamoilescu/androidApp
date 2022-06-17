package com.example.bazededate;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoriesViewHolder> {
    private List<Location> favLocations;
    public AdapterView.OnItemClickListener onItemClickListener;
    private View view;

    public StoryAdapter(List<Location> locations) {
        this.favLocations = locations;
    }


    @NonNull
    @Override
    public StoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        view = LayoutInflater.from(context).inflate(R.layout.layout_item, parent, false);

        StoriesViewHolder storiesViewHolder = new StoriesViewHolder(view);

        return storiesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StoriesViewHolder holder, int position) {
        Location location = favLocations.get(position);

        holder.setStory(location);

    }



    @Override
    public int getItemCount() {
        return favLocations.size();
    }

    public class StoriesViewHolder extends RecyclerView.ViewHolder {
        private TextView locationName;
        private TextView storyText;
        public RelativeLayout relativeLayout;
        private Location location;

        public StoriesViewHolder(View itemView) {
            super(itemView);

            this.relativeLayout =  (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            this.locationName = (TextView) itemView.findViewById(R.id.locationName);
            this.storyText = (TextView) itemView.findViewById(R.id.fav_story);
        }

        public void setStory(Location location) {
            this.location = location;

            this.locationName.setText(location.getStreetName() + ", " + location.getNumber());
            this.storyText.setText(location.getStory());



        }

    }



}
