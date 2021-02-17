package com.dy.dentalyear.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dy.dentalyear.R;
import com.dy.dentalyear.databinding.ItemSponsorBinding;
import com.dy.dentalyear.model.api.SponsorsResponse;
import com.dy.dentalyear.model.constant.AppConstants;
import com.dy.dentalyear.ui.fragment.ExhibitsFragment;
import com.dy.dentalyear.ui.fragment.ExhibitsViewFragment;
import com.dy.dentalyear.ui.fragment.NotesViewerFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SponsorsRecyclerAdapter extends RecyclerView.Adapter<SponsorsRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<SponsorsResponse> sponsorsResponses;
    private FragmentManager fragmentManager;

    public SponsorsRecyclerAdapter(Context context, ArrayList<SponsorsResponse> sponsorsResponses, FragmentManager fragmentManager) {
        this.context = context;
        this.sponsorsResponses = sponsorsResponses;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_sponsor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SponsorsResponse item = sponsorsResponses.get(position);
        Picasso.get().load(item.getAcf().getSponsor_logo()).placeholder(R.drawable.ic_logo_small).resize(100, 100).into(holder.binding.sponsorImage);
        holder.binding.itemSponsors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.sponsorContainer, new ExhibitsViewFragment(fragmentManager, item), "exhibitView")
                        .setReorderingAllowed(true)
                        .addToBackStack("exhibitView") // name can be null
                        .commit();
                ExhibitsFragment exhibitsFragment = (ExhibitsFragment) fragmentManager.findFragmentByTag(AppConstants.HOME_FRAGMENT_TAGS.get(2));
                if (exhibitsFragment != null) {
                    exhibitsFragment.hideMain();
                }
            }
        });

        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return sponsorsResponses.size();
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
        ItemSponsorBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
