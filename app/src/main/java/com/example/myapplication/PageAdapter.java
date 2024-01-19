package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.fragments.EventFragment;
import com.example.myapplication.fragments.LocationFragment;
import com.example.myapplication.fragments.StopwatchFragment;

public class PageAdapter  extends FragmentStateAdapter {
    public PageAdapter(@NonNull FragmentActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new EventFragment();
            case 1:
                return new LocationFragment();
            case 2:
                return new StopwatchFragment();
            default:
                return new EventFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
