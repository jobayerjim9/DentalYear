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

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.adapters.NotesRecyclerAdapter;
import com.dy.dentalyear.controller.localdb.DatabaseAccess;
import com.dy.dentalyear.databinding.FragmentNotesBinding;
import com.dy.dentalyear.databinding.FragmentNotesListBinding;
import com.dy.dentalyear.model.local.NotesModel;
import com.dy.dentalyear.ui.activity.WritingActivity;

import java.util.ArrayList;

public class NotesListFragment extends Fragment {
    FragmentNotesListBinding binding;
    private ArrayList<NotesModel> notesModels;
    private NotesRecyclerAdapter notesRecyclerAdapter;
    private FragmentManager fragmentManager;


    public NotesListFragment(FragmentManager fragmentManager) {
        // Required empty public constructor
        notesModels = new ArrayList<>();
        this.fragmentManager = fragmentManager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_notes_list,container,false);

        initView();
        return binding.getRoot();

    }

    private void initView() {
        binding.notesListRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        notesRecyclerAdapter = new NotesRecyclerAdapter(requireContext(), notesModels, fragmentManager);
        binding.notesListRecycler.setAdapter(notesRecyclerAdapter);
        getNotes();

    }

    @Override
    public void onResume() {
        super.onResume();
        getNotes();

    }

    public void getNotes() {
        notesModels.clear();
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(requireContext());
        databaseAccess.open();
        notesModels.addAll(databaseAccess.getAllNotes(0));
        notesRecyclerAdapter.notifyDataSetChanged();
        if (notesModels.size()==0) {
            binding.noItem.setVisibility(View.VISIBLE);
        }
        else {
            binding.noItem.setVisibility(View.GONE);
        }

    }
}