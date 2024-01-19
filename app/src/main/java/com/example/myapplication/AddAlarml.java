package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class AddAlarml extends AppCompatActivity {
    EditText titleET,messageET;
    ImageButton menu;
    boolean isEdit=false;
    String docId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarml);
        titleET=findViewById(R.id.titleET);
        menu=findViewById(R.id.menu1);
        docId=getIntent().getStringExtra("docId");
        Button cancelButton = findViewById(R.id.cancelButton);
        if(docId!=null && !docId.isEmpty()){
            isEdit=true;
        }
        if(isEdit){
            cancelButton.setVisibility(View.VISIBLE);
            String loc=getIntent().getStringExtra("locate");
            titleET.setText(loc);
        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduleNotification();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletelocationfromFirebase();
            }
        });
    }

    void deletelocationfromFirebase() {
        DocumentReference documentReference=Utility.getCollectionReference().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddAlarml.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(AddAlarml.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void scheduleNotification() {
        String title = titleET.getText().toString();
        if(title.isEmpty() || title==null){
            titleET.setError("Please Enter Location :)");
            return;
        }
        Lnote lnote=new Lnote();
        lnote.setLocate(title);
        lnote.setFlag(true);
        savenotetoFirebase(lnote);
    }
    void savenotetoFirebase(Lnote note) {

        DocumentReference documentReference;
        if(isEdit){
            documentReference=Utility.getCollectionReference().document(docId);
        }
        else
        {
            documentReference = Utility.getCollectionReference().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddAlarml.this, "City added", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddAlarml.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}