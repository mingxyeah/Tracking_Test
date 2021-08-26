package com.defence.tracking_test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME = 60000;
    private final long MIN_DIST = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        databaseReference = FirebaseDatabase.getInstance().getReference("Location");

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                setContentView(R.layout.activity_main);
                try {
                    String editTextLatitude = Double.toString(location.getLatitude());
                    String editTextLongitude = Double.toString(location.getLongitude());
                    while (true) {
                        databaseReference.child("latitude").removeValue();
                        databaseReference.child("longitude").removeValue();
                        databaseReference.child("latitude").setValue(editTextLatitude);
                        databaseReference.child("longitude").setValue(editTextLongitude);
                        Thread.sleep(60 * 1000);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try {
            if (locationManager != null && locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}