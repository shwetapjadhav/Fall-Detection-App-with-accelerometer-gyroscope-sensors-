package com.example.damera.falldetection;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static android.hardware.SensorManager.SENSOR_DELAY_NORMAL;
import static java.lang.System.out;

/**
 * Created by DAMERA on 04-21-2017.
 */

public class AccActivity extends AppCompatActivity implements SensorEventListener {
    Sensor accelerometer;
    Sensor gravity;
    SensorManager sm;
    TextView acceleration;
    TextView acceleration1;
    double X, Y, Z, A, B; int prevX, prevY, prevZ;

    int count = 0, position = 0;
    Boolean b = false;
    private LocationManager locationManager;
    private LocationListener locationListener;
    String message = "";
    Boolean alreadyDetected = false;
    Boolean timerStopped = false;
    private static long[] alarmer;
    private AccData DataAcc;
    private static boolean alertOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gravity =  sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sm.registerListener(this, accelerometer, SENSOR_DELAY_NORMAL);
        sm.registerListener(this, gravity, SENSOR_DELAY_NORMAL);

        acceleration = (TextView) findViewById(R.id.acceleration);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                message = location.getLatitude() + "," + location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);

            }

        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
                return;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        getAccelerometer1(event);
    }
    private void getAccelerometer(SensorEvent event) {

        double mroot = java.lang.Math.sqrt(event.values[0] * event.values[0] + event.values[1] * event.values[1] + event.values[2] * event.values[2]);

        if (!alertOn)
            if (mroot > DataAcc.getLargest() && alarmer[0] == 0) {
                alarmer[0] = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - alarmer[0] > DataAcc .getTimeDifference() && alarmer[0] > 0 && alarmer[1] == 0) {
                Arrays.fill(alarmer, 0);
            } else if (System.currentTimeMillis() - alarmer[0] < DataAcc.getTimeDifference() && mroot < DataAcc.getSmallest()
                    && alarmer[1] == 0) {
                alarmer[1] = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - alarmer[1] < 15000 && mroot > DataAcc.getSmallest() && mroot< DataAcc.getLargest() && alarmer[2] == 0) {
                alarmer[2] = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - alarmer[2] < 15000 && System.currentTimeMillis() - alarmer[2] > 2000
                    && (mroot < SensorManager.GRAVITY_EARTH - 4 || mroot > SensorManager.GRAVITY_EARTH + 4)) {
                Arrays.fill(alarmer, 0);
            } else if (System.currentTimeMillis() - alarmer[2] > 15000
                    && alarmer[2] > 0) {
                alertOn = true;
                Arrays.fill(alarmer, 0);
                Toast.makeText(getApplicationContext(),"FALL DETECTED TIMER STARTED", Toast.LENGTH_SHORT).show();
            }

    }

    public void getAccelerometer1(SensorEvent event ){
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            X = event.values[0];
            Y = event.values[1];
            Z = event.values[2];


            A = Math.sqrt(X * X + Y * Y + Z * Z);
            B = X + Y + Z;
            double Amax = 0, Amin = 10.0;
            if (A > Amax) {
                Amax = A;
            }
            if (A < Amin) {
                Amin = A;
            }

            if (A > 10 && B < 3 && !alreadyDetected) {
                alreadyDetected = true;
                prevX = (int) X;
                prevY = (int) Y;
                prevZ = (int) Z;

                new CountDownTimer(15000, 1000) {


                    public void onTick(long millisUntilFinished) {
                        if(Math.abs((int) X - prevX) > 3 && Math.abs((int) Y - prevY) > 3){
                            alreadyDetected = false;
                            acceleration.setText("");
                            this.cancel();
                        }
                        else{
                            //Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                            acceleration.setText("Seconds remaining: " + millisUntilFinished / 1000);
                        }
                    }

                    public void onFinish() {
                        acceleration.setText("I NEED YOUR HELP!");
                        DatabaseHelper DB2 = new DatabaseHelper(getApplicationContext());
                        Cursor res = DB2.SelectData();
                        if (res.getCount() == 0)
                        {

                        }

                        StringBuffer buffer = new StringBuffer();
                        while (res.moveToNext()) {
                            buffer.append("Id :" + res.getString(0) + "\n");
                            buffer.append("Name :" + res.getString(1) + "\n");
                            buffer.append("Phone :" + res.getString(2) + "\n\n");

                            if(!timerStopped)
                                sendMessage(res.getString(2), message);
                        }


                        finish();
                    }

                }.start();
            }

        }

    }

    private void sendMessage(String phoneNo, String message) {
        try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, "http://maps.google.com/?q=" + message, null, null);
                Toast.makeText(getApplicationContext(), "SMS Sent.", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS Fail. Please try again!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


    public void gotocloseActivity(View v) {
        timerStopped = true;
        finish();

        Intent i = new Intent(this, CloseActivity.class);
        startActivity(i);
    }

}
