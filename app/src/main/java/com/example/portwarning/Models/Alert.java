package com.example.portwarning.Models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.orm.SugarRecord;

import java.util.ArrayList;

@IgnoreExtraProperties
public class Alert extends SugarRecord {

    public static final String CHILD_NAME = "alerts";

    public String name = "";
    public double lat, lon, latSFC, lonSFC;
    public String chennai, colachel, cuddlore, ennore, karaikal, kattupalli, nagapattinam, pamban, puducherry, rameshwaram, thoothukodi;
    public String date, time;

    public Alert() {
    }

    public Alert(String name, double latSC, double lonSC, double latSFC, double lonSFC, String date, String time) {
        this.name = (name.isEmpty()) ? "Unknown" : name;
        this.lat = latSC;
        this.lon = lonSC;
        this.latSFC = latSFC;
        this.lonSFC = lonSFC;
        this.date = date;
        this.time = time;
    }

    public Alert(String name, double latSC, double lonSC, double latSFC, double lonSFC, String date, String time, String chennai, String colachel, String cuddlore, String ennore, String karaikal, String kattupalli, String nagapattinam, String pamban, String puducherry, String rameshwaram, String thoothukodi){
        this(name, latSC, lonSC, latSFC, lonSFC, date, time);
        this.chennai = chennai;
        this.colachel = colachel;
        this.cuddlore = cuddlore;
        this.ennore = ennore;
        this.karaikal = karaikal;
        this.kattupalli = kattupalli;
        this.nagapattinam = nagapattinam;
        this.pamban = pamban;
        this.puducherry = puducherry;
        this.rameshwaram = rameshwaram;
        this.thoothukodi = thoothukodi;
        this.save();
    }
}