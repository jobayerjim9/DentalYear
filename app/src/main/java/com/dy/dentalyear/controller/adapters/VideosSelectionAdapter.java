package com.dy.dentalyear.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.helpers.VideoItemClickListener;
import com.dy.dentalyear.databinding.ItemVideoSelectorBinding;
import com.dy.dentalyear.databinding.ItemVideoSelectorGridBinding;
import com.dy.dentalyear.model.api.VideoResponse;

import java.util.ArrayList;

public class VideosSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<VideoResponse> videoResponses;
    private VideoItemClickListener videoItemClickListener;
    private boolean isGrid;

    public VideosSelectionAdapter(Context context, ArrayList<VideoResponse> videoResponses, VideoItemClickListener videoItemClickListener, boolean isGrid) {
        this.context = context;
        this.videoResponses = videoResponses;
        this.videoItemClickListener = videoItemClickListener;
        this.isGrid = isGrid;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isGrid) {
            return new ViewHolderGrid(LayoutInflater.from(context).inflate(R.layout.item_video_selector_grid, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_video_selector, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final VideoResponse item = videoResponses.get(position);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.binding.setData(item);
            viewHolder.binding.itemVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoItemClickListener.onClickVideo(position);
                }
            });
            viewHolder.binding.executePendingBindings();
        } else if (holder instanceof ViewHolderGrid) {
            ViewHolderGrid viewHolderGrid = (ViewHolderGrid) holder;
            viewHolderGrid.binding.setData(item);
            viewHolderGrid.binding.itemVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    videoItemClickListener.onClickVideo(position);
                }
            });
            viewHolderGrid.binding.executePendingBindings();
        }

    }

    @Override
    public int getItemCount() {
        return videoResponses.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemVideoSelectorBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }

    class ViewHolderGrid extends RecyclerView.ViewHolder {
        ItemVideoSelectorGridBinding binding;

        public ViewHolderGrid(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }

}
