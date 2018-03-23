package com.example.portwarning;

public class MarkerInfo {

    public String title;
    public String lat, lng, distance;
    public String intensity, signal;

    public MarkerInfo(String title, String lat, String lng, String distance, String intensity, String signal) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
        this.intensity = intensity;
        this.signal = signal;
    }
}
