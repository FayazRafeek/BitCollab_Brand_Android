package com.fyzanz.bitcollab.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.View.Adapter.InfluencerListAdapter;
import com.fyzanz.bitcollab.databinding.ActivityFavoriteBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements InfluencerListAdapter.OnInfluencerClick {

    ActivityFavoriteBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.favBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setUpFavRecycler();
    }

    InfluencerListAdapter influencerListAdapter;
    void setUpFavRecycler(){

        if(influencerListAdapter == null){
            influencerListAdapter = new InfluencerListAdapter(this, "FAVORITE");
            binding.favRecycler.setAdapter(influencerListAdapter);
            binding.favRecycler.setLayoutManager(new LinearLayoutManager(this));
        }

        List<Influencer> demoList = new ArrayList<>();
        demoList.add(new Influencer("1","Cristiano","Ronaldo"));
        demoList.add(new Influencer("2","Bell","Fort"));
        demoList.add(new Influencer("3","Romelu","Lukaku"));
        demoList.add(new Influencer("4","Cris","Evans"));
        influencerListAdapter.updateAdapter(demoList);
    }

    @Override
    public void onClickInf(Influencer influencer) {
        Intent intent = new Intent(this, InfluencerActivity.class);
        startActivity(intent);
    }
}
