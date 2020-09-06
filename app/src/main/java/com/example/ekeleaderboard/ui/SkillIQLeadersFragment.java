package com.example.ekeleaderboard.ui;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.ekeleaderboard.R;
import com.example.ekeleaderboard.adapters.OnItemTouchListener;
import com.example.ekeleaderboard.adapters.RecyclerViewOnItemTouchListener;
import com.example.ekeleaderboard.adapters.SkillIqListAdapter;

public class SkillIQLeadersFragment extends Fragment {

    View root;
    public static final String ARG_SKILL = "Skill IQ Leaders";
    private SkillIQLeadersViewModel skillIQLeadersViewModel;
    private RecyclerView recyclerViewSkills;
    SkillIqListAdapter skillIqListAdapter;

    public static SkillIQLeadersFragment newInstance() {
        return new SkillIQLeadersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.skill_i_q_leaders_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        skillIQLeadersViewModel = new ViewModelProvider(requireActivity()).get(SkillIQLeadersViewModel.class);

        recyclerViewSkills = root.findViewById(R.id.recyclerViewSkill);
        skillIqListAdapter = new SkillIqListAdapter(skillIQLeadersViewModel,this,requireActivity());

        recyclerViewSkills.setHasFixedSize(true);
        RecyclerView.LayoutManager audioLayoutManager = new LinearLayoutManager(requireActivity());
        recyclerViewSkills.setLayoutManager(audioLayoutManager);

        int[] ATTRS = new int[]{android.R.attr.listDivider};
        TypedArray a = requireActivity().obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen.rec_margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);
        recyclerViewSkills.addItemDecoration(itemDecoration);

        recyclerViewSkills.setAdapter(skillIqListAdapter);

        observableViewModelLocalSkillIq();

        recyclerViewSkills.addOnItemTouchListener(new RecyclerViewOnItemTouchListener(this
                .requireActivity().getApplicationContext(),
                recyclerViewSkills, new OnItemTouchListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(requireActivity(), skillIqListAdapter.getName(position),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void observableViewModelLocalSkillIq() {
        skillIQLeadersViewModel.getLocalSkillRepos().observe(getViewLifecycleOwner(), repos -> {
            if (repos != null) recyclerViewSkills.setVisibility(View.VISIBLE);
            Toast.makeText(requireActivity(), "SkillIq Loaded", Toast.LENGTH_LONG).show();
        });

        skillIQLeadersViewModel.getLocalSkillError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if (isError) {
                    Toast.makeText(requireActivity(), "Error SkillIq Loading ...", Toast.LENGTH_LONG).show();
                    recyclerViewSkills.setVisibility(View.INVISIBLE);
                }
            } else {

                recyclerViewSkills.setVisibility(View.VISIBLE);

            }
        });

        skillIQLeadersViewModel.getLocalSkillLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                Toast.makeText(requireActivity(), "SkillIq Loading ...", Toast.LENGTH_LONG).show();
                if (isLoading) {
                    recyclerViewSkills.setVisibility(View.INVISIBLE);

                }
            }
        });
    }



}