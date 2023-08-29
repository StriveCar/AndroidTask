package com.example.androidtask;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyShareListFragment extends Fragment {
    private View MyShareList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(MyShareList == null){
            MyShareList = inflater.inflate(R.layout.fragment_my_share_list,container,false);
        }
        return MyShareList;
    }
}