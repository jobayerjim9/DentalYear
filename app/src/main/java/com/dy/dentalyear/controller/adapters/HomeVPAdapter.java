package com.dy.dentalyear.controller.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dy.dentalyear.ui.fragment.ExhibitsFragment;
import com.dy.dentalyear.ui.fragment.HomeFragment;
import com.dy.dentalyear.ui.fragment.NotesFragment;
import com.dy.dentalyear.ui.fragment.VideoFragment;

public class HomeVPAdapter extends FragmentStateAdapter {


    public HomeVPAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0) {
            return new HomeFragment();
        } else if (position==1) {
            return new NotesFragment();
        } else if (position==2) {
            return new ExhibitsFragment();
        } else if (position==3) {
            return new VideoFragment();
        }
        return null;

    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
