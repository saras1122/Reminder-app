package com.example.myapplication;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity4 extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationProviderClient;
    TextView locationTextView;
    LocationRequest locationRequest;
    Button getLocation;
    private final static int REQUEST_CODE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        locationTextView = findViewById(R.id.location_text);
        getLocation = findViewById(R.id.getLocation);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });
    }
    void getLastLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location){
                            if (location != null){
                                try {
                                    Geocoder geocoder = new Geocoder(MainActivity4.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    locationTextView.setText(MessageFormat.format("Lat: {0} Long: {1} Accuracy: {2}",addresses.get(0).getLatitude(),
                                            addresses.get(0).getLongitude(), addresses.get(0).getLocality()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        }else {
            askPermission();
        }
    }
    private void askPermission(){
        ActivityCompat.requestPermissions(MainActivity4.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions,
                                           @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(MainActivity4.this,"Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}