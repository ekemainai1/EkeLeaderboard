package com.example.ekeleaderboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.ekeleaderboard.entity.LearningLeadersEntity;
import com.example.ekeleaderboard.R;
import com.example.ekeleaderboard.ui.LearningLeadersViewModel;
import java.util.ArrayList;
import java.util.List;

public class LearnersListAdapter extends RecyclerView.Adapter<LearnersListAdapter.LearnersListViewHolder>{


    Context context;
    List<LearningLeadersEntity> leadersEntities= new ArrayList<>();
    private int mTrackPlaying = -1;


    public LearnersListAdapter(LearningLeadersViewModel viewModel, LifecycleOwner lifecycleOwner,
                               Context context) {
        this.context = context;
        viewModel.getLocalLearnRepos().observe(lifecycleOwner, repos -> {
            if (leadersEntities != null) {
                leadersEntities.clear();
            }

            if (repos != null) {
                assert leadersEntities != null;
                leadersEntities.addAll(repos);
                notifyDataSetChanged();
            }
        });
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public LearnersListAdapter.LearnersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lerners_item_list,
                parent, false);
        return new LearnersListAdapter.LearnersListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LearnersListViewHolder holder, int position) {
        LearningLeadersEntity leadersEntity = leadersEntities.get(position);
        if(position == mTrackPlaying) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
        } else {
            // Here, you must restore the color because the view is reused.. so, you may receive a reused view with wrong colors
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }

        Glide
                .with(context)
                .load(leadersEntity.getBadgeUrl())
                .fitCenter()
                .placeholder(R.drawable.toplearner)
                .into(holder.learnerThumbnail);

        String hours = String.valueOf(leadersEntity.getHours()) + " Learning hours,";
        holder.learnerName.setText(leadersEntity.getName());
        holder.learnerHours.setText(hours);
        holder.learnerCountry.setText(leadersEntity.getCountry());

    }

    @Override
    public long getItemId(int position) {
        return leadersEntities.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return leadersEntities.size();
    }

    static class LearnersListViewHolder extends RecyclerView.ViewHolder {

        private ImageView learnerThumbnail;
        private TextView learnerName;
        private TextView learnerHours;
        private  TextView learnerCountry;


        LearnersListViewHolder(@NonNull View itemView) {
            super(itemView);

            learnerThumbnail = itemView.findViewById(R.id.profile);
            learnerName = itemView.findViewById(R.id.learnersName);
            learnerHours = itemView.findViewById(R.id.learnHours);
            learnerCountry = itemView.findViewById(R.id.learnersCountry);
            }
        }


    public String getName(int pos){
        return leadersEntities.get(pos).getName();
    }

    public String getId(int pos){
        return String.valueOf(leadersEntities.get(pos).getId());
    }


    public void setTrackPlaying(int position) {
        mTrackPlaying = position;
    }


}
