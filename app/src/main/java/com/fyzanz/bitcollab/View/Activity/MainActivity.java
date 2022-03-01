package com.fyzanz.bitcollab.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Category;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.View.Adapter.CategoryAdapter;
import com.fyzanz.bitcollab.View.Adapter.InfluencerListAdapter;
import com.fyzanz.bitcollab.View.Adapter.PopBrandAdapter;
import com.fyzanz.bitcollab.View.Adapter.PopularInfAdapter;
import com.fyzanz.bitcollab.ViewModel.MainViewModel;
import com.fyzanz.bitcollab.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClick, PopBrandAdapter.BrandListAction, InfluencerListAdapter.OnInfluencerClick {

    private static final String TAG = "333";

    ActivityMainBinding binding;
    MainViewModel mainViewModel;

    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        userType = AppSingleton.getInstance().getUSER_TYPE();
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigationDrawer();
        setupCategory();

        getPopularInf();
        getPopularBrand();

        if(userType.equals("INFLUENCER")){
            checkInfProfStatus();
        } else {
            checkBrnProfStatus();
        }

        binding.profileCompBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateProfileActivity.class));
            }
        });


        mainViewModel.getPopularInfListLive().observe(this, new Observer<List<Influencer>>() {
            @Override
            public void onChanged(List<Influencer> influencers) {
                Log.d(TAG, "onChanged: Pop Inf change " + influencers);
                updatePopInfRecycler(influencers);
            }
        });
        mainViewModel.getPopularBrnListLive().observe(this, new Observer<List<Brand>>() {
            @Override
            public void onChanged(List<Brand> brands) {
                updatePopularBrand(brands);
            }
        });
    }

    CategoryAdapter categoryAdapter;
    void setupCategory(){
        categoryAdapter = new CategoryAdapter(this);
        binding.categoryRecycler.setAdapter(categoryAdapter);
        binding.categoryRecycler.setLayoutManager(new GridLayoutManager(this,2));
    }


    PopularInfAdapter popularInfAdapter;
    void updatePopInfRecycler(List<Influencer> list){
        if(popularInfAdapter == null){
            popularInfAdapter = new PopularInfAdapter("HOME",this);
            binding.homePopRecycler.setAdapter(popularInfAdapter);
            binding.homePopRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        }
        popularInfAdapter.updateAdapter(list);
    }

    PopBrandAdapter popBrandAdapter;
    void updatePopularBrand(List<Brand> brands){
        if(popBrandAdapter == null){
            popBrandAdapter = new PopBrandAdapter(this,this);
            binding.homePopBrnRecycler.setAdapter(popBrandAdapter);
            binding.homePopBrnRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        }
        popBrandAdapter.updateList(brands);
    }


    void setupNavigationDrawer(){

        binding.menuToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.mainDrawer.openDrawer(GravityCompat.START);
            }
        });

        binding.navigationMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_favorite: startActivity(new Intent(MainActivity.this,FavoriteActivity.class)); break;
                    case R.id.nav_message: startActivity(new Intent(MainActivity.this,MessageActivity.class));
                    case R.id.nav_logout: logout();
                }
                return true;
            }
        });

    }


    //Fetchings...
    void getPopularInf(){
        mainViewModel.getPopularInf()
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" :
                                binding.homePopRecycler.setVisibility(View.GONE);
                                binding.popInfShimmer.setVisibility(View.VISIBLE);
                                binding.popInfShimmer.startShimmer();
                                break;
                            case "SUCCESS" :
                                binding.homePopRecycler.setVisibility(View.VISIBLE);
                                binding.popInfShimmer.setVisibility(View.GONE);
                                binding.popInfShimmer.stopShimmer();
                                mainViewModel.setPopularInfListLive((List<Influencer>) basicResponse.getData());
                                break;
                            case "ERROR" :
                                binding.popInfShimmer.stopShimmer();
                                Toast.makeText(MainActivity.this, "Error fetching", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void getPopularBrand(){
        mainViewModel.getPopularBrand()
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        switch (basicResponse.getStatus()){
                            case "LOADING" :
                                binding.homePopBrnRecycler.setVisibility(View.GONE);
                                binding.popBrnShimmer.setVisibility(View.VISIBLE);
                                binding.popBrnShimmer.startShimmer();
                                break;
                            case "SUCCESS" :
                                binding.homePopBrnRecycler.setVisibility(View.VISIBLE);
                                binding.popBrnShimmer.setVisibility(View.GONE);
                                binding.popBrnShimmer.stopShimmer();
                                mainViewModel.setPopularBrnListLive((List<Brand>) basicResponse.getData());
                                break;
                            case "ERROR" :
                                binding.popBrnShimmer.stopShimmer();
                                Toast.makeText(MainActivity.this, "Error fetching", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onClickCat(CategoryAdapter.CategoryVH viewHolder, String category) {

        getWindow().setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.cat_transition));
        getWindow().setSharedElementExitTransition(new Explode());

        Intent intent = new Intent(this, CategoryActivity.class);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(viewHolder.itemView.findViewById(R.id.cat_back_image), "CAT_TRANS_IMG"),
                Pair.create(viewHolder.itemView.findViewById(R.id.cat_label), "CAT_TRANS_TEXT"));

        intent.putExtra("CATEGORY",category);
        startActivity(intent, options.toBundle());

    }

    @Override
    public void onClickInf(Influencer influencer) {
        Intent intent = new Intent(this, InfluencerActivity.class);
        intent.putExtra("INFLUENCER",influencer.getInfId());
        AppSingleton.getInstance().setSelectedInfluencer(influencer);
        startActivity(intent);
    }

    void logout(){
        SharedPreferences pref = getSharedPreferences(getString(R.string.AUTH_PREF_FILE), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(getString(R.string.IS_LOGIN_KEY), false);
        editor.putString("USER_ID_KEY",null);
        editor.putString("USER_NAME_KEY",null);
        editor.putString("USER_EMAIL_KEY",null);
        editor.putString("USER_TYPE",null);
        AppSingleton.getInstance().setUSER_TYPE("");
        editor.apply();

        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }



    //Influencer
    void checkInfProfStatus(){
        mainViewModel.checkInfProfStatus()
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        Log.d(TAG, "onChanged: Return " + basicResponse.getStatus() + basicResponse.getData());
                        if(basicResponse.getStatus().equals("SUCCESS")){
                            String compl = basicResponse.getData().toString();
                            if(!compl.equals("100")){
                                showProfCompleteUi();
                            } else hideProfComUi();
                        }
                    }
                });
    }

    void checkBrnProfStatus(){
        mainViewModel.checkBrnProfStatus()
                .observe(this, new Observer<BasicResponse>() {
                    @Override
                    public void onChanged(BasicResponse basicResponse) {
                        if(basicResponse.getStatus().equals("SUCCESS")){
                            String compl = basicResponse.getData().toString();
                            if(!compl.equals("100")){
                                showProfCompleteUi();
                            } else hideProfComUi();
                        }
                    }
                });
    }

    void showProfCompleteUi(){
        binding.profileCompBtn.setVisibility(View.VISIBLE);
    }

    void hideProfComUi(){
        binding.profileCompBtn.setVisibility(View.GONE);
    }

    @Override
    public void onBrandClick(Brand brand) {
        Intent intent = new Intent(this, BrandActivity.class);
        intent.putExtra("BRAND_ID",brand.getBrandId());
        AppSingleton.getInstance().setSelectedBrand(brand);
        startActivity(intent);
    }
}