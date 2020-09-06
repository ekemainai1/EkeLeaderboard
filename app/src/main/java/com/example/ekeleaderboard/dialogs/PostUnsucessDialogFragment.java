package com.example.ekeleaderboard.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ekeleaderboard.R;
import com.example.ekeleaderboard.activities.LeadersPostActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PostUnsucessDialogFragment extends DialogFragment {

    View root;

    public PostUnsucessDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static PostUnsucessDialogFragment newInstance() {
        return new PostUnsucessDialogFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.post_unsuccess_dialog_fragment, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    }

    @Override
    public void onStart() {
        super.onStart();
            Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow())
                    .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


}
