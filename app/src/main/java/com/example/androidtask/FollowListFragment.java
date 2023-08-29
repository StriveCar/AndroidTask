package com.example.androidtask;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FollowListFragment extends Fragment {
    private View FollowList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(FollowList == null){
            FollowList = inflater.inflate(R.layout.fragment_follow_list,container,false);
        }
        return FollowList;
    }
}