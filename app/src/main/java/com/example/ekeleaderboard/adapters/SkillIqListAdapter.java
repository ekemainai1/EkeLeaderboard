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
import com.example.ekeleaderboard.R;
import com.example.ekeleaderboard.entity.SkillIQLeadersEntity;
import com.example.ekeleaderboard.ui.SkillIQLeadersViewModel;
import java.util.ArrayList;
import java.util.List;

public class SkillIqListAdapter extends RecyclerView.Adapter<SkillIqListAdapter.SkillIqListViewHolder>{


    Context context;
    List<SkillIQLeadersEntity> iqLeadersEntities= new ArrayList<>();
    private int mTrackPlaying = -1;


    public SkillIqListAdapter(SkillIQLeadersViewModel viewModel, LifecycleOwner lifecycleOwner,
                              Context context) {
        this.context = context;
        viewModel.getLocalSkillRepos().observe(lifecycleOwner, repos -> {
            if (iqLeadersEntities != null) {
                iqLeadersEntities.clear();
            }

            if (repos != null) {
                assert iqLeadersEntities != null;
                iqLeadersEntities.addAll(repos);
                notifyDataSetChanged();
            }
        });
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public SkillIqListAdapter.SkillIqListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skill_item_list,
                parent, false);
        return new SkillIqListAdapter.SkillIqListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillIqListViewHolder holder, int position) {
        SkillIQLeadersEntity iqLeadersEntity = iqLeadersEntities.get(position);
        if(position == mTrackPlaying) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));
        } else {
            // Here, you must restore the color because the view is reused.. so, you may receive a reused view with wrong colors
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));
        }

        Glide
                .with(context)
                .load(iqLeadersEntity.getBadgeUrl())
                .fitCenter()
                .placeholder(R.drawable.skilltrimmed)
                .into(holder.skillThumbnail);

        String scores = String.valueOf(iqLeadersEntity.getScore())+" skill IQ Score,";
        holder.skillName.setText(iqLeadersEntity.getName());
        holder.skillScores.setText(scores);
        holder.skillCountry.setText(iqLeadersEntity.getCountry());

    }

    @Override
    public long getItemId(int position) {
        return iqLeadersEntities.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return iqLeadersEntities.size();
    }

    static class SkillIqListViewHolder extends RecyclerView.ViewHolder {

        private ImageView skillThumbnail;
        private TextView skillName;
        private TextView skillScores;
        private  TextView skillCountry;

        SkillIqListViewHolder(@NonNull View itemView) {
            super(itemView);

            skillThumbnail = itemView.findViewById(R.id.skillprofile);
            skillName = itemView.findViewById(R.id.skillName);
            skillScores = itemView.findViewById(R.id.skillScores);
            skillCountry = itemView.findViewById(R.id.skillCountry);
            }
        }


    public String getName(int pos){
        return iqLeadersEntities.get(pos).getName();
    }

    public String getId(int pos){
        return String.valueOf(iqLeadersEntities.get(pos).getId());
    }

    public void setTrackPlaying(int position) {
        mTrackPlaying = position;
    }


}
