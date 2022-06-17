package com.example.bazededate;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MapsGoodActivity extends AppCompatActivity
        implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback {

    private TextView tapTextView;
    private TextView cameraTextView;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_good);

        //tapTextView = findViewById(R.id.tap_text);
        //cameraTextView = findViewById(R.id.camera_text);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        this.map.setOnMapClickListener(this);
        this.map.setOnMapLongClickListener(this);
        this.map.setOnCameraIdleListener(this);
    }

    @Override
    public void onMapClick(LatLng point) {
       // tapTextView.setText("tapped, point=" + point);
        System.out.println("tapped " + point);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        System.out.println("long tapped " + point);
    }

    @Override
    public void onCameraIdle() {
        //cameraTextView.setText(map.getCameraPosition().toString());
        System.out.println("here " + map.getCameraPosition().toString());
    }
}


