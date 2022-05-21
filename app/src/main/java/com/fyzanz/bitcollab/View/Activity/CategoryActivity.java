package com.fyzanz.bitcollab.View.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Category;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.View.Adapter.BrandListAdapter;
import com.fyzanz.bitcollab.View.Adapter.InfluencerListAdapter;
import com.fyzanz.bitcollab.View.Adapter.PopBrandAdapter;
import com.fyzanz.bitcollab.View.Adapter.PopularInfAdapter;
import com.fyzanz.bitcollab.ViewModel.MainViewModel;
import com.fyzanz.bitcollab.databinding.ActivityCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements InfluencerListAdapter.OnInfluencerClick, PopBrandAdapter.BrandListAction {

    ActivityCategoryBinding binding;
    Category category;

    String userType;

    MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setSharedTransitions();
        getCategory();
        setStatusBarColor();


        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setHeroUi();

        userType = AppSingleton.getInstance().getUSER_TYPE();

        if(userType.equals("INFLUENCER")){
            startBrandFetching();
        } else startInfFetching();

        binding.catSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(userType.equals("INFLUENCER")){
                    startBrandFetching();
                } else startInfFetching();
            }
        });

    }


    //Inf Data & Ui

    void startInfFetching(){

        showLoading();
        mainViewModel.fetchPopCatInf(category.getName())
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" : showLoading(); break;
                            case "SUCCESS" : updateTrendInfRecycler((List<Influencer>) basicResponse.getData());
                            fetchNearbyInfluencer(); break;
                            case "ERROR" : stopLoading(); showError(basicResponse.getError()); fetchNearbyInfluencer(); break;
                        }
                    }
                });

    }

    void fetchNearbyInfluencer(){

        showLoading();
        mainViewModel.fetchNearbyCatInf(category.getName())
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" : showLoading(); break;
                            case "SUCCESS" : updateNearbyInfRecycler((List<Influencer>) basicResponse.getData());
                                fetInfCount();
                                stopLoading(); break;
                            case "ERROR" : stopLoading(); fetInfCount(); showError(basicResponse.getError()); break;
                        }
                    }
                });
    }

    int infCount = 0, brandCount = 0;
    void fetInfCount(){
        mainViewModel.fetchCatInfCount(category.getName())
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "SUCCESS" :
                                infCount = (int)basicResponse.getData();
                                binding.catSubText.setText(infCount + " Influencers");break;
                            case "ERROR" : stopLoading(); showError(basicResponse.getError()); break;
                        }
                    }
                });
    }


    PopularInfAdapter popularInfAdapter;
    void updateTrendInfRecycler(List<Influencer> influencers){
        if(popularInfAdapter == null){
            popularInfAdapter = new PopularInfAdapter("CATEGORY",this);
            binding.catPopRecycler.setAdapter(popularInfAdapter);
            binding.catPopRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        }

        popularInfAdapter.updateAdapter(influencers);

        if(influencers == null || influencers.isEmpty())
            binding.catInfPopEmptyParent.setVisibility(View.VISIBLE);
        else
            binding.catInfPopEmptyParent.setVisibility(View.GONE);
    }


    InfluencerListAdapter influencerListAdapter;
    void updateNearbyInfRecycler(List<Influencer> influencers){

        if(influencerListAdapter == null){
            influencerListAdapter = new InfluencerListAdapter(this,"CATEGORY");
            binding.catNearInfRecycler.setAdapter(influencerListAdapter);
            binding.catNearInfRecycler.setLayoutManager(new LinearLayoutManager(this));
        }
        influencerListAdapter.updateAdapter(influencers);


        if(influencers == null || influencers.isEmpty())
            binding.catInfNearbyEmptyParent.setVisibility(View.VISIBLE);
        else
            binding.catInfNearbyEmptyParent.setVisibility(View.GONE);
    }

    //

    //Brand Data & UI
    void startBrandFetching(){
        mainViewModel.fetchPopCatBrn(category.getName())
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" : showLoading(); break;
                            case "SUCCESS" : updateTrendBrand((List<Brand>) basicResponse.getData()); fetchNearbyBrands(); break;
                            case "ERROR" : stopLoading(); fetchNearbyBrands(); showError(basicResponse.getError()); break;
                        }
                    }
                });
    }

    void fetchNearbyBrands(){

        mainViewModel.fetchNearbyCatBrand(category.getName())
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" : showLoading(); break;
                            case "SUCCESS" : updateNearbyBrand((List<Brand>) basicResponse.getData());
                                stopLoading(); fetchBrandsCount(); break;
                            case "ERROR" : stopLoading(); fetInfCount(); showError(basicResponse.getError()); break;
                        }
                    }
                });
    }

    void fetchBrandsCount(){
        mainViewModel.fetchCatBrandCount(category.getName())
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "SUCCESS" : brandCount = (int) basicResponse.getData();
                            binding.catSubText.setText(brandCount + " Brands");
                        }
                    }
                });
    }


    PopBrandAdapter popBrandAdapter;
    void updateTrendBrand(List<Brand> brands){

        if(popBrandAdapter == null){
            popBrandAdapter = new PopBrandAdapter(this, this);
            binding.catPopBrandRecycler.setAdapter(popBrandAdapter);
            binding.catPopBrandRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }
        popBrandAdapter.updateList(brands);

        if(brands == null || brands.isEmpty())
            binding.catBrnPopEmptyParent.setVisibility(View.VISIBLE);
        else
            binding.catBrnPopEmptyParent.setVisibility(View.GONE);
    }

    BrandListAdapter nearBrandAdapter;
    void updateNearbyBrand(List<Brand> brands){
        if(nearBrandAdapter == null){
            nearBrandAdapter = new BrandListAdapter(this,this);
            binding.catNearBrndRecycler.setAdapter(nearBrandAdapter);
            binding.catNearBrndRecycler.setLayoutManager(new LinearLayoutManager(this));
        }
        nearBrandAdapter.updateList(brands);


        if(brands == null || brands.isEmpty())
            binding.catBrnNearbyEmptyParent.setVisibility(View.VISIBLE);
        else
            binding.catBrnNearbyEmptyParent.setVisibility(View.GONE);
    }


    void setHeroUi(){

        binding.actCatLabel.setText(category.getName());
        switch (category.getName()){
            case "Fitness" : binding.actCatBackImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.cat_fitness_big)); break;
            case "Travel" : binding.actCatBackImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.cat_travel_big)); break;
            case "Food" : binding.actCatBackImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.cat_food_big)); break;
            case "Fashion" : binding.actCatBackImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.cat_fashion_big)); break;
            case "Creative" : binding.actCatBackImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.cat_creative_big)); break;
            case "Sports" : binding.actCatBackImage.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.cat_sports_big)); break;
        }

        binding.backBtnCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAfterTransition();
            }
        });
    }

    void getCategory(){
        Intent intent = getIntent();
        String categoryString = intent.getStringExtra("CATEGORY");
        if(categoryString == null){
            Toast.makeText(this, "Failed to open", Toast.LENGTH_SHORT).show();
            finishAfterTransition();
            return;
        }
        switch (categoryString){
            case "Fitness" : category = new Category("001",categoryString); break;
            case "Travel" : category = new Category("002",categoryString); break;
            case "Food" : category = new Category("003",categoryString); break;
            case "Fashion" : category = new Category("004",categoryString); break;
            case "Creative" : category = new Category("005",categoryString); break;
            case "Sports" : category = new Category("006",categoryString); break;
        }
    }


    void setStatusBarColor(){
        switch (category.getName()){
            case "Fitness" : getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.fitness)); break;
            case "Travel" : getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.travel)); break;
            case "Food" : getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.food)); break;
            case "Fashion" : getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.fashion)); break;
            case "Creative" : getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.creative)); break;
            case "Sports" : getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.sports)); break;
        }
    }

    void setSharedTransitions(){
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.cat_transition));
        getWindow().setSharedElementExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.cat_transition));
        getWindow().setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.cat_transition));
        getWindow().setSharedElementExitTransition(new Explode());
    }

    @Override
    public void onClickInf(Influencer influencer) {
        AppSingleton.getInstance().setSelectedInfluencer(influencer);
        Intent intent = new Intent(this, InfluencerActivity.class);
        intent.putExtra("INFLUENCER",influencer.getInfId());
        startActivity(intent);
    }


    void showLoading(){
        binding.catSwipe.setRefreshing(true);
        binding.infCatLayout.setVisibility(View.GONE);
        binding.brandCatLayout.setVisibility(View.GONE);
    }
    void stopLoading(){
        binding.catSwipe.setRefreshing(false);
        if(userType.equals("BRAND"))
             binding.infCatLayout.setVisibility(View.VISIBLE);
        else
            binding.brandCatLayout.setVisibility(View.VISIBLE);
    }

    void showError(Throwable e){

        if(e == null) return;
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        binding.infCatLayout.setVisibility(View.GONE);
        binding.brandCatLayout.setVisibility(View.GONE);

        Log.d(TAG, "\n\nshowError: Error " + e.toString());
    }

    private static final String TAG = "333 category";

    @Override
    public void onBrandClick(Brand brand) {
        AppSingleton.getInstance().setSelectedBrand(brand);
        startActivity(new Intent(this, BrandActivity.class));
    }
}
