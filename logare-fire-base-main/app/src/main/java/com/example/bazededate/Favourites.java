package com.example.bazededate;

import java.io.Serializable;

public class Favourites implements Serializable {
    public String locationId;
    public   String UID;

    public Favourites(String locationId, String UID) {
        this.locationId = locationId;
        this.UID = UID;
    }
}
