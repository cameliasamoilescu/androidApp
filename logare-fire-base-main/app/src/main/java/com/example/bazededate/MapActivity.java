package com.example.bazededate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.Manifest;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class MapActivity extends AppCompatActivity implements  OnMapReadyCallback, LocationListener, LocationOperations, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener{
    private boolean locationPermissionGranted;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private GoogleMap mMap;
    Location lastKnownLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private Marker mCurrLocationMarker;
    private Marker newLocationMarker;
    private String selectedAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        // gets the permission
        getLocationPermission();



        // Get the current location of the device and set the position of the map.
        getDeviceLocation();




    }


    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lastKnownLocation = task.getResult();
                            Log.d("Location", "Current location has been found.");
                            if (lastKnownLocation != null) {
                                LatLng currentPos = new LatLng(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom( currentPos
                                        , 15));
                                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

                                MarkerOptions markerOptions = new MarkerOptions().position(currentPos).title("Here am I");
                                mMap.addMarker(markerOptions);

                            }
                        } else {
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
            else System.out.println("Fara permisiune!");
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //Initialize Google Play Services

        //Initialize Google Play Services
        GoogleMap.OnMapLongClickListener onMapClickListener = latLng -> System.out.println("hei");
        this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        newLocationMarker = null;

        FloatingActionButton writeButton = findViewById(R.id.write);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, selectedAddress, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    public void onMapClick(LatLng point) {
        // tapTextView.setText("tapped, point=" + point);
        // System.out.println("tapped " + point);

        MarkerOptions markerOptions = new MarkerOptions().position(point).title("new location");
        mMap.addMarker(markerOptions);


        selectedLocation(point);
    }

    @Override
    public void onMapLongClick(LatLng point) {
        // System.out.println("long tapped " + point);
        selectedLocation(point);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();

    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void getLocationPermission() {
        // request location permission, so that we can get the location of the device
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lastKnownLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Showing Current Location Marker on Map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state
                            + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


    }



    @Override
    public void selectedLocation(com.example.bazededate.Location location) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = new Intent(getApplicationContext(), ShowInfoActivity.class);
        intent.putExtra("Location",  location);
        intent.putExtra("UID", user.getUid());
        startActivity(intent);

    }

    private void selectedLocation(LatLng latLng) {
        Double[] latLngs = new Double[]{latLng.latitude, latLng.longitude};

        new SelectedLocationOperation(getApplicationContext(), this).execute(latLngs);
    }


}