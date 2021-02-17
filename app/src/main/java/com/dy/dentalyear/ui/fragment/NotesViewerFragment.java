package com.dy.dentalyear.ui.fragment;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.helpers.Utils;
import com.dy.dentalyear.databinding.FragmentNotesViewerBinding;
import com.dy.dentalyear.model.constant.AppConstants;
import com.dy.dentalyear.model.local.NotesModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NotesViewerFragment extends Fragment {
    FragmentNotesViewerBinding binding;
    private NotesModel notesModel;
    private FragmentManager fragmentManager;

    public NotesViewerFragment(NotesModel notesModel, FragmentManager fragmentManager) {
        this.notesModel = notesModel;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes_viewer, container, false);
        binding.setData(notesModel);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = format.parse(notesModel.getDate());
            String placeHolder = Utils.getMonthForInt(date.getMonth()) + " " + date.getDate() + "," + (date.getYear() + 1900);
            binding.dateViewer.setText(placeHolder);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotesFragment notesFragment = (NotesFragment) fragmentManager.findFragmentByTag(AppConstants.HOME_FRAGMENT_TAGS.get(1));
                if (notesFragment != null) {
                    notesFragment.showTopBar();
                }
                fragmentManager.popBackStack();
            }
        });

        return binding.getRoot();

    }
}