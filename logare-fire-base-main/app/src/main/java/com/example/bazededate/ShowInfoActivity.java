package com.example.bazededate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class ShowInfoActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DatabaseReference savedLocationDatabase;
    private String locationId;
    private TextView storyTextView;
    private FloatingActionButton writeInfoButton;
    private Location location;
    private FloatingActionButton addToFavsButton;
    private TextView tileTxt;
    private  boolean isInDataBase = false;
    private String UID;
    private final static int REQUEST_CODE = 1;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private FloatingActionButton homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);

// ...
        mDatabase = FirebaseDatabase.getInstance().getReference("locations");
        savedLocationDatabase = FirebaseDatabase.getInstance().getReference("savedLocations");
        storageReference = FirebaseStorage.getInstance().getReference();
        storyTextView = findViewById(R.id.story);
        writeInfoButton = findViewById(R.id.writeInfo);
        addToFavsButton = findViewById(R.id.add_to_favs);
        location = (Location) getIntent().getExtras().getSerializable("Location");
        locationId = location.getID();
        tileTxt = findViewById(R.id.tilteTextView);
        tileTxt.setText(location.getStreetName() + ", " + location.getNumber());
        storyTextView.setMovementMethod(new ScrollingMovementMethod());
        homeButton = findViewById(R.id.homez);

        homeButton.setOnClickListener(
                view -> GoHome()
        );


        UID = getIntent().getExtras().getString("UID");

        getData();

        writeInfoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), EditStoryActivity.class);


                if(isInDataBase)
                    intent.putExtra("story", location.getStory());
                else intent.putExtra("story", "");
                startActivityForResult(intent, REQUEST_CODE);

            }
        });

        addToFavsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addToFavourites();

            }
        });



    }

    private void GoHome() {
        Intent intent = new Intent(getApplicationContext(), HelloActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                if (data != null)
                storyTextView.setText(data.getStringExtra("value"));
                writeInfo(location, data.getStringExtra("value"));
            }
        }
    }

    private void addToFavourites() {
        // Adds selected location to the favourites for current user
        DatabaseReference savedLocationRef = savedLocationDatabase.child(UID).child(locationId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    String id = savedLocationDatabase.push().getKey();
                    savedLocationDatabase.child(id).setValue(new Favourites(locationId, UID));
                    Toast.makeText(getApplicationContext(), "Location saved!", Toast.LENGTH_LONG).show();

                }
                else {
                    dataSnapshot.getRef().removeValue();
                    Toast.makeText(getApplicationContext(), "Removed from favourites!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error ", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        savedLocationRef.addListenerForSingleValueEvent(eventListener);


    }

    private void getData() {
        // Attach a listener to read the data at our posts reference
        // Read from the database

        mDatabase.child(locationId).child("story").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);

                if (value == null) {
                    storyTextView.setText("No story for this location:(");
                }
                else {
                    isInDataBase = true;
                    storyTextView.setText(value);
                }

                Log.d("Read data", "Value is: " + value + " for location id " + locationId);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Read data", "Failed to read value." + " for location id " + locationId, error.toException());
            }
        });
    }

    public void writeInfo(Location location, String story) {
        // checks if loca
        location.setStory(story);

        location.setId( location.getID() );
        mDatabase.child(location.getID()).setValue(location);
        }

}
