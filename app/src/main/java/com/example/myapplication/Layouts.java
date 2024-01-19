package com.example.myapplication;

import static android.content.ContentValues.TAG;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.pdf.PdfDocument;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Layouts extends AppCompatActivity {
    private static final int LOCATION_REQUEST_INTERVAL = 500000; // 5 seconds
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    private Handler handler = new Handler();
    private Runnable locationRunnable;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    PageAdapter myViewPagerAdapter;
    ImageButton pop;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layouts);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        pop=findViewById(R.id.pop);
        pop.setOnClickListener((v)->show());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
//        Toast.makeText(Layouts.this, datePick(),
//                Toast.LENGTH_SHORT).show();
//        firestoreChecker();
        Context context = this;
        myViewPagerAdapter = new PageAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);
        int holoPurpleColor = ContextCompat.getColor(context, R.color.skyBlue1);
        TabLayout.Tab initialTab = tabLayout.getTabAt(0); // Change 0 to the index of your default tab
        initialTab.getIcon().setColorFilter(holoPurpleColor, PorterDuff.Mode.SRC_IN);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
                int holoPurpleColor = ContextCompat.getColor(context, R.color.skyBlue1);
                tab.getIcon().setColorFilter(holoPurpleColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }
    void show(){
        PopupMenu popupMenu=new PopupMenu(Layouts.this,pop,R.drawable.rounded_popup_background);
        popupMenu.getMenu().add("Setting");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Setting"){
                    startActivity(new Intent(Layouts.this,Setting.class));
                    return true;
                }
                return false;
            }
        });
    }
    private void getLastLocation() {
        locationRunnable = new Runnable() {
            @Override
            public void run() {
                // Check if the permission is granted before attempting to get the location
                if (ContextCompat.checkSelfPermission(Layouts.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // Get last known location
                    fusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        try {
                                            Geocoder geocoder = new Geocoder(Layouts.this, Locale.getDefault());
                                            List<Address> addresses = geocoder.getFromLocation(
                                                    location.getLatitude(), location.getLongitude(), 1);
                                            if (firestoreChecker(addresses.get(0).getLocality().toString().toLowerCase())) {
                                                Toast.makeText(Layouts.this, "You reached " + addresses.get(0).getLatitude(), Toast.LENGTH_SHORT).show();
                                            }
                                            Log.d("","");
                                            Toast.makeText(Layouts.this, addresses.get(0).getAdminArea()+ " " + addresses.get(0).getAddressLine(0),
                                                    Toast.LENGTH_SHORT).show();
                                        } catch (IOException e) {
                                            //Log.d("","55");
                                            e.printStackTrace();
                                        }
                                    }
                                    Log.d("","hhf");
                                }
                            });
                    // Schedule the next location update after 5 seconds
                    handler.postDelayed(this, LOCATION_REQUEST_INTERVAL);
                } else {
                    askPermission();
                }
            }
        };

        // Initial delay of 0 milliseconds to start the updates immediately
        handler.postDelayed(locationRunnable, 0);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(Layouts.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions,
                                           @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(Layouts.this, "Please provide the required permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    boolean firestoreChecker(String s) {
        Query query = Utility.getCollectionReference().whereEqualTo("locate", s);
        List<String> set = new ArrayList<>();
        FirestoreRecyclerOptions<Lnote> options = new FirestoreRecyclerOptions.Builder<Lnote>()
                .setQuery(query, Lnote.class).build();
        if (!options.getSnapshots().isEmpty()) {
        } else {
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String loc = document.getString("locate");
                    loc.toLowerCase();
                    boolean flag=document.getBoolean("flag");
                    if (loc.equals(s) && flag==true) {
                        showNotification("You reached "+s);
                        String documentId = document.getId();
                        Lnote lnote=new Lnote();
                        lnote.setLocate(loc);
                        lnote.setFlag(false);
                        savenotetoFirebase(lnote,documentId);
                        Toast.makeText(Layouts.this, "You reached " + loc, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Layouts.this, flag + loc, Toast.LENGTH_SHORT).show();
                    }
                    set.add(loc);
                    Log.d("images", flag + "" + "");
                }
                //System.out.println(set);
            }).addOnFailureListener(e -> {
                Log.w(TAG, "Error getting documents.", e);
            });
            //System.out.println(set);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the callback to stop location updates when the activity is destroyed
        handler.removeCallbacks(locationRunnable);
    }

    void showNotification(String message) {
        // Notification ID
        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() , default_notification_channel_id ) ;
        mBuilder.setContentTitle( "My Notification" ) ;
        mBuilder.setContentText( message) ;
        mBuilder.setTicker( "Notification Listener Service Example" ) ;
        mBuilder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        mBuilder.setAutoCancel( true ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
    }
    void savenotetoFirebase(Lnote note,String docId) {

        DocumentReference documentReference;
        {
            documentReference = Utility.getCollectionReference().document(docId);
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Layouts.this, "You reached your destination", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Layouts.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStop () {
        super .onStop() ;
        startService( new Intent( this, NotificationService. class )) ;
    }
    public void closeApp (View view) {
        finish() ;
    }
}