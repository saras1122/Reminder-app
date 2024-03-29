package com.example.myapplication;

import static com.example.myapplication.Notification.channelID;
import static com.example.myapplication.Notification.messageExtra;
import static com.example.myapplication.Notification.notificationID;
import static com.example.myapplication.Notification.silentExtra;
import static com.example.myapplication.Notification.titleExtra;
import static com.example.myapplication.Notification.musicExtra;
import static com.example.myapplication.Notification.repeatExtra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainActivity2 extends AppCompatActivity {
    Button submitButton;
    ImageButton menu;
    private static int MAX_REPEATS = 4;
    DatePicker datePicker;
    TimePicker timePicker;
    TextView pageTitle, textView, textView1;
    CheckBox ch4, ch1, ch2, ch3,ch5,ch6;
    EditText titleET,messageET;
    String title,content,docId,timestamp;
    String silent="true";
    Spinner spinner1,spinner2;
    private Map<Integer,String> map=new HashMap();
    boolean isEdit=false;
    String music="default";
    int numberOfRepeats=0;
    boolean isAlarmCanceled = false;
    boolean repeat=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
//         db = FirebaseFirestore.getInstance();
        createNotificationChannel();
        pageTitle=findViewById(R.id.hello);
        ch2=(CheckBox)findViewById(R.id.checkBox2);
        ch1=(CheckBox)findViewById(R.id.checkBox1);
        ch3=(CheckBox)findViewById(R.id.checkBox3);
        ch4=(CheckBox)findViewById(R.id.checkBox4);
        ch5=(CheckBox)findViewById(R.id.checkBox5);
        ch6=(CheckBox)findViewById(R.id.checkBox6);
        datePicker=findViewById(R.id.datePicker);
        timePicker=findViewById(R.id.timePicker);
        textView = findViewById(R.id.text3);
        textView1=findViewById(R.id.text4);
        titleET=findViewById(R.id.titleET);
        spinner1=findViewById(R.id.spinner1);
        spinner2=findViewById(R.id.spinner2);
        String[] items = new String[]{"None","30 minute", "1 hour", "1.30 hour","2 hour",
                "2.30 hour", "3 hour","3.30 hour", "4 hour", "4.30 hour","5 hour","5.30 hour","6 hour","6.30 hour",
                "7 hour","7.30 hour","8 hour"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spinner1.setAdapter(adapter);
        String[] items1 = new String[]{"None","1", "2", "3", "4","5","6","7","8"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        spinner2.setAdapter(adapter1);
        Switch sw = (Switch) findViewById(R.id.switch1);
        Switch s1 = (Switch) findViewById(R.id.switch2);
        menu=findViewById(R.id.menu1);
        messageET=findViewById(R.id.messageET);
        //recieve
        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");
        timestamp=getIntent().getStringExtra("date");
        Button cancelButton = findViewById(R.id.cancelButton);
        if(docId!=null && !docId.isEmpty()){
            isEdit=true;
        }
        titleET.setText(title);
        messageET.setText(content);

        if(isEdit){
            ArrayList<Integer> checkboxList=getIntent().getIntegerArrayListExtra("checkbox");
            //ArrayList<String> days=getIntent().getStringArrayListExtra("days");
            int[] a = new int[checkboxList.size()];
            for (int i = 0; i < checkboxList.size(); i++) {
                a[i] = checkboxList.get(i);
            }
            if(a[0]==1){
                ch1.setChecked(true);
            }
            if(a[1]==1){
                ch2.setChecked(true);
            }
            if(a[2]==1){
                ch3.setChecked(true);
            }
            if(a[3]==1){
                ch4.setChecked(true);
            }
            if(a[4]==1){
                ch5.setChecked(true);
            }
            if(a[5]==1){
                ch6.setChecked(true);
            }
            String sessionId = getIntent().getStringExtra("text");
            //boolean te=getIntent().getBooleanExtra("repeat",true);

            music=sessionId;
            textView1.setText(sessionId);
            pageTitle.setText("Edit your note");
            cancelButton.setVisibility(View.VISIBLE);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity2.this, textView);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        textView1.setText(menuItem.getTitle());
                        music=menuItem.getTitle().toString();
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MAX_REPEATS=0;
                isAlarmCanceled=true;
                cancelScheduledAlarms();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleNotification();
            }
        });
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Intent i=new Intent(getApplicationContext(),Notification.class);
                    i.putExtra("check",true);
                } else {
                    // The toggle is disabled
                    silent="false";
                    Intent i=new Intent(getApplicationContext(),Notification.class);
                    i.putExtra("check",false);
                }
            }
        });
        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                } else {
                    // The toggle is disabled
                    repeat=false;
                }
            }
        });
    }
    private void scheduleNotification() {
        Intent intent = new Intent(getApplicationContext(), Notification.class);
        String title = titleET.getText().toString();
        String message = messageET.getText().toString();
        //String id=
        long time = getTime();
        Note note = new Note();
        note.setText1(music);
        note.setSilent(silent);
        note.setRepeat(repeat);
        note.setTimes(String.valueOf(spinner2.getSelectedItemPosition()));
        note.setTimer(String.valueOf(spinner1.getSelectedItemPosition()));
        note.setTitle(title);
        Date date = new Date(time);
        DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(getApplicationContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getApplicationContext());
        note.setContent(timeFormat.format(date));
        note.setTimestamp(dateFormat.format(time));
        intent.putExtra(silentExtra,silent);
        intent.putExtra(titleExtra, title);
        intent.putExtra(messageExtra, message);
        intent.putExtra(musicExtra,music);
        if(repeat==true)
            intent.putExtra(repeatExtra,"true");
        else
            intent.putExtra(repeatExtra,"false");
        ArrayList<Integer> selectedDays = new ArrayList<>();
        int []a=new int[7];
        for(int i:a){
            a[i]=0;
        }
        if (ch1.isChecked()) {
            a[0]=1;
            selectedDays.add(Calendar.MONDAY);
        }
        if (ch2.isChecked()) {
            a[1]=1;
            selectedDays.add(Calendar.TUESDAY);
        }
        if (ch3.isChecked()) {
            a[2]=1;
            selectedDays.add(Calendar.WEDNESDAY);
        }
        if (ch4.isChecked()) {
            a[3]=1;
            selectedDays.add(Calendar.THURSDAY);
        }
        if (ch5.isChecked()) {
            a[4]=1;
            selectedDays.add(Calendar.FRIDAY);
        }
        if (ch6.isChecked()) {
            a[5]=1;
            selectedDays.add(Calendar.SATURDAY);
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i:a){
            arrayList.add(i);
        }
        note.setArr(arrayList);
        if (!selectedDays.isEmpty()) {
            int uniqueNotificationID = (int) System.currentTimeMillis(); // Unique ID for each notification
            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(time);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            ArrayList<String> getDay=new ArrayList<>();
            ArrayList<String> ids2=new ArrayList<>();
            for (int selectedDay : selectedDays) {
                getDay.add(String.valueOf(uniqueNotificationID));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), uniqueNotificationID, intent,
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
                );
                note.setId(String.valueOf(uniqueNotificationID));
                uniqueNotificationID++;
                Log.d("AlarmDebug", "Scheduling alarm for day: " + selectedDay);
                int daysToAdd = 0;
                if (selectedDay != dayOfWeek) {
                    daysToAdd = (selectedDay + 7 - dayOfWeek) % 7;
                }
                else if (selectedDay == dayOfWeek) {
                    daysToAdd = 7; // Set to 7 to schedule for the same day next week
                }
                Toast.makeText(MainActivity2.this,"1 "+selectedDay,Toast.LENGTH_SHORT).show();
                Calendar nextSelectedDay = (Calendar) calendar.clone();
                nextSelectedDay.add(Calendar.DAY_OF_YEAR, daysToAdd);
                nextSelectedDay.set(Calendar.HOUR_OF_DAY, 0);
                nextSelectedDay.set(Calendar.MINUTE, 0);
                long nextSelectedDayTime = nextSelectedDay.getTimeInMillis();
                Log.d("AlarmDebug", "Alarm time for day " + selectedDay + ": " + map);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                if (dayOfWeek == selectedDay && nextSelectedDayTime > System.currentTimeMillis()) {
                    if(repeat){
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                time+24*60*60*1000*(selectedDay-2),
                                AlarmManager.INTERVAL_DAY*7,// Repeat every week
                                pendingIntent);
                        ids2.add(String.valueOf((int)(uniqueNotificationID+7*3000)));
                        Toast.makeText(MainActivity2.this,"1 ",Toast.LENGTH_SHORT).show();
                    }else {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                time, // Repeat every week
                                pendingIntent);
                        ids2.add(String.valueOf((int)(uniqueNotificationID)));
                        Toast.makeText(MainActivity2.this,"2 ",Toast.LENGTH_SHORT).show();
                    }

                }else {
                    if(repeat){
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                nextSelectedDayTime+24*60*60*1000*(selectedDay-2),
                                AlarmManager.INTERVAL_DAY * 7,// Repeat every week
                                pendingIntent);
                        uniqueNotificationID+=10;
                        pendingIntent=PendingIntent.getBroadcast(
                                getApplicationContext(), (int)(uniqueNotificationID), intent,
                                PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
                        );
                        ids2.add(String.valueOf((int)(uniqueNotificationID)));
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                nextSelectedDayTime,
                                pendingIntent);
                        uniqueNotificationID+=10;
                        Toast.makeText(MainActivity2.this,"3 ",Toast.LENGTH_SHORT).show();
                    }else {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                                nextSelectedDayTime, // Repeat every week
                                pendingIntent);
                        ids2.add(String.valueOf((int)(uniqueNotificationID)));
                        Toast.makeText(MainActivity2.this,"4 ",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            ArrayList<String> ids=new ArrayList<>();
            ids.add(String.valueOf(uniqueNotificationID));
            note.setDays(getDay);
            note.setMids(ids);
            note.setIds2(ids2);
        }else{
            int uniqueNotificationID = (int) System.currentTimeMillis();
            note.setId(String.valueOf(uniqueNotificationID));
            intent.putExtra(String.valueOf(notificationID),uniqueNotificationID);
            Log.d("string" , "Here the id" + note.getId());
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                    getApplicationContext(), uniqueNotificationID, intent,
//                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
//            );
            ArrayList<String> ids=new ArrayList<>();
            ArrayList<String> ids2=new ArrayList<>();
            ArrayList<String> getDay=new ArrayList<>();
            int temp1=spinner1.getSelectedItemPosition();
            int temp2=spinner2.getSelectedItemPosition();
            if((temp1!=0 && temp2==0) || (temp1==0)){
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), uniqueNotificationID, intent,
                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
                );
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        time, // Repeat every week
                        pendingIntent);
                Toast.makeText(MainActivity2.this,temp1 +" "+ temp2,Toast.LENGTH_SHORT).show();
                ids.add(String.valueOf(uniqueNotificationID));
                ids2.add(String.valueOf(uniqueNotificationID));
                getDay.add(String.valueOf(uniqueNotificationID));
            }else{
                Toast.makeText(MainActivity2.this,temp1 +" "+ temp2,Toast.LENGTH_SHORT).show();
                for (int i=0;i<temp2;i++) {
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            getApplicationContext(), uniqueNotificationID, intent,
                            PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
                    );
                    uniqueNotificationID++;
                    ids.add(String.valueOf(uniqueNotificationID));
                    ids2.add(String.valueOf(uniqueNotificationID));
                    getDay.add(String.valueOf(uniqueNotificationID));
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                            time, // Repeat every week
                            pendingIntent);
                    Toast.makeText(MainActivity2.this,time+" ",Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity2.this,i+" ",Toast.LENGTH_SHORT).show();
                    time+=30*60*10000*temp1;
                }
            }
            //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            note.setMids(ids);
            note.setIds2(ids2);
            note.setDays(getDay);
            //alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
            //scheduleRepeatingAlarmInternal(alarmManager,pendingIntent);
        }
        savenotetoFirebase(note);
        //showAlert(time, title, message);
    }
    private void savenotetoFirebase(Note note) {
        DocumentReference documentReference;
        if(isEdit){
            cancelScheduledAlarms();
            documentReference=Utility.getCollectionReferenceForNotes().document();
        }
        else{
            documentReference=Utility.getCollectionReferenceForNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity2.this, "Note added", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(MainActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        CollectionReference dbCourses = db.collection("Courses");
//        dbCourses.add(note).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentReference> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(MainActivity2.this, "Note added", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                else{
//                    Toast.makeText(MainActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }


    private long getTime() {
        int minute = timePicker.getMinute();
        int hour = timePicker.getHour();
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar.getTimeInMillis();
    }

    private void createNotificationChannel() {
        String name = "Notif Channel";
        String desc = "A Description of the Channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelID, name, importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setDescription(desc);
        }
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void cancelScheduledAlarms() {
        DocumentReference documentReference;
            documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity2.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(MainActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent = new Intent(getApplicationContext(), Notification.class);
        String s=getIntent().getStringExtra("id");
        ArrayList<String> al=new ArrayList<>(getIntent().getStringArrayListExtra("Mids"));
        ArrayList<String> al1=new ArrayList<>(getIntent().getStringArrayListExtra("days"));
        ArrayList<String> al2=new ArrayList<>(getIntent().getStringArrayListExtra("ids2"));
        if(al!=null) {
            for (int i = 0; i < al.size(); i++) {
                int temp = Integer.valueOf(al.get(i));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), temp, intent, PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            }
        }
        if(al1!=null) {
            for (int i = 0; i < al1.size(); i++) {
                int temp = Integer.valueOf(al1.get(i));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), temp, intent, PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                Toast.makeText(this, "Alarms"+i, Toast.LENGTH_SHORT).show();
            }
        }
        if(al2!=null) {
            for (int i = 0; i < al2.size(); i++) {
                int temp = Integer.valueOf(al2.get(i));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), temp, intent, PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                Toast.makeText(this, "Alarms"+i, Toast.LENGTH_SHORT).show();
            }
        }
        int res=Integer.valueOf(s);
        Log.d("AlarmDebug", "Alarm time  "  + ": " + res + " "+ s);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), res, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarms canceled", Toast.LENGTH_SHORT).show();
    }
    private String generateUniqueId() {
        // Generate a unique identifier using timestamp and random UUID
        long timestamp = System.currentTimeMillis();
        UUID uuid = UUID.randomUUID();
        String uniqueId = timestamp + "_" + uuid.toString();
        return uniqueId;
    }

    private void showAlert(long time, String title, String message) {
        Date date = new Date(time);
        DateFormat dateFormat =
                android.text.format.DateFormat.getLongDateFormat(getApplicationContext());
        DateFormat timeFormat =
                android.text.format.DateFormat.getTimeFormat(getApplicationContext());

        new AlertDialog.Builder(this)
                .setTitle("Notification Scheduled")
                .setMessage(
                        "Title: " + title +
                                "\nMessage: " + message +
                                "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
//    void sh(){
//        if (!selectedDays.isEmpty()) {
//            int uniqueNotificationID = (int) System.currentTimeMillis(); // Unique ID for each notification
//            Calendar calendar = Calendar.getInstance();
//
//            calendar.setTimeInMillis(time);
//            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//            note.setId(String.valueOf(uniqueNotificationID));
//            ArrayList<String> ids=new ArrayList<>();
//            ids.add(String.valueOf(uniqueNotificationID));
//            for (int i=0;i<3;i++) {
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                        getApplicationContext(), uniqueNotificationID, intent,
//                        PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT
//                );
//                uniqueNotificationID++;
//                ids.add(String.valueOf(uniqueNotificationID));
//                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
//                            time, // Repeat every week
//                            pendingIntent);
//                    time+=30000;
//            }
//        }
//    }
}