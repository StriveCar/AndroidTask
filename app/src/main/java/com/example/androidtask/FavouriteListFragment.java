package com.example.androidtask;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class FavouriteListFragment extends Fragment {
    private View FavouriteList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(FavouriteList == null){
            FavouriteList = inflater.inflate(R.layout.fragment_favourite_list,container,false);
        }

        return FavouriteList;
    }
}