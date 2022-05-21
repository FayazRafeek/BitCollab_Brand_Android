package com.fyzanz.bitcollab.View.Fragments.CampaignCreate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.fyzanz.bitcollab.ViewModel.CampaignViewModel;
import com.fyzanz.bitcollab.databinding.FragmentCampStep1Binding;

public class CampCreateStep1Fragment extends Fragment {

    FragmentCampStep1Binding binding;
    CampaignViewModel campaignViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCampStep1Binding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        campaignViewModel = new ViewModelProvider(getActivity()).get(CampaignViewModel.class);

        if(campaignViewModel.STEP_1_COMPLETE)
            updateUi();

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gatherData();
            }
        });
    }

    void gatherData(){
        String campName = binding.campNameInp.getText().toString();
        String camDesc = binding.campDescInp.getText().toString();
        String promotitle = binding.campTitleInp.getText().toString();
        campaignViewModel.setNewCampStep1Data(campName,promotitle,camDesc);
        campaignViewModel.nextPage();
    }

    void updateUi(){
        binding.campNameInp.setText(campaignViewModel.newCampaign.getCampaignName());
        binding.campDescInp.setText(campaignViewModel.newCampaign.getCampaignDesc());
        binding.campTitleInp.setText(campaignViewModel.newCampaign.getPromoteTitle());
    }
}
