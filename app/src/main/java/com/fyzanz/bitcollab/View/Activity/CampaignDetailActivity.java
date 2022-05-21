package com.fyzanz.bitcollab.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.Model.Data.Campaign;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.databinding.ActivityCampaignDetailBinding;

import java.util.ArrayList;

public class CampaignDetailActivity extends AppCompatActivity {


    ActivityCampaignDetailBinding binding;
    Campaign campaign;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCampaignDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        campaign = AppSingleton.getInstance().getSelectedCampaign();
        if(campaign == null) finish();

        updateUi();
    }

    void updateUi(){

        Glide.with(this)
                .load(campaign.getBannerUrl())
                .centerCrop()
                .into(binding.hero);

        Glide.with(this)
                .load(campaign.getBrandLogoUrl())
                .centerCrop()
                .into(binding.brandLogo);


        String campTitle = campaign.getPromoteTitle();
        binding.campTitle.setText(campTitle);

        String brandInfo = "By " + campaign.getBrandName();
        binding.campBrand.setText(brandInfo);

        binding.campDesc.setText(campaign.getCampaignDesc());

        binding.startDate.setText(campaign.getStartDate());
        binding.endDate.setText(campaign.getEndDate());

        if(campaign.getTwitt()) binding.ptTwt.setVisibility(View.VISIBLE);
        if(campaign.getIntsa()) binding.ptIn.setVisibility(View.VISIBLE);
        if(campaign.getYout()) binding.ptYt.setVisibility(View.VISIBLE);

        BrandActivity.BrandCatAdapter catAdapter = new BrandActivity.BrandCatAdapter(this);
        binding.campTopicRecycer.setAdapter(catAdapter);
        binding.campTopicRecycer.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        catAdapter.updateList(campaign.getTopics());


        binding.msgBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
