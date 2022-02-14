package com.fyzanz.bitcollab.Model.Repository;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fyzanz.bitcollab.BaseApplication;
import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Category;
import com.fyzanz.bitcollab.Model.Data.Influencer;
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
    public MainRepo() {
        this.context = BaseApplication.getContext();
        firestore = FirebaseFirestore.getInstance();
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
//
//        firestore.collection("TestingT")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            List<Influencer> infList = new ArrayList<>();
////                            for (DocumentSnapshot doc : task.getResult())
////                                infList.add( doc.toObject(Influencer.class));
//                            Log.d(TAG, "onComplete: Pop List " + infList.size() + " " + task.getResult().getDocuments()  );
//                            resp.setStatus("SUCCESS"); resp.setData(infList);
//                        } else
//                            resp.setStatus("ERROR"); resp.setError(task.getException());
//
//                        live.setValue(resp);
//                    }
//                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Influencer> infList = new ArrayList<>();
                infList.add(new Influencer("101",true,"Rihana Ray"));
                infList.add(new Influencer("102",true,"Jack Synder"));
                infList.add(new Influencer("103",true,"Ken Adams"));
                resp.setStatus("SUCCESS"); resp.setData(infList);
                live.setValue(resp);
            }
        },500);

        return live;
    }
}
