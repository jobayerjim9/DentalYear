package com.dy.dentalyear.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.dy.dentalyear.R;
import com.dy.dentalyear.databinding.ActivityMainBinding;
import com.dy.dentalyear.model.constant.AppConstants;
import com.dy.dentalyear.ui.fragment.ExhibitsFragment;
import com.dy.dentalyear.ui.fragment.HomeFragment;
import com.dy.dentalyear.ui.fragment.NotesFragment;
import com.dy.dentalyear.ui.fragment.VideoFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.util.ArrayList;
import java.util.Arrays;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<Fragment> allFragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);

        init();
    }

    private void init() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        binding.bottomNav.add(new MeowBottomNavigation.Model(0, R.drawable.ic_home_active));
        binding.bottomNav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_edit));
        binding.bottomNav.add(new MeowBottomNavigation.Model(2, R.drawable.ic_shopping_cart));
        binding.bottomNav.add(new MeowBottomNavigation.Model(3, R.drawable.ic_play));
        allFragments.add(new HomeFragment(fragmentManager));
        allFragments.add(new NotesFragment(fragmentManager));
        allFragments.add(new ExhibitsFragment(fragmentManager));
        allFragments.add(new VideoFragment(fragmentManager));
        for (int i = 0; i < allFragments.size(); i++) {
            fragmentManager.beginTransaction()
                    .add(R.id.mainContainer, allFragments.get(i), AppConstants.HOME_FRAGMENT_TAGS.get(i))
                    .setReorderingAllowed(true)
                    .addToBackStack(AppConstants.HOME_FRAGMENT_TAGS.get(i)) // name can be null
                    .commit();
        }
        binding.bottomNav.setBackgroundColor(Color.parseColor("#AAE8FF"));
        binding.bottomNav.setOnClickMenuListener(model -> {
            binding.bottomNav.show(model.getId(), true);
            return null;
        });
        binding.bottomNav.setOnShowListener(model -> {
            Log.d("onShow", "called");
            for (int i = 0; i < allFragments.size(); i++) {
                fragmentManager.beginTransaction().hide(allFragments.get(i)).commit();
            }
            fragmentManager.beginTransaction().show(allFragments.get(model.getId())).commit();
            return null;
        });
        binding.bottomNav.show(0, true);
    }

    @Override
    public void onBackPressed() {

    }
}