package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Ladapter extends FirestoreRecyclerAdapter<Lnote, LocationViewholder> {
    Context context;
    public Ladapter(@NonNull FirestoreRecyclerOptions<Lnote> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull LocationViewholder holder, int position, @NonNull Lnote model) {
        holder.locate.setText(model.locate);
        if(model.flag==false){
            holder.completed.setTextColor(Color.GREEN);
            holder.completed.setText("Completed");
        }
        holder.itemView.setOnClickListener((v)->{
            Intent intent=new Intent(context,AddAlarml.class);
            intent.putExtra("locate",model.getLocate());
            String docId=this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public LocationViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.location,parent,false);
        return new LocationViewholder(view);
    }
}
    class LocationViewholder extends RecyclerView.ViewHolder {
        TextView locate,completed,date;
        Switch aSwitch;

        public LocationViewholder(@NonNull View itemView) {
            super(itemView);
            locate=itemView.findViewById(R.id.city);
            completed=itemView.findViewById(R.id.completed);
        }
    }

