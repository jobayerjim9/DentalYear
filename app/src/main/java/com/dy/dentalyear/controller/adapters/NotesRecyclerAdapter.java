package com.dy.dentalyear.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.dy.dentalyear.R;
import com.dy.dentalyear.databinding.ItemNotesBinding;
import com.dy.dentalyear.model.local.NotesModel;

import java.util.ArrayList;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NotesModel> notesModels;

    public NotesRecyclerAdapter(Context context, ArrayList<NotesModel> notesModels) {
        this.context = context;
        this.notesModels = notesModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notes,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NotesModel item=notesModels.get(position);
        holder.binding.setData(item);
    }
    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    @Override
    public int getItemCount() {
        return notesModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemNotesBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }
    }
}
