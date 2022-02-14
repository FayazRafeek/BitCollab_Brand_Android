package com.fyzanz.bitcollab.View.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.fyzanz.bitcollab.Model.Data.Category;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.View.Adapter.CategoryAdapter;
import com.fyzanz.bitcollab.View.Adapter.InfluencerListAdapter;
import com.fyzanz.bitcollab.View.Adapter.PopularInfAdapter;
import com.fyzanz.bitcollab.ViewModel.MainViewModel;
import com.fyzanz.bitcollab.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClick, InfluencerListAdapter.OnInfluencerClick {

    private static final String TAG = "333";

    ActivityMainBinding binding;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavigationDrawer();

        setupCategory();

        setUpPopularInf();
    }

    CategoryAdapter categoryAdapter;
    void setupCategory(){
        categoryAdapter = new CategoryAdapter(this);
        binding.categoryRecycler.setAdapter(categoryAdapter);
        binding.categoryRecycler.setLayoutManager(new GridLayoutManager(this,2));
    }


    PopularInfAdapter popularInfAdapter;
    void setUpPopularInf(){
        if(popularInfAdapter == null){
            popularInfAdapter = new PopularInfAdapter("HOME",this);
            binding.homePopRecycler.setAdapter(popularInfAdapter);
            binding.homePopRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        }
        List<Influencer> demoList = new ArrayList<>();
        demoList.add(new Influencer("1","Suzean","Mark"));
        demoList.add(new Influencer("1","Bell","Fort"));
        demoList.add(new Influencer("1","Romelu","Lukaku"));
        demoList.add(new Influencer("1","Cris","Evans"));

        popularInfAdapter.updateAdapter(demoList);
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
                }
                return true;
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
        startActivity(intent);
    }
}