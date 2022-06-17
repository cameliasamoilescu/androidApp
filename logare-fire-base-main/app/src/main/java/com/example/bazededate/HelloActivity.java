package com.example.bazededate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelloActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private RecyclerView.LayoutManager layoutManager;
    private String currentUser;
    private ListView listView;
    private FloatingActionButton menuButton;
    private FloatingActionButton singOutButton;
    private FloatingActionButton mapsButton;
    private FloatingActionButton classifyButton;
    ArrayList<Location> userLocations;
    List<String> locations;
    ArrayAdapter<Location> adapter;
    Context var;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name = user.getEmail();
        this.currentUser = user.getUid();
        this.singOutButton = findViewById(R.id.logout);
        this.mapsButton = findViewById(R.id.gotomaps);
        this.classifyButton = findViewById(R.id.gotoclassifier);

        this.listView = (ListView) findViewById(R.id.listview);

        this.layoutManager = new LinearLayoutManager(this);


        this.menuButton = findViewById(R.id.menu);
        menuButton.setOnClickListener(view -> makeMenuVisible());
        this.ref = FirebaseDatabase.getInstance().getReference();
        locations = new ArrayList<String>();
        userLocations = new ArrayList<Location>();
         getLocations();

        mAuth = FirebaseAuth.getInstance();
        var = HelloActivity.this;




        singOutButton.setOnClickListener(view -> signOut());
        mapsButton.setOnClickListener(view -> goToMapsActivity());
        classifyButton.setOnClickListener(view -> goToRecognitionActivity());
    }

    private void makeMenuVisible() {
        if (this.singOutButton.getVisibility() != View.VISIBLE) {
            this.singOutButton.setVisibility(View.VISIBLE);
            this.classifyButton.setVisibility(View.VISIBLE);
            this.mapsButton.setVisibility(View.VISIBLE);
        }
        else {
            this.singOutButton.setVisibility(View.INVISIBLE);
            this.classifyButton.setVisibility(View.INVISIBLE);
            this.mapsButton.setVisibility(View.INVISIBLE);
        }
    }


    private void getLocations() {

        this.ref.child("savedLocations").addListenerForSingleValueEvent(new ValueEventListener() {
            // asta este anonymouss class
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // gets all the children at this level
                Iterable<DataSnapshot> children =  snapshot.getChildren();

                // verifies for all the elements
                for (DataSnapshot child: children) {
                    LocationUser locationUser = child.getValue(LocationUser.class);

                    if ( locationUser.getUID().equals( currentUser) ) {
                        locations.add(locationUser.getLocationId());

                    }
                }



                ref.child("locations").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Iterable<DataSnapshot> children = snapshot.getChildren();


                        for (DataSnapshot child: children) {
                            Location location = child.getValue(Location.class);

                            if (locations.contains(location.getID())) {
                                userLocations.add(location);
                            }


                            UserLocationAdapter adapter1 = new UserLocationAdapter(var, userLocations);


                            // after passing this array list to our adapter
                            // class we are setting our adapter to our list view.
                            listView.setAdapter(adapter1);


                        }

                        if (userLocations.isEmpty() == false) {
                            listView.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }


                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Location selectedItem = (Location) parent.getItemAtPosition(position);
//                System.out.println(selectedItem.getStory());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(getApplicationContext(), ShowInfoActivity.class);
                intent.putExtra("Location",  selectedItem);
                intent.putExtra("UID", user.getUid());
                startActivity(intent);
            }
        });

    }

    private void goToMapsActivity() {
        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        startActivity(intent);
    }

    private void goToRecognitionActivity() {
        Intent intent = new Intent(getApplicationContext(), BuildingRecognitionActivity.class);
        startActivity(intent);
    }


    private void signOut() {
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


}