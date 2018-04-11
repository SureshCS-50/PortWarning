package com.example.portwarning.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.portwarning.DBHelper;
import com.example.portwarning.MarkerInfo;
import com.example.portwarning.Models.Alert;
import com.example.portwarning.Models.Port;
import com.example.portwarning.PortWarningApplication;
import com.example.portwarning.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, ValueEventListener, ChildEventListener, GoogleMap.InfoWindowAdapter {

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;

    private List<Port> mPorts;

    private DatabaseReference mFirebaseDatabase;
    private DBHelper mDBHelper;
    private Alert mLastestAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mMapFragment.getMapAsync(this);

        mDBHelper = new DBHelper();

        mPorts = Port.getAllPorts();

        // get reference to 'alerts' node
        mFirebaseDatabase = PortWarningApplication.getInstance().getFirebaseDatabase();
        mFirebaseDatabase.addChildEventListener(this);
        mFirebaseDatabase.addValueEventListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (Port port : mPorts) {
            int drawableResId = (port.isIntermediatePort) ? R.drawable.anchor_blue : R.drawable.anchor;
            googleMap.addMarker(new MarkerOptions().position(new LatLng(port.lat, port.lon))
                    .icon(BitmapDescriptorFactory.fromResource(drawableResId))
                    .title(port.name));
        }

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Port.ZOOM_LAT, Port.ZOOM_LONG), 6f));
        mMap.setInfoWindowAdapter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_send_alert) {
            Intent iSendAlert = new Intent(MapActivity.this, SendAlertActivity.class);
            startActivity(iSendAlert);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.d("called", "Added");
        mLastestAlert = dataSnapshot.getValue(Alert.class);
        if (mLastestAlert == null) {
            return;
        }
        mLastestAlert.save();


        LatLng latLng = new LatLng(mLastestAlert.lat, mLastestAlert.lon);
        mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.cyclone))
                .title(mLastestAlert.name));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f));
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.d("called", "Changed");
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.d("called", "Removed");
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        Log.d("called", "Moved");
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Log.d("called", "onDataChange");
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Log.d("called", "Cancelled");
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        LatLng latLng = marker.getPosition();
        MarkerInfo info = null;
        View v = null;
        int iconResId = R.drawable.cyclone_large;
        String lat = "", lng = "";
        Port port = (Port) mDBHelper.getValuesByLatLong(latLng.latitude, latLng.longitude, Port.class);
        if (port != null) {
            v = getLayoutInflater().inflate(R.layout.map_info_window_port, null);
            lat = String.format("%.2f", port.lat);
            lng = String.format("%.2f", port.lon);
            info = new MarkerInfo(port.name, lat, lng, "0", "", "");

            if (mLastestAlert != null) {
                TextView txtSignal = v.findViewById(R.id.txtSignal);
                txtSignal.setVisibility(View.VISIBLE);

                switch(port.name.toLowerCase()){
                    case "chennai":
                        txtSignal.setText(mLastestAlert.chennai);
                        break;
                    case "colachel":
                        txtSignal.setText(mLastestAlert.colachel);
                        break;
                    case "cuddlore":
                        txtSignal.setText(mLastestAlert.cuddlore);
                        break;
                    case "ennore":
                        txtSignal.setText(mLastestAlert.ennore);
                        break;
                    case "karaikal":
                        txtSignal.setText(mLastestAlert.karaikal);
                        break;
                    case "kattupalli":
                        txtSignal.setText(mLastestAlert.kattupalli);
                        break;
                    case "nagapattinam":
                        txtSignal.setText(mLastestAlert.nagapattinam);
                        break;
                    case "pamban":
                        txtSignal.setText(mLastestAlert.pamban);
                        break;
                    case "puducherry":
                        txtSignal.setText(mLastestAlert.puducherry);
                        break;
                    case "rameshwaram":
                        txtSignal.setText(mLastestAlert.rameshwaram);
                        break;
                    case "thoothukodi":
                        txtSignal.setText(mLastestAlert.thoothukodi);
                        break;
                }
            }
            iconResId = (port.isIntermediatePort) ? R.drawable.anchor_blue : R.drawable.anchor;
        }

        if (info == null) {
            Alert alert = (Alert) mDBHelper.getValuesByLatLong(latLng.latitude, latLng.longitude, Alert.class);
            v = getLayoutInflater().inflate(R.layout.map_info_window_alert, null);
            lat = String.format("%.2f", alert.lat);
            lng = String.format("%.2f", alert.lon);

            info = new MarkerInfo(alert.name, lat, lng, "0", "", "");
        }

        ImageView imgIcon = (ImageView) v.findViewById(R.id.imgIcon);
        TextView tvTitle = (TextView) v.findViewById(R.id.txtTitle);
        TextView tvLatLong = (TextView) v.findViewById(R.id.txtLatLong);

        imgIcon.setImageResource(iconResId);
        tvTitle.setText(info.title);
        tvLatLong.setText(info.lat + ", " + info.lng);

        return v;
    }

    @Override
    protected void onDestroy() {
        Alert.deleteAll(Alert.class);
        super.onDestroy();
    }
}
