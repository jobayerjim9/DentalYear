package com.dy.dentalyear.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.adapters.HomeVPAdapter;
import com.dy.dentalyear.databinding.ActivityMainBinding;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_main);

        init();
    }

    private void init() {

        binding.bottomNav.add(new MeowBottomNavigation.Model(0,R.drawable.ic_home_active));
        binding.bottomNav.add(new MeowBottomNavigation.Model(1,R.drawable.ic_edit));
        binding.bottomNav.add(new MeowBottomNavigation.Model(2,R.drawable.ic_shopping_cart));
        binding.bottomNav.add(new MeowBottomNavigation.Model(3,R.drawable.ic_play));
        binding.bottomNav.show(0,true);
        HomeVPAdapter homeVPAdapter=new HomeVPAdapter(getSupportFragmentManager(),getLifecycle());
        binding.homeViewPager.setAdapter(homeVPAdapter);
        binding.bottomNav.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                // YOUR CODES
                binding.homeViewPager.setCurrentItem(model.getId(),true);
                return null;
            }
        });
        binding.homeViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.bottomNav.show(position,true);
            }
        });
    }
}