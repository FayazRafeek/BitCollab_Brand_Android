package com.fyzanz.bitcollab.View.Activity;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.ViewModel.MainViewModel;
import com.fyzanz.bitcollab.databinding.ActivityInfluencerBinding;

public class InfluencerActivity extends AppCompatActivity {

    ActivityInfluencerBinding binding;
    Influencer influencer;
    String userType;

    MainViewModel mainViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        influencer = AppSingleton.getInstance().getSelectedInfluencer();
        if(influencer != null && influencer.getPalletColor() != null)
            getWindow().setStatusBarColor(influencer.getPalletColor());
         else
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding = ActivityInfluencerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.infBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userType = AppSingleton.getInstance().getUSER_TYPE();
        if(influencer == null) fetchInfluencer();
        else
        setUpUi();
    }


    void setUpUi(){

        Glide.with(this)
                .load(influencer.getProfileUrl()).centerCrop()
                .into(binding.infProfileImage);

        Glide.with(this)
                .load(influencer.getCoverImgUrl()).centerCrop()
                .into(binding.infProfBack);

        binding.infProfName.setText(influencer.getDisplayName());
        StringBuilder catSt = new StringBuilder();
        if(influencer.getCategory() != null){
            int i=0;
            for(String s : influencer.getCategory()){
                catSt.append(i > 0 ? " | " : "").append(s);
                i++;
            }
        }
        String category = catSt.toString();
        binding.infProfCat.setText(category);

        String location = influencer.getState() + "," + influencer.getCountry();
        binding.infProfLocation.setText(location);

        binding.infProfBio.setText(influencer.getBio());

        if(influencer.getInstaId() != null){
            binding.instaCardParent.setVisibility(View.VISIBLE);
            binding.instaId.setText("@" + influencer.getInstaId());
        }
        if(influencer.getYoutube() != null){
            binding.youtubeCardParent.setVisibility(View.VISIBLE);
            binding.youtubeId.setText(influencer.getYoutube());
        }


        binding.favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handlefavoriteClick();
            }
        });


        IS_FAV = mainViewModel.checkInfInFav(influencer.getInfId());

        if (IS_FAV){
            favActiveUi();
        } else favInactiveUi();

    }
    Boolean IS_FAV = false;

    void fetchInfluencer(){
        String infId = getIntent().getStringExtra("INFLUENCER");
        if(infId == null) finish();
    }


    void handlefavoriteClick(){

        if(!IS_FAV){
            mainViewModel.addInftoFav(influencer);
            IS_FAV = true;
            favActiveUi();
        } else {
            mainViewModel.removeInfFromFav(influencer);
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
}
