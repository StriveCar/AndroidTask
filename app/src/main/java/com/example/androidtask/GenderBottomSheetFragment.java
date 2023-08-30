package com.example.androidtask;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class GenderBottomSheetFragment extends BottomSheetDialogFragment {

    private NumberPicker numberPicker;

    private OnGenderSelectedListener listener;

    public static String select = "男";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gender_bottom_sheet, container, false);

        numberPicker = view.findViewById(R.id.number_picker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(1);
        String[] genderOptions = {"男", "女"};
        numberPicker.setDisplayedValues(genderOptions);

        TextView btnCancel = view.findViewById(R.id.btn_cancel);
        TextView btnConfirm = view.findViewById(R.id.btn_confirm);

        btnCancel.setOnClickListener(v -> dismiss());

        btnConfirm.setOnClickListener(v -> {
            int selectedValue = numberPicker.getValue();
            select = genderOptions[selectedValue];
            if (listener != null) {
                listener.onGenderSelected(select);
            }
            dismiss();
        });

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnGenderSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnGenderSelectedListener");
        }
    }


    public interface OnGenderSelectedListener {
        void onGenderSelected(String gender);
    }


}

