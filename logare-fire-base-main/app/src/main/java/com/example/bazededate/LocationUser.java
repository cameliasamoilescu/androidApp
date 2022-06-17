package com.example.bazededate;

public class LocationUser {
    private String UID;
    private String locationId;

    public LocationUser(String UID, String locationId) {
        this.UID = UID;
        this.locationId = locationId;
    }

    public LocationUser(){}

    public String getUID() {
        return UID;
    }

    public String getLocationId() {
        return locationId;
    }
}
