package com.example.androidproject.ui.GameOver;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidproject.FirebaseModule;
import com.example.androidproject.R;
import com.example.androidproject.databinding.FragmentFinalscoreBinding;

public class FinalScoreFragment extends Fragment {

    private FragmentFinalscoreBinding binding;
    private TextView score,max;
    private ImageView camImg;
    private Button btn;
    private FirebaseModule db;
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFinalscoreBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        setup();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void setup(){
        db = FirebaseModule.getInstance();
        score = view.findViewById(R.id.finalScore);
        max = view.findViewById(R.id.finalMax);
        camImg = view.findViewById(R.id.camPicture);
        btn = view.findViewById(R.id.picBtn);
        score.setText(db.getScore().toString());
        max.setText(db.getMax().toString());
    }
}