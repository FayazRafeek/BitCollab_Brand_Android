package com.fyzanz.bitcollab.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.FavInfluencer;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.View.Adapter.BrandListAdapter;
import com.fyzanz.bitcollab.View.Adapter.InfluencerListAdapter;
import com.fyzanz.bitcollab.View.Adapter.PopBrandAdapter;
import com.fyzanz.bitcollab.ViewModel.MainViewModel;
import com.fyzanz.bitcollab.databinding.ActivityFavoriteBinding;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity implements InfluencerListAdapter.OnInfluencerClick, PopBrandAdapter.BrandListAction {

    ActivityFavoriteBinding binding;
    MainViewModel mainViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.favBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getInfFavList();
        getBrandFavList();
    }

    InfluencerListAdapter influencerListAdapter;
    void updateFavRecycler(List<Influencer> list){

        if(influencerListAdapter == null){
            influencerListAdapter = new InfluencerListAdapter(this, "FAVORITE");
            binding.favRecycler.setAdapter(influencerListAdapter);
            binding.favRecycler.setLayoutManager(new LinearLayoutManager(this));
        }
        influencerListAdapter.updateAdapter(list);

    }

    BrandListAdapter brandListAdapter;
    void updateBrandFavRecycler(List<Brand> brands){

        if(brandListAdapter == null){
            brandListAdapter = new BrandListAdapter(this,this);
            binding.favBrandRecycler.setAdapter(brandListAdapter);
            binding.favBrandRecycler.setLayoutManager(new LinearLayoutManager(this));
        }

        brandListAdapter.updateList(brands);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInfFavList();
    }

    void getInfFavList(){

        List<FavInfluencer> favInfluencers = mainViewModel.getAllFavInfluencer();
        List<Influencer> infList = new ArrayList<>();

        if(favInfluencers != null)
        for (FavInfluencer fav : favInfluencers){
            Influencer inf = new Influencer();
            inf.setDataFromFav(fav);
            infList.add(inf);
        }

        updateFavRecycler(infList);
    }

    void getBrandFavList(){

        List<Brand> list = mainViewModel.getAllFavBrands();
        if(list != null)
            updateBrandFavRecycler(list);
    }

    @Override
    public void onClickInf(Influencer influencer) {
        Intent intent = new Intent(this, InfluencerActivity.class);
        AppSingleton.getInstance().setSelectedInfluencer(influencer);
        startActivity(intent);
    }

    @Override
    public void onBrandClick(Brand brand) {
        Intent intent = new Intent(this, BrandActivity.class);
        AppSingleton.getInstance().setSelectedBrand(brand);
        startActivity(intent);
    }
}
