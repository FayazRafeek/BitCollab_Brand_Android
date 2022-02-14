package com.fyzanz.bitcollab.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fyzanz.bitcollab.databinding.FragmentChatListBinding;

public class ChatListFragment extends Fragment {

    FragmentChatListBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         binding = FragmentChatListBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
