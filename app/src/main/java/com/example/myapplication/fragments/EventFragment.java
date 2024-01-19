package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.myapplication.Adapter;
import com.example.myapplication.MainActivity2;
import com.example.myapplication.MainActivity3;
import com.example.myapplication.Note;
import com.example.myapplication.R;
import com.example.myapplication.Setting;
import com.example.myapplication.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class EventFragment extends Fragment {
    FloatingActionButton plus;
    RecyclerView recyclerView;
    ImageButton menu;
    Adapter noteAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_event, container, false);
        plus=view.findViewById(R.id.notebuton);
        recyclerView=view.findViewById(R.id.recyclerview);
        plus.setOnClickListener((v) -> startActivity(new Intent(getContext(), MainActivity2.class)));
//        menu.setOnClickListener((v)->show());
        setupRecyclerView();
        return view;
    }
    void show(){}
    void setupRecyclerView(){
        Query query= Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options=new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        noteAdapter =new Adapter(options,requireContext());
        recyclerView.setAdapter(noteAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }
    @Override
    public void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}