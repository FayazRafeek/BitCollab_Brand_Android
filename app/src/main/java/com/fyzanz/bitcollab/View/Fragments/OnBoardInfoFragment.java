package com.fyzanz.bitcollab.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.databinding.FragmentOnboardInfoBinding;

public class OnBoardInfoFragment extends Fragment {

    String userType;
    int position = 0;

    public OnBoardInfoFragment(int postion) {
        this.position = postion;
        userType = AppSingleton.getInstance().getUSER_TYPE();
    }

    FragmentOnboardInfoBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardInfoBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(position == 0)
            binding.obParent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ob_color_1));
        if(position == 1)
            binding.obParent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ob_color_2));
        if(position == 2)
            binding.obParent.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.ob_color_3));

        binding.typeInfo.setText(userType);
    }
}
