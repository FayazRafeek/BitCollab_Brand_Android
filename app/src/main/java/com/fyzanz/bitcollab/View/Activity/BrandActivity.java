package com.fyzanz.bitcollab.View.Activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.ViewModel.MainViewModel;
import com.fyzanz.bitcollab.databinding.ActivityBrandBinding;
import com.fyzanz.bitcollab.databinding.BrandCatItemBinding;

import java.util.ArrayList;
import java.util.List;

public class BrandActivity extends AppCompatActivity {

    ActivityBrandBinding binding;
    Brand brand;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        brand = AppSingleton.getInstance().getSelectedBrand();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(brand != null && brand.getPalleteColor() != null)
            getWindow().setStatusBarColor(brand.getPalleteColor());
        else
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = ActivityBrandBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(brand == null) fetchBrand();
        else updateUi();



        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlefavoriteClick();
            }
        });

        IS_FAV = mainViewModel.checkInfInFav(brand.getBrandId());

        if (IS_FAV){
            favActiveUi();
        } else favInactiveUi();

    }

    void fetchBrand(){
        String brandId = getIntent().getStringExtra("BRAND_ID");
        if(brandId == null) finish();
    }

    BrandCatAdapter brandCatAdapter;
    void updateUi(){

        Glide.with(this)
                .load(brand.getCoverImgUrl())
                .centerCrop().into(binding.brnProfBack);
        Glide.with(this)
                .load(brand.getLogoImgUrl())
                .centerCrop().into(binding.brnProfileImage);

        binding.brandProfName.setText(brand.getBrandName());
        binding.brandTagline.setText(brand.getTagline());

        binding.brnProfBio.setText(brand.getBio());

        String location = brand.getState() + ", " + brand.getCountry();
        binding.brandProfLocation.setText(location);
        binding.brandProfWebsite.setText(brand.getWebsiteUrl());

        brandCatAdapter = new BrandCatAdapter(this);
        binding.brandCatRecycler.setAdapter(brandCatAdapter);
        binding.brandCatRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        brandCatAdapter.updateList(brand.getCategories());

        binding.brndBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    Boolean IS_FAV = false;
    void handlefavoriteClick(){

        if(!IS_FAV){
            mainViewModel.addBrandtoFav(brand);
            IS_FAV = true;
            favActiveUi();
        } else {
            mainViewModel.removeBrandFromFav(brand);
            IS_FAV = false;
            favInactiveUi();
        }

    }

    void favActiveUi(){
        Drawable drawable = getResources().getDrawable(R.drawable.ic_heart_fill).mutate();
        drawable.setColorFilter(getResources().getColor(R.color.red_300), PorterDuff.Mode.SRC_ATOP);
        binding.favoriteBtn.setIcon(drawable);
        binding.favoriteBtn.setBackgroundColor(ContextCompat.getColor(this,R.color.orange_300));
    }

    void favInactiveUi(){
        Drawable drawable = getResources().getDrawable(R.drawable.ic_fav_outline).mutate();
        drawable.setColorFilter(getResources().getColor(R.color.orange_400), PorterDuff.Mode.SRC_ATOP);
        binding.favoriteBtn.setIcon(drawable);
        binding.favoriteBtn.setBackgroundColor(ContextCompat.getColor(this,R.color.orange_100));
    }



    public static class BrandCatAdapter extends RecyclerView.Adapter<BrandCatAdapter.BrandCatVH>{

        Context context;
        List<String> category = new ArrayList<>();

        public BrandCatAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public BrandCatVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            BrandCatItemBinding binding = BrandCatItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            return new BrandCatVH(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull BrandCatVH holder, int position) {

            String cat = category.get(position);
            holder.binding.brandCatTitle.setText(cat);

            switch (cat){
                case "Sports" : holder.binding.brandCatIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_sports)); break;
                case "Fitness" : holder.binding.brandCatIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_fitness)); break;
                case "Fashion" : holder.binding.brandCatIcon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_fashion)); break;
            }
        }

        @Override
        public int getItemCount() {
            return category.size();
        }

        void updateList(List<String> list){
            this.category = list;
            notifyDataSetChanged();
        }

        public class BrandCatVH extends RecyclerView.ViewHolder{

            BrandCatItemBinding binding;
            public BrandCatVH(@NonNull BrandCatItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
}
