package com.fyzanz.bitcollab.View.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fyzanz.bitcollab.Model.Data.Campaign;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.View.Adapter.CampaignListAdapter;
import com.fyzanz.bitcollab.ViewModel.CampaignViewModel;
import com.fyzanz.bitcollab.databinding.ActivityCampaignListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CampaignListActivity extends AppCompatActivity implements CampaignListAdapter.CampaignClick {

    ActivityCampaignListBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCampaignListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fetchActiveCampaigns();

        binding.campSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchActiveCampaigns();
            }
        });
    }


    void fetchActiveCampaigns(){

        binding.campSwipe.setRefreshing(true);
        String brandId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance()
                .collection("Campaigns")
                .whereEqualTo("brandId",brandId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        binding.campSwipe.setRefreshing(false);
                        if(task.isSuccessful()){

                            List<Campaign> list = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult()){
                                Campaign campaign = doc.toObject(Campaign.class);
                                list.add(campaign);
                            }

                            updateActiveRecycler(list);
                        }
                    }
                });
    }

    CampaignListAdapter activeAdpater;
    void updateActiveRecycler(List<Campaign> list){

        if(activeAdpater == null){
            activeAdpater = new CampaignListAdapter(this,"",this);
            binding.activeCampRecycler.setAdapter(activeAdpater);
            binding.activeCampRecycler.setLayoutManager(new LinearLayoutManager(this));
        }

        activeAdpater.updateList(list);
    }

    @Override
    public void onCampaignClick(Campaign campaign) {
        AppSingleton.getInstance().setSelectedCampaign(campaign);
        startActivity(new Intent(this,CampaignDetailActivity.class));
    }
}
