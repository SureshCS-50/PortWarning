package com.example.portwarning.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.portwarning.Models.Alert;
import com.example.portwarning.Models.Port;
import com.example.portwarning.PortWarningApplication;
import com.example.portwarning.R;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SendAlertActivity extends AppCompatActivity {

    private String mLatSC, mLongSC, mLatSFC, mLongSFC;
    private EditText mEtName, mEtLatSC, mEtLongSC, mEtLatSFC, mEtLongSFC;
    private Spinner mSpinnerIntensity, mSpinnerIntensity2;
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
        mSpinnerIntensity2 = findViewById(R.id.spinnerIntensity2);

        // get reference to 'alerts' node
        mFirebaseDatabase = PortWarningApplication.getInstance().getFirebaseDatabase();

        mPorts = Port.getAllPorts();
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
        double cyx, cyy, hx, hy;
        String intensity = mSpinnerIntensity.getSelectedItem().toString();
        String intensity1 = mSpinnerIntensity2.getSelectedItem().toString();
        String[] results = new String[16];

        mLatSC = mEtLatSC.getText().toString();
        mLongSC = mEtLongSC.getText().toString();
        mLatSFC = mEtLatSFC.getText().toString();
        mLongSFC = mEtLongSFC.getText().toString();
        int i = 0;

        if (validateFields()) {
            cyx = Double.valueOf(mLatSC);
            cyy = Double.valueOf(mLongSC);
            hx = Double.valueOf(mLatSFC);
            hy = Double.valueOf(mLongSFC);

            for (Port port : mPorts) {
                results[i] = calcSignal(cyx, cyy, hx, hy, intensity, intensity1, port);
                i++;
            }

            sendToFirebaseDb(mEtName.getText().toString().trim(), cyx, cyy, hx, hy, results);
        }
    }

    private void sendToFirebaseDb(String name, double cyx, double cyy, double hx, double hy, String... results) {
        // create alert id
        String alertId = mFirebaseDatabase.push().getKey();
        String date = getSystemCurrentTime("yyyy:MM:dd");
        String time = getSystemCurrentTime("HH:mm:ss");
        Alert alert = new Alert(name, cyx, cyy, hx, hy, date, time, results[0], results[1], results[2], results[3], results[4], results[5], results[6], results[7], results[8], results[9], results[10]);
        mFirebaseDatabase.child(alertId).setValue(alert);
        finish();
    }

    private String calcSignal(double cyx, double cyy, double hx, double hy, String inte, String inte1, Port port) {
        double x, y, x1, y1;
        double px = port.lat, py = port.lon;

        x1 = cyx - px;
        y1 = cyy - py;
        x = hx - px;
        y = hy - py;
        double d, d1;
        d1 = (x1 * x1) + (y1 * y1);
        d1 = Math.sqrt(d1);
        d = (x * x) + (y * y);
        d = Math.sqrt(d);
        double distance, distance1;
        distance = d * 111.0;
        distance1 = d1 * 111.0;

        String sd, cs, scs;
        String sig = "";
        sd = "D";
        cs = "CS";
        scs = "SCS";

        if (distance1 > 500.0) {
            if (inte1.equals(sd)) {
                sig = "Signal 1 - DC1 (Distant Cautionary 1)";
            } else if (inte1.equals(cs) || inte1.equals(scs)) {
                sig = "Signal 2 - DW2 (Distant Warning 2)";
            } else {
                sig = "ERROR";
            }
        } else {
            if (distance > 250.0) {
                if (inte.equals(sd)) {
                    sig = "Signal 1 - DC1 (Distant Cautionary 1)";
                } else if (inte.equals(cs) || inte.equals(scs)) {
                    sig = "Signal 2 - DW2 (Distant Warning 2)";
                } else {
                    sig = "ERROR";
                }
            } else if (distance < 250.0) {
                if (inte.equals(sd)) {
                    sig = "Signal 3 - LC3 (Local Cautionary 3)";
                } else if (inte.equals(cs)) {
                    if ((hx + 0.20) >= px && (hx - 0.20) <= px) {
                        sig = "Signal 7 - D7 (Danger 7) ";
                    } else if (hx <= px) {
                        sig = "Signal 6 - D6 (Danger 6)";
                    } else if (hx >= px) {
                        sig = "Signal 5 - D5 (Danger 5)";
                    }
                } else if (inte.equals(scs)) {
                    if ((hx + 0.20) >= px && (hx - 0.20) <= px) {
                        sig = "Signal 10 - GD10 (Great Danger 10)";
                    } else if (hx <= px) {
                        sig = "Signal 9 - GD9 (Great Danger 9)";
                    } else if (hx >= px) {
                        sig = "Signal 8 - GD8 (Great Danger 8)";
                    }
                } else {
                    sig = "ERROR";
                }
            } else {
                sig = "Signal 11";
            }
        }
        return sig;
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

    public static String getSystemCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

}
