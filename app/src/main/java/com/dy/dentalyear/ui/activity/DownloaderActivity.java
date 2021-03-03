package com.dy.dentalyear.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.adapters.DownloaderAdapter;
import com.dy.dentalyear.controller.helpers.LocalVideoItemClickListener;
import com.dy.dentalyear.controller.helpers.VideoItemClickListener;
import com.dy.dentalyear.controller.localdb.DatabaseAccess;
import com.dy.dentalyear.databinding.ActivityDownloaderBinding;
import com.dy.dentalyear.model.local.LocalVideo;

import java.util.ArrayList;

public class DownloaderActivity extends AppCompatActivity implements LocalVideoItemClickListener {
    ActivityDownloaderBinding binding;
    ArrayList<LocalVideo> localVideos = new ArrayList<>();
    DownloaderAdapter downloaderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_downloader);
        binding.imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        localVideos.addAll(databaseAccess.getAllVideo());
        databaseAccess.close();
        downloaderAdapter = new DownloaderAdapter(this, localVideos, this::onClickVideo);
        binding.localVideoRecycler.setLayoutManager(new LinearLayoutManager(this));
        binding.localVideoRecycler.setAdapter(downloaderAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        localVideos.clear();
        localVideos.addAll(databaseAccess.getAllVideo());
        downloaderAdapter.notifyDataSetChanged();
        databaseAccess.close();
    }

    @Override
    public void onClickVideo(LocalVideo localVideo) {
        Intent intent = new Intent();
        intent.putExtra("name", localVideo.getName());
        intent.putExtra("path", localVideo.getPath());
        intent.putExtra("duration", localVideo.getDuration());
        setResult(100, intent);
        finish();
    }
}