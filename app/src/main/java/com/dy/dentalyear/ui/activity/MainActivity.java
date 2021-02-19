package com.dy.dentalyear.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.dy.dentalyear.R;
import com.dy.dentalyear.databinding.ActivityMainBinding;
import com.dy.dentalyear.model.constant.AppConstants;
import com.dy.dentalyear.ui.fragment.ExhibitsFragment;
import com.dy.dentalyear.ui.fragment.HomeFragment;
import com.dy.dentalyear.ui.fragment.NotesFragment;
import com.dy.dentalyear.ui.fragment.VideoFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<Fragment> allFragments = new ArrayList<>();
    ArrayList<TextView> bottomNavText = new ArrayList<>();
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        init();
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        addAllViews();
        for (int i = 0; i < allFragments.size(); i++) {
            fragmentManager.beginTransaction()
                    .add(R.id.mainContainer, allFragments.get(i), AppConstants.HOME_FRAGMENT_TAGS.get(i))
                    .setReorderingAllowed(true)
                    .addToBackStack(AppConstants.HOME_FRAGMENT_TAGS.get(i)) // name can be null
                    .commit();
        }
        binding.bottomNav.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        binding.bottomNav.setBackgroundBottomColor(ContextCompat.getColor(this, R.color.white));
        binding.bottomNav.setOnClickMenuListener(model -> {
            binding.bottomNav.show(model.getId(), false);
            return null;
        });
        binding.bottomNav.setOnShowListener(model -> {

            Log.d("onShow", "called");
            for (int i = 0; i < allFragments.size(); i++) {
                fragmentManager.beginTransaction().hide(allFragments.get(i)).commit();
                bottomNavText.get(i).setPressed(false);
            }
            fragmentManager.beginTransaction().show(allFragments.get(model.getId())).commit();
            bottomNavText.get(model.getId()).setPressed(true);
            return null;
        });
        binding.bottomNav.show(0, true);
    }

    private void addAllViews() {
        binding.bottomNav.add(new MeowBottomNavigation.Model(0, R.drawable.ic_home_active));
        binding.bottomNav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_edit));
        binding.bottomNav.add(new MeowBottomNavigation.Model(2, R.drawable.ic_exhibits_new));
        binding.bottomNav.add(new MeowBottomNavigation.Model(3, R.drawable.ic_play));
        allFragments.add(new HomeFragment(getSupportFragmentManager()));
        allFragments.add(new NotesFragment(getSupportFragmentManager()));
        allFragments.add(new ExhibitsFragment(getSupportFragmentManager()));
        allFragments.add(new VideoFragment(getSupportFragmentManager()));
        bottomNavText.add(binding.textView);
        bottomNavText.add(binding.textView2);
        bottomNavText.add(binding.textView3);
        bottomNavText.add(binding.textView4);
    }


    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Do you want to exit Dental Year?")
                .setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}