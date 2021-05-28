package com.example.androidproject.ui.Leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidproject.FirebaseModule;
import com.example.androidproject.R;
import com.example.androidproject.Score;
import com.example.androidproject.ScoreAdapter;
import com.example.androidproject.databinding.FragmentLeaderboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardFragment extends Fragment {

    private FragmentLeaderboardBinding binding;

    private ListView lv;
    private ScoreAdapter adapter;
    private FirebaseModule db;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        setup();

        return root;
    }

    private void setup(){
        lv = root.findViewById(R.id.listView);
        db = FirebaseModule.getInstance();
        db.getDatabase().getReference("scores").orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                ArrayList<Score> list = new ArrayList<Score>();
                for (DataSnapshot ds : snapshot.getChildren())
                    list.add(ds.getValue(Score.class));
                Collections.reverse(list);  // descending order
                adapter = new ScoreAdapter(root.getContext(),list);
                lv.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}