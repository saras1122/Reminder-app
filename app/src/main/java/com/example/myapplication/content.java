package com.example.myapplication;

import static com.example.myapplication.Notification.messageExtra;
import static com.example.myapplication.Notification.titleExtra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class content extends AppCompatActivity {
    TextView titleET1,messageET1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        titleET1=findViewById(R.id.title1);
        messageET1=findViewById(R.id.task1);
        Intent intent =getIntent();
        String title1 = intent.getStringExtra(titleExtra);
        String message = intent.getStringExtra(messageExtra);
        titleET1.setText(title1);
        messageET1.setText(message);
    }
}