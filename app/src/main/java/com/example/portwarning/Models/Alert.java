package com.example.portwarning.Models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.orm.SugarRecord;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Alert extends SugarRecord {

    public static final String CHILD_NAME = "alerts";

    public String name;
    public double lat, lon, latSFC, lonSFC, distance;
    public String intensity, signal;

    public Alert() {
    }

    public Alert(String name, double latSC, double lonSC, double latSFC, double lonSFC, double distance, String intensity, String signal) {
        this.name = (name.isEmpty()) ? "Unknown" : name;
        this.lat = latSC;
        this.lon = lonSC;
        this.latSFC = latSFC;
        this.lonSFC = lonSFC;
        this.distance = distance;
        this.intensity = intensity;
        this.signal = signal;
        this.save();
    }

}