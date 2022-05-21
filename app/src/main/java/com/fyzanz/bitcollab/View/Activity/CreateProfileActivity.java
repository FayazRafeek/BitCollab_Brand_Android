package com.fyzanz.bitcollab.View.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.View.Fragments.BrandProfile.CreateProfBrnAbout;
import com.fyzanz.bitcollab.View.Fragments.BrandProfile.CreateProfBrnBasic;
import com.fyzanz.bitcollab.View.Fragments.BrandProfile.CreateProfBrnContact;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfAbout;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfBasic;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfContact;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfSocial;
import com.fyzanz.bitcollab.ViewModel.ProfileViewModel;
import com.fyzanz.bitcollab.databinding.ActivityCreateProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

public class CreateProfileActivity extends AppCompatActivity {

    ActivityCreateProfileBinding binding;
    String userType;
    ProfileViewModel profileViewModel;
    String type = "CREATE";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        userType = AppSingleton.getInstance().getUSER_TYPE();
        MAX_PAGE  = userType.equals("INFLUENCER") ? 4 : 3;

        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getIntent().getBooleanExtra("EDIT",false)){
            type = "EDIT";
            binding.createProfTitle.setText("Edit Profile");
            if(userType.equals("BRAND")){
                profileViewModel.setBrand(AppSingleton.getInstance().getSelectedBrand());
                profileViewModel.IS_BRAND_BASIC_SET = true;
                profileViewModel.IS_BRN_CONTACT_SET = true;
                profileViewModel.IS_BRN_BIO_SET = true;
            } else {
                profileViewModel.setInfluencer(AppSingleton.INSTANCE.getSelectedInfluencer());
                profileViewModel.IS_BASIC_SET = true;
                profileViewModel.IS_BIO_SET = true;
                profileViewModel.IS_CONTACT_SET = true;
                profileViewModel.IS_SOCIAL_SET = true;
            }
        }

        profileViewModel.getFragPosLive().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, "onChanged: CHANGE " + integer);
                showFragment(integer);
            }
        });

    }

    private static final String TAG = "333";

    int MAX_PAGE;
    void showFragment(int pos){
        if(pos < MAX_PAGE)
            getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.createProfFragCon.getId(), getFragment(pos))
                .commit();
        else finishClickHandle();
    }

    
    void finishClickHandle(){

        binding.createProfFragCon.setVisibility(View.GONE);
        binding.createProfLoader.setVisibility(View.VISIBLE);

        if(userType.equals("INFLUENCER"))
        profileViewModel.infImageUploadLiveObserve().observe(this, new Observer<BasicResponse>() {
            @Override
            public void onChanged(BasicResponse basicResponse) {
                if(basicResponse.getStatus().equals("SUCCESS")){

                    profileViewModel.uploadInfData()
                            .observe(CreateProfileActivity.this, new Observer<BasicResponse>() {
                                @Override
                                public void onChanged(BasicResponse basicResponse) {
                                    switch (basicResponse.getStatus()){
                                        case "SUCCESS" : startActivity(new Intent(CreateProfileActivity.this, MainActivity.class)); finish(); break;
                                        case "ERROR" : binding.createProfLoader.setVisibility(View.GONE);
                                            Toast.makeText(CreateProfileActivity.this, "Error uploading profile", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(CreateProfileActivity.this, basicResponse.getError().toString(), Toast.LENGTH_SHORT).show(); break;
                                    }
                                }
                            });
                }
            }
        });
        else
            profileViewModel.brnImageUploadLiveObserve().observe(this, new Observer<BasicResponse>() {
                @Override
                public void onChanged(BasicResponse basicResponse) {
                    if(basicResponse.getStatus().equals("SUCCESS")){

                        profileViewModel.uploadBrnData()
                                .observe(CreateProfileActivity.this, new Observer<BasicResponse>() {
                                    @Override
                                    public void onChanged(BasicResponse basicResponse) {
                                        switch (basicResponse.getStatus()){
                                            case "SUCCESS" : startActivity(new Intent(CreateProfileActivity.this, MainActivity.class)); finish(); break;
                                            case "ERROR" : binding.createProfLoader.setVisibility(View.GONE);
                                                Toast.makeText(CreateProfileActivity.this, "Error uploading profile", Toast.LENGTH_SHORT).show();
                                                Toast.makeText(CreateProfileActivity.this, basicResponse.getError().toString(), Toast.LENGTH_SHORT).show(); break;
                                        }
                                    }
                                });

                    }
                }
            });


    }

    Fragment getFragment(int pos){
        if(userType.equals("INFLUENCER")){
            switch (pos){
                case 0 : return new CreateProfInfBasic(type);
                case 1 : return new CreateProfInfContact();
                case 2 : return new CreateProfInfSocial();
                case 3 : return new CreateProfInfAbout(type);
            }
        } else {
            switch (pos){
                case 0 : return new CreateProfBrnBasic(type);
                case 1 : return new CreateProfBrnContact();
                case 2 : return new CreateProfBrnAbout(type);
            }
        }
        return null;
    }
}
