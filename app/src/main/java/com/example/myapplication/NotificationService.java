package com.example.myapplication;

import static android.content.ContentValues.TAG;
import static com.example.myapplication.Layouts.NOTIFICATION_CHANNEL_ID;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;
import java.util.Timer;

public class NotificationService extends Service {
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    Timer timer ;
    TimerTask timerTask ;
    String TAG = "Timers" ;
    int Your_X_SECS = 5 ;
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        String s=datePick();
        solve();
        return START_STICKY;
    }
    String solve() {
        String s = datePick();
        Query query = Utility.getCollectionReferenceForNotes();
        Set<String> al= new HashSet<>();
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        if (!options.getSnapshots().isEmpty()) {
        } else {
            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String date1 = document.getString("timestamp");
                    boolean repeat=document.getBoolean("repeat");
                    ArrayList<Integer>al1= (ArrayList<Integer>) document.get("arr");
                    String timeString = document.getString("content");

                    // Create a SimpleDateFormat object with the desired format and locale
                    SimpleDateFormat inputFormat = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

                    try {
                        // Parse the time string to a Date object
                        Date date = inputFormat.parse(timeString);
                        String amPmIndicator = new SimpleDateFormat("a", Locale.ENGLISH).format(date);

                        // Create a new SimpleDateFormat based on the initial AM/PM indicator
                        String outputFormatPattern = "h:mm a";
                        SimpleDateFormat outputFormat = new SimpleDateFormat(outputFormatPattern, Locale.ENGLISH);

                        long timeInMillis = outputFormat.parse(timeString).getTime();

                        // Print the results
                        System.out.println("Original Time String: " + timeString);
                        System.out.println("AM/PM: " + amPmIndicator);
                        System.out.println("Time in milliseconds: " + timeInMillis);

                    } catch (ParseException e) {
                        // Handle the ParseException
                        e.printStackTrace();
                    }
                    Date currentDate = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
                    String formattedDate = sdf.format(currentDate);
                    System.out.println(date1+"hdgd"+ " "+al1 +" "+repeat);
                }
            }).addOnFailureListener(e -> {
                Log.w(TAG, "Error getting documents.", e);
            });
            return s;
        }
        return "";
    }
    String datePick(){
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }

        // Add one year to the current date
        LocalDate futureDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            futureDate = currentDate.plusYears(1);
        }

        // Specify the desired date format
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        }
        String currentDateString="";
        // Format the dates to strings
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDateString = currentDate.format(formatter);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String futureDateString = futureDate.format(formatter);
        }
        return currentDateString;
    }


    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();

    }
    private void createNotification () {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() , default_notification_channel_id ) ;
        mBuilder.setContentTitle( "My Notification" ) ;
        mBuilder.setContentText( "Notification Listener Service Example" ) ;
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
}
