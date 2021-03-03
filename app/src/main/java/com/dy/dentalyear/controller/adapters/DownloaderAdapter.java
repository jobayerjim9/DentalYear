package com.dy.dentalyear.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.helpers.LocalVideoItemClickListener;
import com.dy.dentalyear.databinding.ItemDownloadedVideoBinding;
import com.dy.dentalyear.model.local.LocalVideo;

import java.util.ArrayList;

public class DownloaderAdapter extends RecyclerView.Adapter<DownloaderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<LocalVideo> localVideos;
    private LocalVideoItemClickListener listener;

    public DownloaderAdapter(Context context, ArrayList<LocalVideo> localVideos, LocalVideoItemClickListener listener) {
        this.context = context;
        this.localVideos = localVideos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DownloaderAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_downloaded_video, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setData(localVideos.get(position));
        holder.binding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickVideo(localVideos.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return localVideos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemDownloadedVideoBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
