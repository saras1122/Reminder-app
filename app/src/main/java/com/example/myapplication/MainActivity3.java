package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity3 extends AppCompatActivity {
    FloatingActionButton plus;
    RecyclerView recyclerView;
    ImageButton menu;
    Adapter noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
//        db = FirebaseFirestore.getInstance();
        plus=findViewById(R.id.notebuton);
        recyclerView=findViewById(R.id.recyclerview);
        menu=findViewById(R.id.menu1);
        plus.setOnClickListener((v) -> startActivity(new Intent(MainActivity3.this,MainActivity2.class)));
//        menu.setOnClickListener((v)->show());
        menu.setOnClickListener((v)->startActivity(new Intent(MainActivity3.this,Setting.class)));
        setupRecyclerView();
    }
    void show(){}
    void setupRecyclerView(){
        Query query=Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options=new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter =new Adapter(options,this);
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