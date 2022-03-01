package com.fyzanz.bitcollab.Model.Repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fyzanz.bitcollab.BaseApplication;
import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Category;
import com.fyzanz.bitcollab.Model.Data.FavInfluencer;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Room.AppDatabase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainRepo {

    private static final String TAG = "333";

    public static MainRepo INSTANCE = null;

    public static MainRepo getInstance() {
        if(INSTANCE == null) INSTANCE = new MainRepo();
        return INSTANCE;
    }

    FirebaseFirestore firestore;
    Context context;
    AppDatabase appDatabase;
    public MainRepo() {
        this.context = BaseApplication.getContext();
        firestore = FirebaseFirestore.getInstance();
        appDatabase = AppDatabase.getINSTANCE();
    }


    public LiveData<BasicResponse> getInfCategories(){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        firestore.collection("BrandCategories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<Category> catList = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult())
                                catList.add( doc.toObject(Category.class));
                            resp.setStatus("SUCCESS"); resp.setData(catList);
                        } else
                            resp.setStatus("ERROR"); resp.setError(task.getException());

                        live.setValue(resp);
                    }
                });

        return live;
    }

    public LiveData<BasicResponse> getPopularInf(){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        firestore.collection("Influencers")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<Influencer> infList = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult())
                                infList.add( doc.toObject(Influencer.class));
                            resp.setStatus("SUCCESS"); resp.setData(infList);
                        } else
                            resp.setStatus("ERROR"); resp.setError(task.getException());

                        live.setValue(resp);
                    }
                });

        return live;
    }


    public LiveData<BasicResponse> getPopularBrands(){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        firestore.collection("Brands")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<Brand> infList = new ArrayList<>();
                            for (DocumentSnapshot doc : task.getResult())
                                infList.add( doc.toObject(Brand.class));
                            resp.setStatus("SUCCESS"); resp.setData(infList);
                        } else
                            resp.setStatus("ERROR"); resp.setError(task.getException());

                        live.setValue(resp);
                    }
                });

        return live;
    }

    //
    //Room

    //Influencer fav handle
    public Boolean checkIsFav(String infId){
        FavInfluencer fav = appDatabase.influencerDao().getFavInfById(infId);
        return fav != null;
    }
    public List<FavInfluencer> getFavInfluencers(){
        return appDatabase.influencerDao().getAll();
    }
    public void addToFav(Influencer influencer){
        FavInfluencer favInfluencer = new FavInfluencer();
        favInfluencer.setData(influencer);
        appDatabase.influencerDao().insert(favInfluencer);
    }
    public void removeFromfav(Influencer influencer){
        FavInfluencer favInfluencer = new FavInfluencer();
        favInfluencer.setData(influencer);
        appDatabase.influencerDao().delete(favInfluencer);
    }
    //
    public Boolean checkBrnIsFav(String brandId){
        Brand brand = appDatabase.brandDao().getFavBrandById(brandId);
        return brand != null;
    }
    public List<Brand> getFavBrands(){
        return appDatabase.brandDao().getAllFav();
    }
    public void addBrandtoFav(Brand brand){
        appDatabase.brandDao().insert(brand);
    }
    public void removeBrandFromfav(Brand brand){
        appDatabase.brandDao().delete(brand);
    }

    //


}
