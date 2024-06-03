package com.example.administrationapp.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrationapp.R;
import com.example.administrationapp.databinding.FragmentProfileSetBinding;

public class profile_set_fragment extends Fragment {

    private FragmentProfileSetBinding binding;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentProfileSetBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        return root;
    }
}