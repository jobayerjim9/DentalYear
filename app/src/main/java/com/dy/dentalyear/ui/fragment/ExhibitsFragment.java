package com.dy.dentalyear.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.adapters.SponsorsRecyclerAdapter;
import com.dy.dentalyear.controller.apis.ApiClient;
import com.dy.dentalyear.controller.apis.ApiInterface;
import com.dy.dentalyear.databinding.FragmentExhibitsBinding;
import com.dy.dentalyear.model.api.SponsorsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExhibitsFragment extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentExhibitsBinding binding;
    private ArrayList<SponsorsResponse> sponsorsResponses = new ArrayList<>();
    private SponsorsRecyclerAdapter sponsorsRecyclerAdapter;


    public ExhibitsFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;

    }

    public void showMain() {
        binding.mainLayout.setVisibility(View.VISIBLE);
    }

    public void hideMain() {
        binding.mainLayout.setVisibility(View.GONE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exhibits, container, false);
        initView();

        return binding.getRoot();
    }

    private void initView() {

        binding.exhibitsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        sponsorsRecyclerAdapter = new SponsorsRecyclerAdapter(requireContext(), sponsorsResponses, fragmentManager);
        binding.exhibitsRecycler.setAdapter(sponsorsRecyclerAdapter);
        getAllSponsorsData();
    }

    private void getAllSponsorsData() {
        binding.setLoading(true);
        ApiInterface apiInterface = ApiClient.getClient(requireContext()).create(ApiInterface.class);
        Call<ArrayList<SponsorsResponse>> call = apiInterface.getAllSponsors();
        call.enqueue(new Callback<ArrayList<SponsorsResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<SponsorsResponse>> call, Response<ArrayList<SponsorsResponse>> response) {
                binding.setLoading(false);

                if (response.body() != null) {
                    sponsorsResponses.clear();
                    sponsorsResponses.addAll(response.body());
                    sponsorsRecyclerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<SponsorsResponse>> call, Throwable t) {
                binding.setLoading(false);
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}