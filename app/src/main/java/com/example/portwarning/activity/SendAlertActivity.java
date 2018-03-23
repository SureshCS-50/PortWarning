package com.example.portwarning.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.portwarning.Models.Alert;
import com.example.portwarning.Models.Port;
import com.example.portwarning.PortWarningApplication;
import com.example.portwarning.R;
import com.example.portwarning.PortAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class SendAlertActivity extends AppCompatActivity {

    private String mLatSC, mLongSC, mLatSFC, mLongSFC;
    private EditText mEtName, mEtLatSC, mEtLongSC, mEtLatSFC, mEtLongSFC;
    private Spinner mSpinnerIntensity, mSpinnerPort;
    private PortAdapter mPortAdapter;
    private Port mSelectedPort;

    private DatabaseReference mFirebaseDatabase;
    private List<Port> mPorts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_warning);

        mEtName = findViewById(R.id.etAlertName);
        mEtLatSC = findViewById(R.id.etLatSC);
        mEtLongSC = findViewById(R.id.etLongSC);
        mEtLatSFC = findViewById(R.id.etLatSFC);
        mEtLongSFC = findViewById(R.id.etLongSFC);
        mSpinnerIntensity = findViewById(R.id.spinnerIntensity);
        mSpinnerPort = findViewById(R.id.spinnerPort);

        mPorts = Port.getAllPorts();
        mPortAdapter = new PortAdapter(this,
                android.R.layout.simple_spinner_item,
                mPorts);
        mSpinnerPort.setAdapter(mPortAdapter);

        // get reference to 'alerts' node
        mFirebaseDatabase = PortWarningApplication.getInstance().getFirebaseDatabase();

        mSpinnerPort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mSelectedPort = mPortAdapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_send_alert, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_send_alert_btn) {
            sendAlert();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void sendAlert() {
        double latSC, longSC, latSFC, longSFC, x, y, d, distance;
        String intensity = mSpinnerIntensity.getSelectedItem().toString(), signal = "";

        mLatSC = mEtLatSC.getText().toString();
        mLongSC = mEtLongSC.getText().toString();
        mLatSFC = mEtLatSFC.getText().toString();
        mLongSFC = mEtLongSFC.getText().toString();

        if (validateFields()) {
            latSC = Double.valueOf(mLatSC); // cx
            longSC = Double.valueOf(mLongSC); // cy
            latSFC = Double.valueOf(mLatSFC); // hx
            longSFC = Double.valueOf(mLongSFC); // hy

            x = mSelectedPort.lat - latSC;
            y = mSelectedPort.lon - longSC;
            d = (x * x) + (y * y);
            d = Math.sqrt(d);
            distance = d * 111.0;

            if (distance > 200) {
                if (intensity.equals("SD")) {
                    signal = "Signal 1";
                } else if (intensity.equals("CS") || intensity.equals("SCS")) {
                    signal = "Signal 2";
                } else {
                    signal = "ERROR";
                }
            } else if (distance < 200) {
                if (intensity.equals("SD")) {
                    signal = "Signal 3";
                } else if (intensity.equals("CS")) {
                    if ((mSelectedPort.lat + 0.18) <= latSFC) {
                        signal = "Signal 6";
                    } else if ((mSelectedPort.lat - 0.18) >= latSFC) {
                        signal = "Signal 5";
                    } else {
                        signal = "Signal 7";
                    }
                } else if (intensity.equals("SCS")) {
                    if ((mSelectedPort.lat + 0.18) <= latSFC) {
                        signal = "Signal 9";
                    } else if ((mSelectedPort.lat - 0.18) >= latSFC) {
                        signal = "Signal 8";
                    } else {
                        signal = "Signal 10";
                    }
                } else {
                    signal = "ERROR";
                }
            } else {
                signal = "Signal 11";
            }

            if (!signal.equals("ERROR")) {
                sendToFirebaseDb(mEtName.getText().toString().trim(), latSC, longSC, latSFC, longSFC, distance, intensity, signal);
            } else {
                Toast.makeText(this, signal, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendToFirebaseDb(String name, double latSC, double longSC, double latSFC, double longSFC, double distance, String intensity, String signal) {
        // create alert id
        String alertId = mFirebaseDatabase.push().getKey();
        Alert alert = new Alert(name, latSC, longSC, latSFC, longSFC, distance, intensity, signal);
        mFirebaseDatabase.child(alertId).setValue(alert);
        finish();
    }

    private boolean validateFields() {
        if (mLatSC.isEmpty()) {
            mEtLatSC.requestFocus();
            mEtLatSC.setError("Enter Lat. of System Center");
            return false;
        }

        if (mLongSC.isEmpty()) {
            mEtLongSC.requestFocus();
            mEtLongSC.setError("Enter Long. of System Center");
            return false;
        }

        if (mLatSFC.isEmpty()) {
            mEtLatSFC.requestFocus();
            mEtLatSFC.setError("Enter Lat. of System Forecast Center");
            return false;
        }

        if (mLongSFC.isEmpty()) {
            mEtLongSFC.requestFocus();
            mEtLongSFC.setError("Enter Long. of System Forecast Center");
            return false;
        }

        return true;
    }

}
