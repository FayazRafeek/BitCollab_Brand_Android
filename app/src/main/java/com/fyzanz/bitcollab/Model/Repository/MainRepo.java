package com.fyzanz.bitcollab.Model.Repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fyzanz.bitcollab.BaseApplication;
import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Campaign;
import com.fyzanz.bitcollab.Model.Data.Category;
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

        try {
            firestore.collection("Influencers")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){

                                List<Influencer> infList = new ArrayList<>();
                                for (DocumentSnapshot doc : task.getResult())
                                    infList.add(doc.toObject(Influencer.class));
                                resp.setStatus("SUCCESS"); resp.setData(infList);
                            } else
                                resp.setStatus("ERROR"); resp.setError(task.getException());

                            live.setValue(resp);
                        }
                    });
        } catch (RuntimeException e){
            resp.setStatus("ERROR"); resp.setError(e);
            live.setValue(resp);
        }


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
        return appDatabase.influencerDao().getFavInfById(infId) != null;
    }

    public List<Influencer> getFavInfluencers(){
        return appDatabase.influencerDao().getAll();
    }
    public void addToFav(Influencer influencer){
        appDatabase.influencerDao().insert(influencer);
    }
    public void removeFromfav(Influencer influencer){
        appDatabase.influencerDao().delete(influencer);
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


    //Category Influencer Fetching
    public LiveData<BasicResponse> getPopCatInfluencer(String category){

        Query query = firestore.collection("Influencers").whereArrayContains("category",category).orderBy("popular").limit(8);

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                            List<Influencer> list = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult())
                                list.add(doc.toObject(Influencer.class));
                            resp.setData(list);
                        } else {
                            resp.setStatus("ERROR"); resp.setError(task.getException());
                        }

                        live.setValue(resp);
                    }
                });

        return live;
    }

    public LiveData<BasicResponse> getNearbyCatInfluencer(String category){

        int pincode = 682002;

        Query query = firestore.collection("Influencers").whereArrayContains("category",category)
                .whereGreaterThanOrEqualTo("pincode",pincode - 100).whereLessThanOrEqualTo("pincode",pincode + 100)
                .limit(20);

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                            List<Influencer> list = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult())
                                list.add(doc.toObject(Influencer.class));
                            resp.setData(list);
                        } else {
                            resp.setStatus("ERROR"); resp.setError(task.getException());
                        }

                        live.setValue(resp);
                    }
                });

        return live;
    }

    public LiveData<BasicResponse> getCatInfluencerCount(String category){

        Query query = firestore.collection("Influencers").whereArrayContains("category",category);

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                            if(task.getResult().isEmpty())
                                resp.setData(0);
                            else resp.setData(task.getResult().size());
                        } else {
                            resp.setStatus("ERROR"); resp.setError(task.getException());
                        }

                        live.setValue(resp);
                    }
                });

        return live;
    }

    //

    //Category Brand Fetching

    public LiveData<BasicResponse> getPopCatBrand(String category){

        Query query = firestore.collection("Brands").whereArrayContains("categories",category).orderBy("popular").limit(8);

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                            List<Brand> list = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult())
                                list.add(doc.toObject(Brand.class));
                            resp.setData(list);
                        } else {
                            resp.setStatus("ERROR"); resp.setError(task.getException());
                        }

                        live.setValue(resp);
                    }
                });

        return live;
    }

    public LiveData<BasicResponse> getNearbyCatBrand(String category){

        int pincode = 682002;

        Query query = firestore.collection("Brands").whereArrayContains("categories",category)
                .whereGreaterThanOrEqualTo("pincode",pincode - 100).whereLessThanOrEqualTo("pincode",pincode + 100)
                .limit(20);

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                            List<Brand> list = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult())
                                list.add(doc.toObject(Brand.class));
                            resp.setData(list);
                        } else {
                            resp.setStatus("ERROR"); resp.setError(task.getException());
                        }

                        live.setValue(resp);
                    }
                });

        return live;
    }

    public LiveData<BasicResponse> getCatBrandCount(String category){

        Query query = firestore.collection("Brands").whereArrayContains("categories",category);

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            resp.setStatus("SUCCESS");
                            if(task.getResult().isEmpty())
                                resp.setData(0);
                            else resp.setData(task.getResult().size());
                        } else {
                            resp.setStatus("ERROR"); resp.setError(task.getException());
                        }

                        live.setValue(resp);
                    }
                });

        return live;
    }


    //Campaigms
    public LiveData<BasicResponse> fetchRecCampaigns(){

        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        firestore
                .collection("Campaigns")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<Campaign> campaigns = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult()){
                                Campaign campaign = doc.toObject(Campaign.class);
                                campaigns.add(campaign);
                            }
                            Log.d(TAG, "onComplete: Campaign success " + campaigns.size());
                            resp.setData(campaigns);
                            resp.setStatus("SUCCESS");

                        } else resp.setStatus("ERROR");

                        live.setValue(resp);
                    }
                });

        return live;
    }

    public LiveData<BasicResponse> fetchBrnCampaigns(String brandId){

        Log.d(TAG, "fetchBrnCampaigns: Brand Id camp " + brandId);
        BasicResponse resp = new BasicResponse("LOADING");
        MutableLiveData<BasicResponse> live = new MutableLiveData<>(resp);

        firestore
                .collection("Campaigns")
                .whereEqualTo("brandId",brandId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            List<Campaign> campaigns = new ArrayList<>();
                            for(DocumentSnapshot doc : task.getResult()){
                                Campaign campaign = doc.toObject(Campaign.class);
                                campaigns.add(campaign);
                            }
                            Log.d(TAG, "onComplete: Campaign success " + campaigns.size());
                            resp.setData(campaigns);
                            resp.setStatus("SUCCESS");

                        } else resp.setStatus("ERROR");

                        live.setValue(resp);
                    }
                });

        return live;
    }

}
