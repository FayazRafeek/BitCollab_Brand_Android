package com.fyzanz.bitcollab.ViewModel;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.palette.graphics.Palette;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Category;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Repository.UserDataRepo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewModel extends ViewModel {


    UserDataRepo userDataRepo;
    public ProfileViewModel() {
        userDataRepo = UserDataRepo.getInstance();
    }

    /* Create Profile Logic (INFLUECNCER) */

    //Pages
    MutableLiveData<Integer> fragPosLive = new MutableLiveData<Integer>(0);
    public LiveData<Integer> getFragPosLive() {
        return fragPosLive;
    }
    public void nextPage(){fragPosLive.setValue(fragPosLive.getValue() + 1);}
    public void prevPage(){fragPosLive.setValue(fragPosLive.getValue() -1);}
    //

    //Data Collection (Inf)
    public Influencer influencer = new Influencer();
    public Bitmap profImgData, coverImgData;
    public Boolean IS_BASIC_SET = false, IS_CONTACT_SET = false, IS_SOCIAL_SET = false, IS_BIO_SET = false;
    public void setInfProfBasicData(String displayName, String dob, Bitmap profileImgData, Bitmap coverImgData){
        influencer.setDisplayName(displayName);
        influencer.setDob(dob);
        this.profImgData = profileImgData; this.coverImgData = coverImgData;
        uploadInfImages();

        Palette.from(coverImgData).generate(palette -> {
            int vibrant = palette.getDarkVibrantColor(0x000000); // <=== color you want
            influencer.setPalletColor(vibrant);
        });

        IS_BASIC_SET = true;
    }
    public void setInfProfContactdata(String phone, String address, String country, String state, String pincode){
        influencer.setAddress(address);
        influencer.setPhone(phone);
        influencer.setCountry(country);
        influencer.setState(state);
        influencer.setPincode(pincode);
        IS_CONTACT_SET = true;
    }
    public void setInfProfSocialData(String insta, String yout){
        influencer.setInstaId(insta);
        influencer.setYoutube(yout);
        IS_SOCIAL_SET = true;
    }
    public void setInfBioData(String bio, List<String> category){
        influencer.setBio(bio);
        influencer.setCategory(new ArrayList<>(category));
        IS_BIO_SET = true;
    }
    //

    //Data Collection (Brand)
    public Brand brand;
    public Bitmap brnLogoImgBit, brnCoverimgBit;
    public Boolean IS_BRAND_BASIC_SET = false,IS_BRN_CONTACT_SET = false,IS_BRN_BIO_SET = false;
    public void setBrnProfBasicData(String brandName,String tagline,Bitmap logoImage, Bitmap coverImgData){
        if(brand == null) brand = new Brand();
        brand.setBrandName(brandName);
        brand.setTagline(tagline);
        brnLogoImgBit = logoImage;
        brnCoverimgBit = coverImgData;

        Palette.from(brnCoverimgBit).generate(palette -> {
            int vibrant = palette.getDarkVibrantColor(0x000000); // <=== color you want
            brand.setPalleteColor(vibrant);
        });

        uploadBrnImages();
        IS_BRAND_BASIC_SET = true;
    }
    public void setBrnProfContactdata(String phone, String address, String country, String state, String pincode){
        brand.setAddress(address);
        brand.setPhone(phone);
        brand.setCountry(country);
        brand.setState(state);
        brand.setPincode(pincode);
        IS_BRN_CONTACT_SET = true;
    }

    public void setBrnProfBioData(String bio,String website, List<String> categories){
        brand.setBio(bio);
        brand.setWebsiteUrl(website);
        brand.setCategories(new ArrayList<>(categories));
        IS_BRN_BIO_SET = false;
    }


    //Verify Accounts
    public LiveData<BasicResponse> verifyInstaAccount(String userName){
        LiveData<BasicResponse> resp = userDataRepo.verifyInstaAccount(userName);
        return resp;
    }
    public LiveData<BasicResponse> verifyYoutubeAccount(String channel){
        LiveData<BasicResponse> resp = userDataRepo.verifyYoutubeAccount(channel);
        return resp;
    }
    //


    private static final String TAG = "333";
    public LiveData<BasicResponse> uploadInfData(){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();
        String email = mAuth.getCurrentUser().getEmail();
        influencer.setInfId(userId);
        influencer.setEmail(email);
        return userDataRepo.uploadInfData(influencer);

    }

    public LiveData<BasicResponse> uploadBrnData(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();
        String email = mAuth.getCurrentUser().getEmail();
        brand.setBrandId(userId);
        brand.setEmail(email);
        return userDataRepo.uploadBrnData(brand);

    }

    MutableLiveData<BasicResponse> infImagesUploadLive = new MutableLiveData<>(new BasicResponse("LOADING"));
    public void uploadInfImages(){

        BasicResponse resp = new BasicResponse();
        resp.setStatus("LOADING");

        String userId = FirebaseAuth.getInstance().getUid();
        StorageReference profRefs = FirebaseStorage.getInstance().getReference().child("influencers/profile/" + userId);
        StorageReference coverRefs = FirebaseStorage.getInstance().getReference().child("influencers/cover/" + userId);

        userDataRepo.uploadFile(profImgData,profRefs)
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Task success " + task.getResult());
                            influencer.setProfileUrl(task.getResult().toString());
                            userDataRepo.uploadFile(coverImgData,coverRefs)
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Log.d(TAG, "onComplete: Task success " + task.getResult());
                                            influencer.setCoverImgUrl(task.getResult().toString());
                                            resp.setStatus("SUCCESS");
                                            infImagesUploadLive.setValue(resp);
                                        }
                                    });
                        } else Log.d(TAG, "onComplete: Error " + task.getException());
                    }
                });
    }
    public LiveData<BasicResponse> infImageUploadLiveObserve() {return infImagesUploadLive;}



    //Brand Image upload
    MutableLiveData<BasicResponse> brnImagesUploadLive = new MutableLiveData<>(new BasicResponse("LOADING"));
    public void uploadBrnImages(){

        BasicResponse resp = new BasicResponse();
        resp.setStatus("LOADING");

        String userId = FirebaseAuth.getInstance().getUid();
        StorageReference profRefs = FirebaseStorage.getInstance().getReference().child("brand/logo/" + userId);
        StorageReference coverRefs = FirebaseStorage.getInstance().getReference().child("brand/cover/" + userId);

        userDataRepo.uploadFile(brnLogoImgBit,profRefs)
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Task success " + task.getResult());
                            brand.setLogoImgUrl(task.getResult().toString());
                            userDataRepo.uploadFile(brnCoverimgBit,coverRefs)
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            Log.d(TAG, "onComplete: Task success " + task.getResult());
                                            brand.setCoverImgUrl(task.getResult().toString());
                                            resp.setStatus("SUCCESS");
                                            brnImagesUploadLive.setValue(resp);
                                        }
                                    });
                        } else Log.d(TAG, "onComplete: Error " + task.getException());
                    }
                });
    }
    public LiveData<BasicResponse> brnImageUploadLiveObserve() {return brnImagesUploadLive;}

}
