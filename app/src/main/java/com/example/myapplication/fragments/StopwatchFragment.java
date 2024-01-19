package com.example.myapplication.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

public class StopwatchFragment extends Fragment {
    TextView timer ;
    Button start, pause, reset;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    ImageButton play,stop12;
    int Seconds, Minutes, MilliSeconds ;
    boolean flag=true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_stopwatch, container, false);
        timer=view.findViewById(R.id.tvTimer);
        play=view.findViewById(R.id.play);
        stop12=view.findViewById(R.id.stop12);

        handler = new Handler() ;

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer currentImageResource = (Integer) play.getTag();
                    Log.d("", currentImageResource + " " + R.drawable.baseline_play_arrow_24);
                    if (flag) {
                        Log.d("","");
                        play.setImageResource(R.drawable.baseline_pause_24);
                        StartTime = SystemClock.uptimeMillis();
                        handler.postDelayed(runnable, 0);
                        flag=false;
                    } else {
                        // If the current image is not the "start" drawable, change it to the "start" drawable
                        TimeBuff += MillisecondTime;
                        handler.removeCallbacks(runnable);
                        play.setImageResource(R.drawable.baseline_play_arrow_24);
                        flag=true;
                    }

            }
        });


        stop12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeBuff = 0L ;
                handler.removeCallbacks(runnable);
                MillisecondTime = 0L ;
                StartTime = 0L ;
                TimeBuff = 0L ;
                UpdateTime = 0L ;
                Seconds = 0 ;
                Minutes = 0 ;
                MilliSeconds = 0 ;
                play.setImageResource(R.drawable.baseline_play_arrow_24);
                flag=true;
                timer.setText("00:00:00");

            }
        });
        return view;
    }
    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            timer.setText("" +String.format("%02d" ,Minutes) + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%01d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };
}