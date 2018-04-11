package com.example.portwarning.Models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;


@IgnoreExtraProperties
public class Port extends SugarRecord {

    public static final double ZOOM_LAT = 10.45;
    public static final double ZOOM_LONG = 79.50;

    public int portId;
    public String name;
    public double lat, lon;
    public boolean isIntermediatePort;
    public String signal;

    public Port() {
    }

    public Port(int portId, String name, double lat, double lon, boolean isIntermediatePort, String signal) {
        this.portId = portId;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.isIntermediatePort = isIntermediatePort;
        this.signal = signal;
        this.save();
    }

    public static List<Port> getAllPorts() {
        List<Port> ports = Port.listAll(Port.class);
        if(ports != null && ports.size() > 0){
            return ports;
        }
        ports = loadPortsToDb();
        return ports;
    }

    private static List<Port> loadPortsToDb() {
        List<Port> ports = new ArrayList<>();

        // Major ports
        ports.add(new Port(1, "CHENNAI", 13.08, 80.29, false, ""));
        ports.add(new Port(2, "ENNORE", 13.1, 80.18, false, ""));
        ports.add(new Port(3, "THOOTHUKODI", 8.44, 78.1, false, ""));

        // Intermediate ports/ Fishing Harbours
        ports.add(new Port(4, "CUDDLORE", 11.41, 79.46, true, ""));
        ports.add(new Port(5, "NAGAPATTINAM", 10.45, 79.50, true, ""));
        ports.add(new Port(6, "KATTUPALLI", 13.18, 80.2, true, ""));
        ports.add(new Port(7, "PUDUCHERRY", 11.55, 79.49, true, ""));
        ports.add(new Port(8, "KARAIKAL", 10.49, 79.5, true, ""));
        ports.add(new Port(9, "PAMBAN", 9.16, 79.12, true, ""));
        ports.add(new Port(10, "RAMESHWARAM", 9.14, 79.18, true, ""));
        ports.add(new Port(11, "COLACHEL", 8.44, 78.1, true, ""));

        return ports;
    }
}
