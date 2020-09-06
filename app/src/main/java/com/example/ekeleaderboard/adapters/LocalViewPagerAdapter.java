package com.example.ekeleaderboard.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.ekeleaderboard.ui.LearningLeadersFragment;
import com.example.ekeleaderboard.ui.SkillIQLeadersFragment;


public class LocalViewPagerAdapter extends FragmentStateAdapter {


    public LocalViewPagerAdapter(Fragment fragment) {
        super(fragment);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle args = new Bundle();

        Fragment fragment = new Fragment();

        if(position == 0) {
            fragment = LearningLeadersFragment.newInstance();
            args.putInt(LearningLeadersFragment.ARG_LEARNERS, position);
            fragment.setArguments(args);
        }else if(position == 1){
            fragment = SkillIQLeadersFragment.newInstance();
            args.putInt(SkillIQLeadersFragment.ARG_SKILL, position + 1);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
