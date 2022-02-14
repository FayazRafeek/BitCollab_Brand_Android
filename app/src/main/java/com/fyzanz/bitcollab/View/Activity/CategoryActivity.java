package com.fyzanz.bitcollab.View.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyzanz.bitcollab.Model.Data.Category;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.View.Adapter.InfluencerListAdapter;
import com.fyzanz.bitcollab.View.Adapter.PopularInfAdapter;
import com.fyzanz.bitcollab.databinding.ActivityCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements InfluencerListAdapter.OnInfluencerClick {

    ActivityCategoryBinding binding;
    Category category;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        setSharedTransitions();
        getCategory();
        setStatusBarColor();


        binding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setHeroUi();

        setUpTrendInf();
        setUpNearbyInf();
    }




    PopularInfAdapter popularInfAdapter;
    void setUpTrendInf(){
        if(popularInfAdapter == null){
            popularInfAdapter = new PopularInfAdapter("CATEGORY",this);
            binding.catPopRecycler.setAdapter(popularInfAdapter);
            binding.catPopRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        }
        List<Influencer> demoList = new ArrayList<>();
        demoList.add(new Influencer("1","Suzean","Mark"));
        demoList.add(new Influencer("1","Bell","Fort"));
        demoList.add(new Influencer("1","Romelu","Lukaku"));
        demoList.add(new Influencer("1","Cris","Evans"));

        popularInfAdapter.updateAdapter(demoList);
    }


    InfluencerListAdapter influencerListAdapter;
    void setUpNearbyInf(){

        if(influencerListAdapter == null){
            influencerListAdapter = new InfluencerListAdapter(this,"CATEGORY");
            binding.catNearInfRecycler.setAdapter(influencerListAdapter);
            binding.catNearInfRecycler.setLayoutManager(new LinearLayoutManager(this));
        }

        List<Influencer> demoList = new ArrayList<>();
        demoList.add(new Influencer("1","Cristiano","Ronaldo"));
        demoList.add(new Influencer("2","Bell","Fort"));
        demoList.add(new Influencer("3","Romelu","Lukaku"));
        demoList.add(new Influencer("4","Cris","Evans"));
        influencerListAdapter.updateAdapter(demoList);
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
        Intent intent = new Intent(this, InfluencerActivity.class);
        intent.putExtra("INFLUENCER",influencer.getInfId());
        startActivity(intent);
    }

}
