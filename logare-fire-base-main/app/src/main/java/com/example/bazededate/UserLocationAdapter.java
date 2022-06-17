package com.example.bazededate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserLocationAdapter extends ArrayAdapter<Location> {
    public UserLocationAdapter(Context context, ArrayList<Location> savedLocations) {
        super(context, 0, savedLocations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Location locationUser = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.locationName);
        TextView tvHome = (TextView) convertView.findViewById(R.id.fav_story);
        // Populate the data into the template view using the data object
        tvName.setText(locationUser.getStreetName() + ", " + locationUser.getNumber());
        tvHome.setText(locationUser.getStory());
        // Return the completed view to render on screen


        return convertView;
    }
}
