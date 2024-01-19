package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class Lalarm extends AppCompatActivity {
    FloatingActionButton plus;
    RecyclerView recyclerView;
    ImageButton menu;
    Ladapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lalarm);
        plus=findViewById(R.id.notebuton);
        plus.setOnClickListener((v) -> startActivity(new Intent(Lalarm.this,AddAlarml.class)));
        recyclerView=findViewById(R.id.recyclerview);
        setupRecyclerView();
    }
    void setupRecyclerView(){
        Query query=Utility.getCollectionReference();
        FirestoreRecyclerOptions<Lnote> options=new FirestoreRecyclerOptions.Builder<Lnote>()
                .setQuery(query,Lnote.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter =new Ladapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }
    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}