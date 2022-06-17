package com.example.bazededate;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.sip.SipSession;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mapbox.mapboxsdk.geometry.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class SelectedLocationOperation extends AsyncTask<Double, Void, Location> {

        Context context;
        LocationOperations listener;

        SelectedLocationOperation(Context context, LocationOperations listener){
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected Location doInBackground(Double...doubles) {
            try {
                Location result = new Location();

                double longitude = doubles[1];
                double latitude = doubles[0];


                Geocoder geocoder = new Geocoder(context);
                try {
                    List<Address> listAddresses = geocoder.getFromLocation(latitude,
                            longitude, 1);
                    if (null != listAddresses && listAddresses.size() > 0) {
                        result = getStringAddres(listAddresses);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;

            } catch(Exception e){
                return null;
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }



        @Override
        protected void onPostExecute(Location location){
            //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            if (location != null)
                Toast.makeText(context, location.getCountry() + ", " + location.getLocality() +
                    ", " + location.getSubLocality() + ", " + location.getStreetName() + ", " + location.getNumber(), Toast.LENGTH_LONG).show();
            else Toast.makeText(context, "error", Toast.LENGTH_LONG).show();

            listener.selectedLocation(location);


        }

        private Location getStringAddres(List<Address> listAddresses) {
            String result;
            String country = listAddresses.get(0).getCountryName();
            String subLocality = listAddresses.get(0).getSubLocality();
            String locality = listAddresses.get(0).getLocality();
            // get number
            String number = listAddresses.get(0).getFeatureName();

            // get street name + number
            String[] address = listAddresses.get(0).getAddressLine(0).split(",", 3);

            String streetName = address[0].substring(0, address[0].lastIndexOf(" "));


            Location location = new Location(country, locality, subLocality, "", number, streetName);
            return location;
        }
}
