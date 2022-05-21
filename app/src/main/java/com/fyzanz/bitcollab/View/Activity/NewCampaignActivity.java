package com.fyzanz.bitcollab.View.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.View.Fragments.CampaignCreate.CampCreateStep1Fragment;
import com.fyzanz.bitcollab.View.Fragments.CampaignCreate.CampCreateStep2Fragment;
import com.fyzanz.bitcollab.View.Fragments.CampaignCreate.CampCreateStep3Fragment;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfAbout;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfBasic;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfContact;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfSocial;
import com.fyzanz.bitcollab.ViewModel.CampaignViewModel;
import com.fyzanz.bitcollab.databinding.ActivityCreateCampaignBinding;

public class NewCampaignActivity extends AppCompatActivity {

    ActivityCreateCampaignBinding binding;
    CampaignViewModel campaignViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        campaignViewModel = new ViewModelProvider(this).get(CampaignViewModel.class);

        binding = ActivityCreateCampaignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        campaignViewModel.getFragPosLive().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                showFragment(integer);
            }
        });
    }


    int MAX_PAGE = 3;
    void showFragment(int pos){
        if(pos < MAX_PAGE)
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(binding.newCampFragContain.getId(), getFragment(pos))
                    .commit();
        else finishClickHandle();
    }

    Fragment getFragment(int pos){
        switch (pos){
            case 0 : return new CampCreateStep1Fragment();
            case 1 : return new CampCreateStep2Fragment();
            case 2 : return new CampCreateStep3Fragment();
            default: return null;
        }

    }

    void finishClickHandle(){

        binding.newCampFragContain.setVisibility(View.GONE);
        binding.progressParent.setVisibility(View.VISIBLE);

        campaignViewModel.getBannerLive()
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse1) {
                        switch (basicResponse1.getStatus()){
                            case "LOADING" : break;
                            case "SUCCESS" :
                                campaignViewModel.uploadCampaignData()
                                        .observe(NewCampaignActivity.this, new Observer<BasicResponse>() {
                                            @Override
                                            public void onChanged(BasicResponse basicResponse2) {
                                                switch (basicResponse2.getStatus()){
                                                    case "SUCCESS" :
                                                        Toast.makeText(NewCampaignActivity.this, "Campaign creating successfull", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        break;
                                                    case "ERROR" :
                                                        binding.newCampFragContain.setVisibility(View.VISIBLE);
                                                        binding.progressParent.setVisibility(View.GONE);
                                                        Toast.makeText(NewCampaignActivity.this, basicResponse2.getError().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                break;
                            case "ERROR" :
                                binding.newCampFragContain.setVisibility(View.VISIBLE);
                                binding.progressParent.setVisibility(View.GONE);
                                Toast.makeText(NewCampaignActivity.this, basicResponse1.getError().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

}
