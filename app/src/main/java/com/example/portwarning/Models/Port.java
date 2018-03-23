package com.example.portwarning.Models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;


@IgnoreExtraProperties
public class Port extends SugarRecord {

    public static final double LAT_CHENNAI= 13.08; // chx
    public static final double LONG_CHENNAI = 80.29; // chy

    public String name;
    public double lat, lon;
    public boolean isIntermediatePort;

    public Port() {
    }

    public Port(String name, double lat, double lon, boolean isIntermediatePort) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.isIntermediatePort = isIntermediatePort;
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
        ports.add(new Port("CHENNAI", 13.082032, 80.292016, false));
        ports.add(new Port("ENNORE", 13.252851, 80.326744, false));
        ports.add(new Port("THOOTHUKUDI", 8.756108, 78.178867, false));
        ports.add(new Port("Deendayal Port", 23.002721, 70.218651, false));
        ports.add(new Port("Porbandar Port", 21.63068, 69.601685, true));
        ports.add(new Port("Mumbai Port", 18.948014, 72.844454, false));
        ports.add(new Port("Jawaharlal Nehru Port Trust", 18.949915, 72.949723, false));
        ports.add(new Port("Panaji Port", 15.411207, 73.799978, false));
        ports.add(new Port("Mangalore Port", 12.927278, 74.812275, false));
        ports.add(new Port("Kochi Port", 9.966681, 76.271960, false));
        ports.add(new Port("Visakhapatnam Port", 17.684053, 83.238926, false));
        ports.add(new Port("Paradip Port Trust", 20.265762, 86.676326, false));
        ports.add(new Port("Haldia Port", 22.547341, 88.302385, false));

        // Intermediate ports/ Fishing Harbours
        ports.add(new Port("CUDDALORE", 11.7124542, 79.7687707, true));
        ports.add(new Port("NAGAPATTINAM", 10.7615737, 79.8396915, true));
        ports.add(new Port("KATTUPALLI", 13.2958353, 80.3079938, true));
        ports.add(new Port("PUDUCHERRY", 11.9171538, 79.8158044, true));
        ports.add(new Port("KARAIKAL", 10.873467, 79.8109926, true));
        ports.add(new Port("PAMBAN", 9.2737779, 79.1361985, true));

        ports.add(new Port("RAMESWARAM", 9.2746323, 79.3161605, true));
        ports.add(new Port("COLACHAL", 8.1, 77.15, true));
        ports.add(new Port("Port of Veraval", 20.54, 70.22, true));
        ports.add(new Port("Bhavnagar Port", 21.45, 72.13, true));
        ports.add(new Port("Bharuch Port", 21.42, 72.59, true));
        ports.add(new Port("Surat Port", 21.06, 72.42, true));
        ports.add(new Port("Ratnagiri Port", 17.00, 73.16, true));
        ports.add(new Port("Marmagoa Port", 15.24, 73.48, false));
        ports.add(new Port("Alappuzha Port", 9.29, 76.19, true));
        ports.add(new Port("Machilipatnam Port", 16.11, 81.08, true));
        ports.add(new Port("Kakinada Port", 16.56, 82.14, true));

        return ports;
    }
}
