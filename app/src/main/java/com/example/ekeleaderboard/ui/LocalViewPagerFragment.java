package com.example.ekeleaderboard.ui;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.ekeleaderboard.adapters.LocalViewPagerAdapter;
import com.example.ekeleaderboard.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class LocalViewPagerFragment extends Fragment {


    private LocalViewPagerViewModel localViewPagerViewModel;
    ViewPager2 viewPager2Spoken;
    TabLayout tabLayoutSpoken;
    private View root;
    LocalViewPagerAdapter localViewPagerAdapter;


    public LocalViewPagerFragment() {
    }

    public static LocalViewPagerFragment newInstance() {
        return new LocalViewPagerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.local_view_pager_fragment, container, false);
        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);

        localViewPagerViewModel = new ViewModelProvider(this).get(LocalViewPagerViewModel.class);
        viewPager2Spoken = root.findViewById(R.id.pager);
        tabLayoutSpoken = root.findViewById(R.id.tab_layout);


        localViewPagerAdapter = new LocalViewPagerAdapter(this);
        viewPager2Spoken.setAdapter(localViewPagerAdapter);

        new TabLayoutMediator(tabLayoutSpoken,viewPager2Spoken, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                if(position == 0){
                    localViewPagerAdapter.createFragment(position);
                    tab.setText(LearningLeadersFragment.ARG_LEARNERS);
                }else if(position == 1){
                    localViewPagerAdapter.createFragment(position);
                    tab.setText(SkillIQLeadersFragment.ARG_SKILL);
                }
            }
        }).attach();


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }
}