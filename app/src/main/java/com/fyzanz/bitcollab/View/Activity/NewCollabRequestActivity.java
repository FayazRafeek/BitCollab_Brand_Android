package com.fyzanz.bitcollab.View.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.CollabRequest;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Repository.UserDataRepo;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.ViewModel.MainViewModel;
import com.fyzanz.bitcollab.databinding.ActivityNewCollabRequestBinding;

public class NewCollabRequestActivity extends AppCompatActivity {

    ActivityNewCollabRequestBinding binding;
    Influencer influencer;
    Brand brand;
    UserDataRepo userDataRepo;
    String userType = "INFLUENCER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNewCollabRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        userDataRepo = UserDataRepo.getInstance();
        userType = AppSingleton.getInstance().getUSER_TYPE();

        if(userType.equals("INFLUENCER")){
            brand = AppSingleton.getInstance().getSelectedBrand();
            setUpBrnUi();
        } else {
            influencer = AppSingleton.getInstance().getSelectedInfluencer();
            setUpInfUi();
        }

        binding.favBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        

    }

    void setUpInfUi(){

        if(influencer == null) {
            finish();
            return;
        }

        binding.infLayoutParent.setVisibility(View.VISIBLE);
        binding.infItemLayout.infListName.setText(influencer.getDisplayName());
        
        Glide.with(this)
                .load(influencer.getProfileUrl())
                .centerCrop()
                .into(binding.infItemLayout.infListImage);
        
        binding.infItemLayout.socialInfo.setVisibility(View.VISIBLE);
        binding.infItemLayout.listFavBtn.setVisibility(View.GONE);

        StringBuilder catSt = new StringBuilder();
        if(influencer.getCategory() != null){
            int i=0, size = influencer.getCategory().size();
            for(String s : influencer.getCategory()){
                catSt.append(s);
                if(i < size - 1)
                    catSt.append(" | ");
                i++;
            }
        }
        String category = catSt.toString();

        if(category.length() > 24) category = category.substring(0,24);
        binding.infItemLayout.infListCat.setText(category);

        binding.reqSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendingReqToInf();
            }
        });
    }

    void setUpBrnUi(){

        if(brand == null) {
            finish();
            return;
        }

        binding.infLayoutParent.setVisibility(View.GONE);
        binding.brnLayoutParent.setVisibility(View.VISIBLE);
        binding.brandItemLayout.brandListName.setText(brand.getBrandName());
        binding.brandItemLayout.brandProfLocation.setText(brand.getState() + ", " + brand.getCountry());

        Glide.with(this)
                .load(brand.getLogoImgUrl())
                .centerCrop()
                .into(binding.brandItemLayout.brandLogoImage);

        StringBuilder catSt = new StringBuilder();
        if(brand.getCategories() != null){
            int i=0, size = brand.getCategories().size();
            for(String s : brand.getCategories()){
                catSt.append(s);
                if(i < size - 1)
                    catSt.append(" | ");
                i++;
            }
        }
        String category = catSt.toString();

        if(category.length() > 24) category = category.substring(0,24);
        binding.brandItemLayout.brandListCat.setText(category);

        binding.camNameLay.setVisibility(View.GONE);


        binding.reqSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendingReqToBrand();
            }
        });
    }


    private static final String TAG = "333";
    Brand userBrand; Influencer userInfluencer;
    void startSendingReqToInf(){
        binding.reqSendProgress.setVisibility(View.VISIBLE);
        binding.reqSendBtn.setEnabled(false);

        userBrand = userDataRepo.getBrnLocalData();
        if(userBrand == null){
            Toast.makeText(this , "Brand profile not found", Toast.LENGTH_SHORT).show();
            return;
        } else {

            String reqTitle = binding.reqTitleInp.getText().toString();
            String campName = binding.camNameInp.getText().toString();
            String reqBody = binding.reqBodyInp.getText().toString();

            String reqId = String.valueOf(System.currentTimeMillis());

            CollabRequest collabRequest = new CollabRequest();

            collabRequest.setReqId(reqId);
            collabRequest.setReqTitle(reqTitle);
            collabRequest.setReqBody(reqBody);
            collabRequest.setCampaignName(campName);

            collabRequest.setInfId(influencer.getInfId());
            collabRequest.setInfName(influencer.getDisplayName());
            collabRequest.setInfLogoUrl(influencer.getProfileUrl());
            collabRequest.setInfLocation(influencer.getState());

            StringBuilder catSt = new StringBuilder();
            if(influencer.getCategory() != null){
                int i=0, size = influencer.getCategory().size();
                for(String s : influencer.getCategory()){
                    catSt.append(s);
                    if(i < size - 1)
                        catSt.append(" | ");
                    i++;
                }
            }
            String infCategory = catSt.toString();

            if(infCategory.length() > 24) infCategory = infCategory.substring(0,24);
            collabRequest
                    .setInfCat(infCategory);


            collabRequest.setBrandId(userBrand.getBrandId());
            collabRequest.setBrandName(userBrand.getBrandName());
            collabRequest.setBrandLogoUrl(userBrand.getLogoImgUrl());
            collabRequest.setBrandLoc(userBrand.getState());
            if(userBrand.getCategories() != null){
                catSt = new StringBuilder();
                int i=0, size = userBrand.getCategories().size();
                for(String s : userBrand.getCategories()){
                    catSt.append(s);
                    if(i < size - 1)
                        catSt.append(" | ");
                    i++;
                }
            }
            String category = catSt.toString();

            if(category.length() > 24) category = category.substring(0,24);
            collabRequest
                    .setBrandCat(category);



            collabRequest.setTs(String.valueOf(System.currentTimeMillis()));

            collabRequest.setType("B_2_I");

            userDataRepo.sendReqToInf(collabRequest)
                    .observe(this, new Observer<BasicResponse>() {
                        @Override
                        public void onChanged(BasicResponse basicResponse) {
                            if(basicResponse.getStatus().equals("SUCCESS")){
                                Toast.makeText(NewCollabRequestActivity.this, "Request has been sent", Toast.LENGTH_SHORT).show();
                                binding.reqSendProgress.setVisibility(View.GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                },800);
                            }
                        }
                    });

        }
    }

    void startSendingReqToBrand(){

        binding.reqSendProgress.setVisibility(View.VISIBLE);
        binding.reqSendBtn.setEnabled(false);

        userInfluencer = userDataRepo.getInfLocalData();
        if(userInfluencer == null){
            Toast.makeText(this , "Brand profile not found", Toast.LENGTH_SHORT).show();
            return;
        } else {

            String reqTitle = binding.reqTitleInp.getText().toString();
            String reqBody = binding.reqBodyInp.getText().toString();

            String reqId = String.valueOf(System.currentTimeMillis());

            CollabRequest collabRequest = new CollabRequest();

            collabRequest.setReqId(reqId);
            collabRequest.setReqTitle(reqTitle);
            collabRequest.setReqBody(reqBody);

            collabRequest.setInfId(userInfluencer.getInfId());
            collabRequest.setInfName(userInfluencer.getDisplayName());
            collabRequest.setInfLogoUrl(userInfluencer.getProfileUrl());

            StringBuilder catSt = new StringBuilder();
            if(userInfluencer.getCategory() != null){
                int i=0, size = userInfluencer.getCategory().size();
                for(String s : userInfluencer.getCategory()){
                    catSt.append(s);
                    if(i < size - 1)
                        catSt.append(" | ");
                    i++;
                }
            }
            String category = catSt.toString();

            if(category.length() > 24) category = category.substring(0,24);
            collabRequest
                    .setInfCat(category);


            collabRequest.setBrandId(brand.getBrandId());
            collabRequest.setBrandName(brand.getBrandName());
            collabRequest.setBrandLogoUrl(brand.getLogoImgUrl());
            collabRequest.setBrandLoc(brand.getState());

            StringBuilder catStBrn = new StringBuilder();
            if(brand.getCategories() != null){
                int i=0, size = brand.getCategories().size();
                for(String s : brand.getCategories()){
                    catStBrn.append(s);
                    if(i < size - 1)
                        catStBrn.append(" | ");
                    i++;
                }
            }
            String categoryBrn = catStBrn.toString();
            if(catStBrn.length() > 24) categoryBrn = catStBrn.substring(0,24);
            collabRequest
                    .setBrandCat(categoryBrn);


            collabRequest.setTs(String.valueOf(System.currentTimeMillis()));

            collabRequest.setType("I_2_B");

            userDataRepo.sendReqToBrn(collabRequest)
                    .observe(this, new Observer<BasicResponse>() {
                        @Override
                        public void onChanged(BasicResponse basicResponse) {
                            if(basicResponse.getStatus().equals("SUCCESS")){
                                Toast.makeText(NewCollabRequestActivity.this, "Request has been sent", Toast.LENGTH_SHORT).show();
                                binding.reqSendProgress.setVisibility(View.GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                },800);
                            }
                        }
                    });

        }
    }
}
