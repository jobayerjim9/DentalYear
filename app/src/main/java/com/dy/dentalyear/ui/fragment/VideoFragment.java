package com.dy.dentalyear.ui.fragment;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.adapters.VideosSelectionAdapter;
import com.dy.dentalyear.controller.apis.ApiClient;
import com.dy.dentalyear.controller.apis.ApiInterface;
import com.dy.dentalyear.controller.helpers.VideoItemClickListener;
import com.dy.dentalyear.controller.localdb.DatabaseAccess;
import com.dy.dentalyear.databinding.FragmentVideoBinding;
import com.dy.dentalyear.model.api.VideoCategoryResponse;
import com.dy.dentalyear.model.api.VideoResponse;
import com.dy.dentalyear.model.constant.AppConstants;
import com.dy.dentalyear.model.local.LocalVideo;
import com.dy.dentalyear.ui.activity.DownloaderActivity;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VideoFragment extends Fragment implements VideoItemClickListener {
    private FragmentManager fragmentManager;
    private FragmentVideoBinding binding;
    private ArrayList<VideoResponse> allVideo;
    private ArrayList<VideoResponse> filteredVideo;
    private VideosSelectionAdapter videosSelectionAdapter;
    private ArrayList<VideoCategoryResponse> videoCategories;
    private int currentCategory = -1;
    private int currentVideo = -1;


    public VideoFragment(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        allVideo = new ArrayList<>();
        filteredVideo = new ArrayList<>();
        videoCategories = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        isStoragePermissionGranted();
        initView();

        return binding.getRoot();
    }

    SimpleExoPlayer player;

    private void initView() {

        binding.setIsGrid(false);
        binding.chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Log.d("checkedId", checkedId + "");
                currentCategory = checkedId;
                if (filteredVideo.size() > 0) {
                    filterVideo(checkedId);
                }
            }
        });

        binding.videosRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        videosSelectionAdapter = new VideosSelectionAdapter(requireContext(), filteredVideo, this, false);
        binding.videosRecycler.setAdapter(videosSelectionAdapter);

        binding.seeAllVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.getIsGrid()) {
                    binding.videosRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
                    videosSelectionAdapter = new VideosSelectionAdapter(requireContext(), filteredVideo, VideoFragment.this, false);
                    binding.viewAllArrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_down_arrow));
                } else {
                    binding.videosRecycler.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                    videosSelectionAdapter = new VideosSelectionAdapter(requireContext(), filteredVideo, VideoFragment.this, true);
                    binding.viewAllArrow.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_up_arrow));
                }
                binding.videosRecycler.setAdapter(videosSelectionAdapter);
                binding.setIsGrid(!binding.getIsGrid());
            }
        });
        binding.downloadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(requireActivity(), DownloaderActivity.class), 1);
            }
        });
        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(context, "Download Completed!", Toast.LENGTH_SHORT).show();

            }
        };
        requireActivity().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        getVideoCategory();
        //getAllVideo();


    }

    private VideoResponse currentDownloading;

    private void downloadVideo(VideoResponse videoResponse) {
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(requireContext());
        databaseAccess.open();
        ArrayList<LocalVideo> localVideos = databaseAccess.getAllVideo();
        for (LocalVideo localVideo : localVideos) {
            if (videoResponse.getId() == localVideo.getId()) {
                Toast.makeText(requireContext(), "Already Downloaded!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        try {
            currentDownloading = videoResponse;
            DownloadManager.Query query = new DownloadManager.Query();
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoResponse.getAcf().getDownload_video_link()));
            request.setTitle(videoResponse.getAcf().getVideo_title());
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, videoResponse.getAcf().getVideo_title() + ".mp4");
            DownloadManager manager = (DownloadManager) requireActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            query.setFilterByStatus(DownloadManager.STATUS_FAILED | DownloadManager.STATUS_PAUSED | DownloadManager.STATUS_SUCCESSFUL |
                    DownloadManager.STATUS_RUNNING | DownloadManager.STATUS_PENDING);
            Cursor c = manager.query(query);
            databaseAccess.addLocalVideo(currentDownloading, Environment.DIRECTORY_DOWNLOADS + "/" + videoResponse.getAcf().getVideo_title() + ".mp4");
            databaseAccess.close();
            Toast.makeText(requireContext(), "Download Started!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    final String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("DetailsActivity", "Permission is granted");
                return true;
            } else {

                Log.v("DetailsActivity", "Permission is revoked");
                ActivityCompat.requestPermissions(requireActivity(), permissions, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("DetailsActivity", "Permission is granted");
            return true;
        }
    }
    private void filterVideo(int categoryId) {
        binding.seeAllVideo.setVisibility(View.VISIBLE);
        binding.downloadVideo.setVisibility(View.VISIBLE);
        filteredVideo.clear();
        for (VideoResponse i : allVideo) {
            int id = i.getAcf().getCategory().get(0).getId();
            if (categoryId == id) {
                filteredVideo.add(i);
            }
        }
        videosSelectionAdapter.notifyDataSetChanged();
        binding.videoScroll.getLayoutParams().height = binding.root.getLayoutParams().height;
        onClickVideo(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (player != null) {
            player.release();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100 & data != null) {
            if (player != null) {
                player.release();
                binding.playerView.setPlayer(null);
            }
            binding.textView17.setText(data.getStringExtra("name"));
            binding.textView18.setText(data.getStringExtra("duration"));
            player = new SimpleExoPlayer.Builder(requireContext()).build();
            binding.playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(Environment.getExternalStorageDirectory() + "/" + data.getStringExtra("path"));
            player.setMediaItem(mediaItem);
            player.prepare();
            videosSelectionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //player.pause();
    }

    private void getAllVideo() {
        binding.setLoading(true);
        ApiInterface apiInterface = ApiClient.getClient(requireContext()).create(ApiInterface.class);
        Call<ArrayList<VideoResponse>> call = apiInterface.getAllVideo();
        call.enqueue(new Callback<ArrayList<VideoResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<VideoResponse>> call, Response<ArrayList<VideoResponse>> response) {
                binding.setLoading(false);
                if (response.body() != null) {
                    allVideo.clear();
                    filteredVideo.clear();
                    allVideo.addAll(response.body());
                    if (currentCategory != -1) {
                        filterVideo(currentCategory);
                    } else {
                        filterVideo(videoCategories.get(0).getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VideoResponse>> call, Throwable t) {
                binding.setLoading(false);
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getVideoCategory() {
        binding.setLoading(true);
        ApiInterface apiInterface = ApiClient.getClient(requireContext()).create(ApiInterface.class);
        Call<ArrayList<VideoCategoryResponse>> call = apiInterface.getAllVideoCategory();
        call.enqueue(new Callback<ArrayList<VideoCategoryResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<VideoCategoryResponse>> call, Response<ArrayList<VideoCategoryResponse>> response) {
                binding.setLoading(false);
                if (response.body() != null) {
                    videoCategories.clear();
                    videoCategories = response.body();
                    getAllVideo();
                    for (int i = 0; i < videoCategories.size(); i++) {
                        Chip sampleChip = (Chip) requireActivity().getLayoutInflater().inflate(R.layout.layout_chip_choice, null, false);
                        sampleChip.setText(videoCategories.get(i).getCategory_title());
                        sampleChip.setId(videoCategories.get(i).getId());
                        binding.chipGroup.addView(sampleChip);
                        if (i == 0) {
                            sampleChip.setChecked(true);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VideoCategoryResponse>> call, Throwable t) {
                binding.setLoading(false);
                Toast.makeText(requireContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClickVideo(int position) {
        currentVideo = position;
        binding.setCurrentVideo(filteredVideo.get(position));
        for (int i = 0; i < filteredVideo.size(); i++) {
            filteredVideo.get(i).setSelected(false);
        }
        filteredVideo.get(position).setSelected(true);
        if (player != null) {
            player.release();
            binding.playerView.setPlayer(null);
        }
        player = new SimpleExoPlayer.Builder(requireContext()).build();
        binding.playerView.setPlayer(player);
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(filteredVideo.get(position).getAcf().getVideo_link())
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build();
        player.setMediaItem(mediaItem);
        player.prepare();
        videosSelectionAdapter.notifyDataSetChanged();


    }

    @Override
    public void onDownloadVideo(int position) {
        if (isStoragePermissionGranted()) {
            downloadVideo(filteredVideo.get(position));
        } else {
            Toast.makeText(requireContext(), "Please Grant Permission", Toast.LENGTH_SHORT).show();
        }
    }
}