package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ScoreAdapter extends ArrayAdapter<Score> {

    private Context context;
    private ArrayList<Score> list;

    public int getSize(){
        return list.size();
    }

    public ScoreAdapter(@NonNull Context context, ArrayList<Score> list){
        super(context,R.layout.layout_score,list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((AppCompatActivity) context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.layout_score,parent,false);
        TextView txt = view.findViewById(R.id.scoreItemText);
        txt.setText(list.get(position).toString());
        return view;
    }
}