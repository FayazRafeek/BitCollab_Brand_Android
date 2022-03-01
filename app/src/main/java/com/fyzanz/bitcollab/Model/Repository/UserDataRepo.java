package com.fyzanz.bitcollab.Model.Repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fyzanz.bitcollab.BaseApplication;
import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

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
}
