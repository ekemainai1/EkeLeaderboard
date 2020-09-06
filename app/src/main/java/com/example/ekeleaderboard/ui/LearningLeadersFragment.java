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
import com.example.ekeleaderboard.adapters.LearnersListAdapter;
import com.example.ekeleaderboard.adapters.OnItemTouchListener;
import com.example.ekeleaderboard.adapters.RecyclerViewOnItemTouchListener;

public class LearningLeadersFragment extends Fragment {

    View root;
    public static final String ARG_LEARNERS = "Learning Leaders";
    private LearningLeadersViewModel learningLeadersViewModel;
    private RecyclerView recyclerViewLearners;
    LearnersListAdapter learnersListAdapter;

    public static LearningLeadersFragment newInstance() {
        return new LearningLeadersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.learning_leaders_fragment, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        learningLeadersViewModel = new ViewModelProvider(requireActivity()).get(LearningLeadersViewModel.class);

        recyclerViewLearners = root.findViewById(R.id.recyclerViewLearn);
        learnersListAdapter = new LearnersListAdapter(learningLeadersViewModel,this,requireActivity());

        recyclerViewLearners.setHasFixedSize(true);
        RecyclerView.LayoutManager audioLayoutManager = new LinearLayoutManager(requireActivity());
        recyclerViewLearners.setLayoutManager(audioLayoutManager);

        int[] ATTRS = new int[]{android.R.attr.listDivider};
        TypedArray a = requireActivity().obtainStyledAttributes(ATTRS);
        Drawable divider = a.getDrawable(0);
        int inset = getResources().getDimensionPixelSize(R.dimen.rec_margin);
        InsetDrawable insetDivider = new InsetDrawable(divider, inset, 0, inset, 0);
        a.recycle();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireActivity(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(insetDivider);
        recyclerViewLearners.addItemDecoration(itemDecoration);

        recyclerViewLearners.setAdapter(learnersListAdapter);

        observableViewModelLocalLearners();

        recyclerViewLearners.addOnItemTouchListener(new RecyclerViewOnItemTouchListener(this
                .requireActivity().getApplicationContext(),
                recyclerViewLearners, new OnItemTouchListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(requireActivity(), learnersListAdapter.getName(position),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


    }


    private void observableViewModelLocalLearners() {
        learningLeadersViewModel.getLocalLearnRepos().observe(getViewLifecycleOwner(), repos -> {
            if (repos != null) recyclerViewLearners.setVisibility(View.VISIBLE);
            Toast.makeText(requireActivity(), "Learners Loaded", Toast.LENGTH_LONG).show();
        });

        learningLeadersViewModel.getLocalLearnError().observe(getViewLifecycleOwner(), isError -> {
            if (isError != null) {
                if (isError) {
                    Toast.makeText(requireActivity(), "Error Learners Loading ...", Toast.LENGTH_LONG).show();
                    recyclerViewLearners.setVisibility(View.INVISIBLE);
                }
            } else {

                recyclerViewLearners.setVisibility(View.VISIBLE);

            }
        });

        learningLeadersViewModel.getLocalLearnLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                Toast.makeText(requireActivity(), "Learners Loading ...", Toast.LENGTH_LONG).show();
                if (isLoading) {
                    recyclerViewLearners.setVisibility(View.INVISIBLE);

                }
            }
        });
    }



}