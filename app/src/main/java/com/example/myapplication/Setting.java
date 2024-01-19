package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends AppCompatActivity {
    Spinner spinner;
    TextView textView1;
    ImageButton save;
    String s="default";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        TextView textView = findViewById(R.id.text3);
        save =findViewById(R.id.save);
        textView1=findViewById(R.id.text4);
        // Sample data for the spinner
        String[] items = {"Item 1", "Item 2", "Item 3"};

        //spinner.setVisibility(View.INVISIBLE);
        // Set a click listener for the TextView to show the dropdown
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Setting.this, textView);

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        textView1.setText(menuItem.getTitle());
                        s=menuItem.getTitle().toString();
                        Intent i = new Intent(Setting.this, MainActivity2.class);
                        i.putExtra("key",menuItem.getTitle());
                        DataHolder.getInstance().setData(menuItem.getTitle().toString());
                        // Toast message on menu item clicked
                        Toast.makeText(Setting.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
    }
}