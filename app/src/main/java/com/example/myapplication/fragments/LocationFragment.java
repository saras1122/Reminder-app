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

import com.example.myapplication.AddAlarml;
import com.example.myapplication.Ladapter;
import com.example.myapplication.Lalarm;
import com.example.myapplication.Lnote;
import com.example.myapplication.R;
import com.example.myapplication.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class LocationFragment extends Fragment {
    FloatingActionButton plus;
    RecyclerView recyclerView;
    ImageButton menu;
    Ladapter noteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_location, container, false);
        plus=view.findViewById(R.id.notebuton);
        plus.setOnClickListener((v) -> startActivity(new Intent(getContext(), AddAlarml.class)));
        recyclerView=view.findViewById(R.id.recyclerview);
        setupRecyclerView();
        return view;
    }
    void setupRecyclerView(){
        Query query= Utility.getCollectionReference();
        FirestoreRecyclerOptions<Lnote> options=new FirestoreRecyclerOptions.Builder<Lnote>()
                .setQuery(query,Lnote.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        noteAdapter =new Ladapter(options,requireContext());
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