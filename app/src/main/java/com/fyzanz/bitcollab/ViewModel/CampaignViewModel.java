package com.fyzanz.bitcollab.ViewModel;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.palette.graphics.Palette;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Campaign;
import com.fyzanz.bitcollab.Model.Repository.UserDataRepo;
import com.fyzanz.bitcollab.View.Fragments.CampaignCreate.CampCreateStep3Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class CampaignViewModel extends ViewModel {

    UserDataRepo userDataRepo;
    public CampaignViewModel() {
        userDataRepo = UserDataRepo.getInstance();
    }

    //Pages
    MutableLiveData<Integer> fragPosLive = new MutableLiveData<Integer>(0);
    public LiveData<Integer> getFragPosLive() {
        return fragPosLive;
    }
    public void nextPage(){fragPosLive.setValue(fragPosLive.getValue() + 1);}
    public void prevPage(){fragPosLive.setValue(fragPosLive.getValue() -1);}
    //


    public Campaign newCampaign;
    public Boolean STEP_1_COMPLETE = false, STEP_2_COMPLETE = false, STEP_3_COMPLETE = false;
    public void setNewCampStep1Data(String campName, String proTitle, String campDesc){
        String brandId = FirebaseAuth.getInstance().getUid();
        if(newCampaign == null) newCampaign = new Campaign(System.currentTimeMillis() + "",brandId);
        newCampaign.setCampaignName(campName);
        newCampaign.setPromoteTitle(proTitle);
        newCampaign.setCampaignDesc(campDesc);

        fetchAndSetBrandInfo();
    }

    public void setNewCampStep2Data(Boolean insta, Boolean yout, Boolean twitt, ArrayList<String> topics, String startDate, String endData, String bugdet){
        newCampaign.setIntsa(insta);
        newCampaign.setYout(yout);
        newCampaign.setTwitt(twitt);
        newCampaign.setTopics(topics);
        newCampaign.setStartDate(startDate);
        newCampaign.setEndDate(endData);
        newCampaign.setEstBudget(bugdet);
    }

    Bitmap bannerData;
    public void setNewCampStep3Data(Bitmap bannerImage, ArrayList<String> refUrls){
        bannerData = bannerImage;
        Bitmap cropBit =  Bitmap.createBitmap(bannerImage, 0, 0, bannerImage.getWidth(), 10);
        Palette.from(cropBit).generate(palette -> {
            int vibrant = palette.getDominantColor(0x000000); // <=== color you want
            newCampaign.setPalletColor(vibrant + "");
        });
        uploadCampBannerFile();
        newCampaign.setRefFiles(refUrls);
    }

    public LiveData<BasicResponse> uploadCampaignData(){
        return userDataRepo.uploadCampaignData(newCampaign);
    }


    private static final String TAG = "333";
    MutableLiveData<BasicResponse> bannerImageUploadLive = new MutableLiveData<>(new BasicResponse("LOADING"));

    public void uploadCampBannerFile(){

        BasicResponse resp = new BasicResponse();
        resp.setStatus("LOADING");

        String userId = FirebaseAuth.getInstance().getUid();
        StorageReference profRefs = FirebaseStorage.getInstance().getReference().child("campaign/" + userId +"/banner");
        bannerImageUploadLive.setValue(resp);
        userDataRepo.uploadFile(bannerData,profRefs)
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            newCampaign.setBannerUrl(task.getResult().toString());
                            resp.setStatus("SUCCESS");
                        } else resp.setStatus("ERROR");


                        if(!IS_BRAND_DATA_FETCH_COMPLETE)
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bannerImageUploadLive.setValue(resp);
                                }
                            },3000);
                        else
                            bannerImageUploadLive.setValue(resp);
                    }
                });
    }
    public LiveData<BasicResponse> getBannerLive() {return bannerImageUploadLive;}


    public LiveData<BasicResponse> uploadFile(String path, String filename){

        MutableLiveData<BasicResponse> fileUploadLive = new MutableLiveData<>(new BasicResponse("LOADING"));
        BasicResponse resp = new BasicResponse();
        resp.setStatus("LOADING");

        String userId = FirebaseAuth.getInstance().getUid();
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("campaign/" + userId +"/ref/" + filename);

        fileUploadLive.setValue(resp);
        try {
            Log.d(TAG, "        uploadFile: Uploading level 2");
            InputStream input = new FileInputStream(path);
            userDataRepo.uploadFile(input,ref)
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Log.d(TAG, "        uploadFile: Uploading complete level 2");
                            if(task.isSuccessful()){
                                resp.setData(task.getResult().toString());
                                resp.setStatus("SUCCESS");

                            } else resp.setStatus("ERROR");

                            fileUploadLive.setValue(resp);
                        }
                    });

        } catch (FileNotFoundException e) {
            Log.d(TAG, "        uploadFile: Level 2 expection " + e.toString());
            e.printStackTrace();
        }

        return fileUploadLive;
    }

    public void deleteFile(String fileItem){
        String userId = FirebaseAuth.getInstance().getUid();
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("campaign/" + userId +"/ref/" + fileItem);
        userDataRepo.deleteFile(ref);
    }


    Boolean IS_BRAND_DATA_FETCH_COMPLETE = false;
    public void fetchAndSetBrandInfo(){

        String brandId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance()
                .collection("Brands")
                .document(brandId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Brand brand = task.getResult().toObject(Brand.class);
                            newCampaign.setBrandName(brand.getBrandName());
                            newCampaign.setBrandLogoUrl(brand.getLogoImgUrl());
                            IS_BRAND_DATA_FETCH_COMPLETE = true;
                        }
                    }
                });
    }
}
