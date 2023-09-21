package com.example.androidtask.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidtask.R;

public class CollectListFragment extends Fragment {
    private View FavouriteList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(FavouriteList == null){
            FavouriteList = inflater.inflate(R.layout.fragment_collect_list,container,false);
        }

        return FavouriteList;
    }
}