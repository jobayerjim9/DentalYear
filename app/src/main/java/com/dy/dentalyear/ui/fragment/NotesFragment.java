package com.dy.dentalyear.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dy.dentalyear.R;
import com.dy.dentalyear.databinding.FragmentNotesBinding;
import com.dy.dentalyear.ui.activity.WritingActivity;
import com.google.android.material.chip.ChipGroup;

import static com.dy.dentalyear.model.constant.AppConstants.NOTE_TYPE;


public class NotesFragment extends Fragment {
    FragmentNotesBinding binding;
    private int type=0;
    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_notes,container,false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        final FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction()
                .replace(R.id.container, new NotesListFragment(), "notes")
                .setReorderingAllowed(true)
                .addToBackStack("notes") // name can be null
                .commit();
        binding.chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                if (checkedId==R.id.notesChip) {
                    type=0;
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new NotesListFragment(), "notes")
                            .setReorderingAllowed(true)
                            .addToBackStack("notes") // name can be null
                            .commit();
                } else if (checkedId==R.id.goalsChip) {
                    type=1;
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new GoalsListFragment(), "goals")
                            .setReorderingAllowed(true)
                            .addToBackStack("goals") // name can be null
                            .commit();
                }
            }
        });
        binding.notesChip.setChecked(true);
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("typeCheck",type+"");
                Intent intent=new Intent(requireActivity(), WritingActivity.class);
                intent.putExtra(NOTE_TYPE,type);
                startActivity(intent);
            }
        });

    }
}