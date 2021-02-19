package com.dy.dentalyear.controller.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dy.dentalyear.R;
import com.dy.dentalyear.controller.helpers.OnSwipeTouchListener;
import com.dy.dentalyear.controller.helpers.Utils;
import com.dy.dentalyear.controller.localdb.DatabaseAccess;
import com.dy.dentalyear.databinding.ItemNotesBinding;
import com.dy.dentalyear.model.constant.AppConstants;
import com.dy.dentalyear.model.local.NotesModel;
import com.dy.dentalyear.ui.activity.WritingActivity;
import com.dy.dentalyear.ui.fragment.GoalsListFragment;
import com.dy.dentalyear.ui.fragment.NotesFragment;
import com.dy.dentalyear.ui.fragment.NotesViewerFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.dy.dentalyear.model.constant.AppConstants.NOTE_EDITABLE;
import static com.dy.dentalyear.model.constant.AppConstants.NOTE_PARCELABLE;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NotesModel> notesModels;
    private FragmentManager fragmentManager;
    float x1, x2;

    public NotesRecyclerAdapter(Context context, ArrayList<NotesModel> notesModels, FragmentManager fragmentManager) {
        this.context = context;
        this.notesModels = notesModels;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final NotesModel item = notesModels.get(position);
        holder.binding.setData(item);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = format.parse(item.getDate());
            String placeHolder = Utils.getMonthForInt(date.getMonth()).substring(0, 3) + " " + date.getDate() + "," + (date.getYear() + 1900);
            holder.binding.dateItem.setText(placeHolder);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.binding.editNoteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WritingActivity.class);
                intent.putExtra(NOTE_PARCELABLE, item);
                intent.putExtra(NOTE_EDITABLE, true);
                context.startActivity(intent);
            }
        });
        holder.binding.base.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new NotesViewerFragment(item, fragmentManager), "noteViewer")
                        .setReorderingAllowed(true)
                        .addToBackStack("noteViewer") // name can be null
                        .commit();
                NotesFragment notesFragment = (NotesFragment) fragmentManager.findFragmentByTag(AppConstants.HOME_FRAGMENT_TAGS.get(1));
                if (notesFragment != null) {
                    notesFragment.hideTopBar();
                }
            }
        });


        holder.binding.base.setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeLeft() {
                Log.d("onSwipe", "left");
                // super.onSwipeLeft();
                holder.binding.deleteButton.setVisibility(View.VISIBLE);
                holder.binding.base.setAlpha(0.5f);
            }

            @Override
            public void onSwipeRight() {
                Log.d("onSwipe", "right");
                //super.onSwipeRight();

                holder.binding.deleteButton.setVisibility(View.GONE);
                holder.binding.base.setAlpha(1f);
            }
        });

        holder.binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(context)
                        .setTitle("Are you sure to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(context);
                                databaseAccess.open();
                                if (databaseAccess.deleteNote(item.getId())) {
                                    holder.binding.deleteButton.setVisibility(View.GONE);
                                    notesModels.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "The Note Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Unable to delete the note!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        holder.binding.executePendingBindings();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return notesModels.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ItemNotesBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
