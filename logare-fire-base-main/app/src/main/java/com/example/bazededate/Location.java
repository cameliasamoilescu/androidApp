package com.example.bazededate;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class Location implements Serializable {
    private String country;
    private String locality;
    private String subLocality;
    private String story;
    private String number;
    private String streetName;
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public Location(String country, String locality, String subLocality, String story, String number, String streetName) {
        this.country = country;
        this.locality = locality;
        this.subLocality = subLocality;
        this.story = story;
        this.number = number;
        this.streetName = streetName;
        this.id = null;
    }

    public Location(String id) {
        this.id = id;
//
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
//        ref.child("locations").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                Iterable<DataSnapshot> children = snapshot.getChildren();

//                for (DataSnapshot child: children) {
//                    Location location = child.getValue(Location.class);
//
//                    if (location.getID().equals(id) ) {
//
//                        country = location.getCountry();
//                        locality = location.getLocality();
//                        subLocality = location.getSubLocality();
//                        story = location.getStory();
//                        number = location.getNumber();
//                        streetName = location.getStreetName();
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    public Location() {}

    public String getSubLocality() {
        return subLocality;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStory() {
        return story;
    }

    public String getNumber() {
        return number;
    }

    public String getLocality() {
        return locality;
    }

    public String getCountry() {
        return country;
    }

    public String getID() {
        if (id == null)
            return (country.substring(0,3) + locality.substring(0, 3) + locality.substring(0, 3) + number + streetName).toUpperCase().replaceAll("\\s", "").replace(".", "");
        else
            return this.id;
    }

    public void setStory(String story) {
        this.story = story;
    }
}
