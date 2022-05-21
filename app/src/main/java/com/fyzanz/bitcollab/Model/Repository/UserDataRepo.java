package com.fyzanz.bitcollab.Model.Repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fyzanz.bitcollab.BaseApplication;
import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Campaign;
import com.fyzanz.bitcollab.Model.Data.CollabRequest;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserDataRepo {

    //Static instance
    public static UserDataRepo INSTANCE = null;
    public static UserDataRepo getInstance(){
        if(INSTANCE == null) INSTANCE = new UserDataRepo();
        return INSTANCE;
    }
    //


    Context context;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    FirebaseStorage firebaseStorage;
    public UserDataRepo() {
        this.context = BaseApplication.getContext();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
    }



    //Verify InstaAccount
    public LiveData<BasicResponse> verifyInstaAccount(String username){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> verifyLive = new MutableLiveData<>(resp);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resp.setStatus("SUCCESS");
                verifyLive.postValue(resp);
            }
        },3000);

        return verifyLive;
    }

    //Verify Youtube channel
    public LiveData<BasicResponse> verifyYoutubeAccount(String channelName){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> verifyLive = new MutableLiveData<>(resp);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resp.setStatus("SUCCESS");
                verifyLive.postValue(resp);
            }
        },3000);

        return verifyLive;
    }

    public LiveData<BasicResponse> verifyTwitterAccount(String channelName){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> verifyLive = new MutableLiveData<>(resp);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resp.setStatus("SUCCESS");
                verifyLive.postValue(resp);
            }
        },3000);

        return verifyLive;
    }

    //Upload Influencer data (FIRSTTIME)
    public LiveData<BasicResponse> uploadInfData(Influencer influencer){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> verifyLive = new MutableLiveData<>(resp);

        firestore.collection("Influencers")
            .document(influencer.getInfId())
                .set(influencer)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                        } else {
                            resp.setError(task.getException());
                            resp.setStatus("ERROR");
                        }

                        verifyLive.setValue(resp);
                    }
                });

        return verifyLive;
    }

    //Upload barnd data (FIRST)
    public LiveData<BasicResponse> uploadBrnData(Brand brand){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> verifyLive = new MutableLiveData<>(resp);

        firestore.collection("Brands")
                .document(brand.getBrandId())
                .set(brand)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                        } else {
                            resp.setError(task.getException());
                            resp.setStatus("ERROR");
                        }

                        verifyLive.setValue(resp);
                    }
                });

        return verifyLive;
    }

    //Upload New Campaign Data
    public LiveData<BasicResponse> uploadCampaignData(Campaign campaign){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> verifyLive = new MutableLiveData<>(resp);

        firestore.collection("Campaigns")
                .document(campaign.getCampaignId())
                .set(campaign)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                        } else {
                            resp.setError(task.getException());
                            resp.setStatus("ERROR");
                        }

                        verifyLive.setValue(resp);
                    }
                });

        return verifyLive;
    }

    //Upload storage files
    public Task<Uri> uploadFile(Bitmap bitmap, StorageReference refs){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = refs.putBytes(data);

        return uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return refs.getDownloadUrl();
            }
        });
    }

    private static final String TAG = "333";
    public Task<Uri> uploadFile(InputStream data, StorageReference refs){

        Log.d(TAG, "            uploadFile: Uploading level 3");
        UploadTask uploadTask = refs.putStream(data);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Log.d(TAG, "            onComplete: UPLOAD COMPLETE level 3" + task.getException());
                    }
                });

        return uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                Log.d(TAG, "            then: UPLOADED URI " + task.getException());
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return refs.getDownloadUrl();
            }
        });
    }

    public void deleteFile(StorageReference ref){
        ref.delete();
    }
    //

    //Check Inf profile completion
    public LiveData<BasicResponse> checkInfProfStatus(){


        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> verifyLive = new MutableLiveData<>(resp);

        if(mAuth.getUid() != null)
            firestore.collection("Influencers")
                .document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult().exists()){
                            Influencer influencer = task.getResult().toObject(Influencer.class);
                            saveInfDataLocal(influencer);

                            resp.setStatus("SUCCESS");
                            resp.setData("100");
                        } else {
                            resp.setStatus("SUCCESS");
                            resp.setData("0");
                        }
                        verifyLive.setValue(resp);
                    }
                });

        return verifyLive;
    }
    //Check Inf Brand completion
    public LiveData<BasicResponse> checkBrnProfStatus(){


        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> verifyLive = new MutableLiveData<>(resp);

        if(mAuth.getUid() != null)
            firestore.collection("Brands")
                .document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful() && task.getResult().exists()){
                            Brand brand = task.getResult().toObject(Brand.class); saveBrnDataLocal(brand);
                            resp.setStatus("SUCCESS");
                            resp.setData("100");
                        } else {
                            resp.setStatus("SUCCESS");
                            resp.setData("0");
                        }
                        verifyLive.setValue(resp);
                    }
                });

        return verifyLive;
    }


    void saveInfDataLocal(Influencer influencer){

        if(influencer == null) return;

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.USER_DATA_PREF_FILE),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String infData = new Gson().toJson(influencer);
        if(infData == null) return;

        editor.putString("INF_PROFILE_JSON",infData);
        editor.putBoolean("IS_INF_PROFILE_SAVED",true);

        editor.commit();
    }

    void saveBrnDataLocal(Brand brand){

        if(brand == null) return;

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.USER_DATA_PREF_FILE),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String infData = new Gson().toJson(brand);
        if(infData == null) return;

        editor.putString("BRAND_PROFILE_JSON",infData);
        editor.putBoolean("IS_BRAND_PROFILE_SAVED",true);

        editor.commit();

    }


    public Influencer getInfLocalData(){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.USER_DATA_PREF_FILE),Context.MODE_PRIVATE);
        if(preferences.getBoolean("IS_INF_PROFILE_SAVED",false)){
            String infData = preferences.getString("INF_PROFILE_JSON","");
            Type listType = new TypeToken<Influencer>() {}.getType();
            return new Gson().fromJson(infData, listType);

        } else return null;
    }

    public Brand getBrnLocalData(){
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.USER_DATA_PREF_FILE),Context.MODE_PRIVATE);
        if(preferences.getBoolean("IS_BRAND_PROFILE_SAVED",false)){
            String infData = preferences.getString("BRAND_PROFILE_JSON","");
            Type listType = new TypeToken<Brand>() {}.getType();
            return new Gson().fromJson(infData, listType);
        } else return null;
    }

    public LiveData<BasicResponse> sendReqToInf(CollabRequest collabRequest){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> Live = new MutableLiveData<>(resp);

        firestore.collection("Collabs")
                .document(collabRequest.getReqId())
                .set(collabRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                        } else {
                            resp.setError(task.getException()); resp.setStatus("ERROR");
                        }
                        Live.setValue(resp);
                    }
                });

        return Live;
    }


    public LiveData<BasicResponse> sendReqToBrn(CollabRequest collabRequest){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> Live = new MutableLiveData<>(resp);

        firestore.collection("Collabs")
                .document(collabRequest.getReqId())
                .set(collabRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                        } else {
                            resp.setError(task.getException()); resp.setStatus("ERROR");
                        }
                        Live.setValue(resp);
                    }
                });

        return Live;
    }



    public LiveData<BasicResponse> fetchInfCollabReqList(String type){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> Live = new MutableLiveData<>(resp);

        String infId = mAuth.getUid();
        firestore.collection("Collabs")
                .whereEqualTo("infId",infId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            resp.setStatus("ERROR");
                            resp.setError(error);
                        } else {

                            List<CollabRequest> list = new ArrayList<>();
                            for(DocumentSnapshot doc : value.getDocuments()){
                                CollabRequest item = doc.toObject(CollabRequest.class);
                                if(item.getType().equals(type))
                                    list.add(item);
                            }

                            resp.setData(list); resp.setStatus("SUCCESS");
                        }

                        Live.setValue(resp);
                    }
                });

        return Live;
    }


    public LiveData<BasicResponse> fetchBrnCollabReq(String type){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> Live = new MutableLiveData<>(resp);

        String infId = mAuth.getUid();
        firestore.collection("Collabs")
                .whereEqualTo("brandId",infId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            resp.setStatus("ERROR");
                            resp.setError(error);
                        } else {

                            List<CollabRequest> list = new ArrayList<>();
                            for(DocumentSnapshot doc : value.getDocuments()){
                                CollabRequest item = doc.toObject(CollabRequest.class);
                                if(item.getType().equals(type))
                                    list.add(item);
                            }

                            resp.setData(list); resp.setStatus("SUCCESS");
                        }

                        Live.setValue(resp);
                    }
                });

        return Live;
    }
}
