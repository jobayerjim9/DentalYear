package com.dy.dentalyear.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dy.dentalyear.R;
import com.dy.dentalyear.databinding.FragmentExhibitsBinding;
import com.dy.dentalyear.databinding.FragmentExhibitsViewBinding;
import com.dy.dentalyear.model.api.SponsorsResponse;
import com.dy.dentalyear.model.constant.AppConstants;
import com.squareup.picasso.Picasso;


public class ExhibitsViewFragment extends Fragment {
    private FragmentManager fragmentManager;
    private SponsorsResponse sponsorsResponse;
    private FragmentExhibitsViewBinding binding;

    public ExhibitsViewFragment(FragmentManager fragmentManager, SponsorsResponse sponsorsResponse) {
        this.fragmentManager = fragmentManager;
        this.sponsorsResponse = sponsorsResponse;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exhibits_view, container, false);
        binding.setData(sponsorsResponse);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExhibitsFragment exhibitsFragment = (ExhibitsFragment) fragmentManager.findFragmentByTag(AppConstants.HOME_FRAGMENT_TAGS.get(2));
                if (exhibitsFragment != null) {
                    exhibitsFragment.showMain();
                }
                fragmentManager.popBackStack();
            }
        });
        Picasso.get().load(binding.getData().getAcf().getSponsor_logo()).resize(100, 100).into(binding.sponsorLogo);
        binding.viewOnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(binding.getData().getAcf().getSponsor_link()));
                startActivity(browserIntent);
            }
        });

    }
}