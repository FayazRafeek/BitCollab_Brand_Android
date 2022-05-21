package com.fyzanz.bitcollab.View.Activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Repository.UserDataRepo;
import com.fyzanz.bitcollab.Model.Utils.AppSingleton;
import com.fyzanz.bitcollab.R;
import com.fyzanz.bitcollab.View.Adapter.BrandListAdapter;
import com.fyzanz.bitcollab.View.Adapter.InfluencerListAdapter;
import com.fyzanz.bitcollab.View.Adapter.PopBrandAdapter;
import com.fyzanz.bitcollab.View.Fragments.InfluencerProfile.CreateProfInfAbout;
import com.fyzanz.bitcollab.databinding.ActivitySearchBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements PopBrandAdapter.BrandListAction, InfluencerListAdapter.OnInfluencerClick {


    ActivitySearchBinding binding;
    FirebaseFirestore firestore;
    String userType = "INFLUENCER";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        binding.menuToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st = binding.searchLabel.getQuery().toString();
                if(!st.isEmpty())
                    binding.searchLabel.setQuery("",false);
                else finish();
            }
        });

        userType = AppSingleton.getInstance().getUSER_TYPE();;

        binding.searchLabel.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSearching();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.searchLabel.requestFocus();

        binding.searchSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startSearching();
            }
        });

        binding.sortSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSortDialog();
            }
        });

        binding.filterSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterDialog();
            }
        });

        setUpSortDiolog();
        setupFilerDialog();
    }


    private static final String TAG = "333";
    List<Brand> brandSearchList = new ArrayList<>();
    List<Influencer> infSearchList = new ArrayList<>();
    void startSearching(){

        binding.searchLabel.clearFocus();

        binding.searchSwipe.setRefreshing(true);

        String st = binding.searchLabel.getQuery().toString().toLowerCase();

        if(userType.equals("INFLUENCER")){

            Query brandQuery = firestore.collection("Brands");

            brandQuery.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            Log.d(TAG, "onComplete: Completed Brnd" + task.getResult());

                            binding.searchSwipe.setRefreshing(false);
                            if(task.isSuccessful()){

                                brandSearchList = new ArrayList<>();
                                for(DocumentSnapshot doc : task.getResult()){
                                    Brand brand = doc.toObject(Brand.class);
                                    if(brand.getBrandName().toLowerCase().equals(st))
                                        brandSearchList.add(brand);
                                    else {
                                        String[] nameSplit = brand.getBrandName().split(" ");
                                        for(String s : nameSplit){
                                            if(s.trim().toLowerCase().equals(st)){
                                                brandSearchList.add(brand);
                                                break;
                                            }
                                        }
                                    }
                                }
                                updateBrandRecycler();

                            } else {
                                Log.d(TAG, "onComplete: Brand Error " + task.getException());
                                Toast.makeText(SearchActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                updateEmptyUi();
                            }
                        }
                    });

        } else {
            Query infQuery = firestore.collection("Influencers");

            infQuery.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            binding.searchSwipe.setRefreshing(false);
                            if(task.isSuccessful()){

                                infSearchList = new ArrayList<>();
                                for(DocumentSnapshot doc : task.getResult()){
                                    Influencer inf = doc.toObject(Influencer.class);
                                    if(inf.getDisplayName().toLowerCase().equals(st))
                                        infSearchList.add(inf);
                                    else {
                                        String[] nameSplit = inf.getDisplayName().split(" ");
                                        for(String s : nameSplit){
                                            if(s.trim().toLowerCase().equals(st)){
                                                infSearchList.add(inf);
                                                break;
                                            }
                                        }
                                    }
                                }
                                updateInfluencerRecycler();

                            } else {
                                Log.d(TAG, "onComplete: Inf Error " + task.getException());
                                Toast.makeText(SearchActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                updateEmptyUi();
                            }
                        }
                    });
        }


    }


    BrandListAdapter brandListAdapter;
    void updateBrandRecycler(){

        if(brandSearchList != null && brandSearchList.size() > 0){

            binding.searchBrandRecycler.setVisibility(View.VISIBLE);
            binding.brandListLabel.setVisibility(View.VISIBLE);
            if(brandListAdapter == null){
                brandListAdapter = new BrandListAdapter(this,this);
                binding.searchBrandRecycler.setAdapter(brandListAdapter);
                binding.searchBrandRecycler.setLayoutManager(new LinearLayoutManager(this));
            }
            brandListAdapter.updateList(brandSearchList);

        } else {
            binding.searchBrandRecycler.setVisibility(View.GONE);
            binding.brandListLabel.setVisibility(View.GONE);
        }
        updateEmptyUi();
    }

    InfluencerListAdapter influencerListAdapter;
    void updateInfluencerRecycler(){

        if(infSearchList != null && infSearchList.size() > 0){

            binding.searchInfluencerRecycler.setVisibility(View.VISIBLE);
            binding.infListLabel.setVisibility(View.VISIBLE);
            if(influencerListAdapter == null){
                influencerListAdapter = new InfluencerListAdapter(this,"CATEGORY");
                binding.searchInfluencerRecycler.setAdapter(influencerListAdapter);
                binding.searchInfluencerRecycler.setLayoutManager(new LinearLayoutManager(this));
            }
            influencerListAdapter.updateAdapter(infSearchList);

        } else {
            binding.searchInfluencerRecycler.setVisibility(View.GONE);
            binding.infListLabel.setVisibility(View.GONE);
        }

        updateEmptyUi();
    }

    void updateEmptyUi(){

        binding.emptyParent.setVisibility(View.VISIBLE);
        if(brandSearchList.isEmpty() && infSearchList.isEmpty())
            binding.emptyText.setText("No data found!");
        else binding.emptyParent.setVisibility(View.GONE);

    }



    // Sort Dialog
    MutableLiveData<String> sortTypeLive = new MutableLiveData<>("HIGH_POP");
    ImageView sortCloseBtn, highPopCheck, lowPopCheck, nearbyCheck;
    ConstraintLayout highPopParent, lowPopParent, nearbyParent;
    TextView hingPopText, lowPopText, nearbyText;
    MaterialButton sortCanelBtn, sortApplyBtn;

    void showSortDialog(){
        sortSheet.show();
    }
    BottomSheetDialog sortSheet;
    void setUpSortDiolog(){

        sortSheet = new BottomSheetDialog(this);
        sortSheet.setContentView(R.layout.sort_sheet_dialog);

        sortCloseBtn = sortSheet.findViewById(R.id.sort_close_btn);

        highPopParent = sortSheet.findViewById(R.id.high_pop_parent);
        highPopCheck = sortSheet.findViewById(R.id.high_pop_check);
        hingPopText = sortSheet.findViewById(R.id.high_pop_text);

        lowPopText = sortSheet.findViewById(R.id.low_pop_text);
        lowPopCheck = sortSheet.findViewById(R.id.low_pop_check);
        lowPopParent = sortSheet.findViewById(R.id.low_pop_parent);

        nearbyParent = sortSheet.findViewById(R.id.nearby_parent);
        nearbyCheck = sortSheet.findViewById(R.id.nearby_check);
        nearbyText = sortSheet.findViewById(R.id.nearby_text);

        sortCanelBtn = sortSheet.findViewById(R.id.sort_cancel_btn);
        sortApplyBtn = sortSheet.findViewById(R.id.sort_apply_btn);

        sortCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortSheet.cancel();
            }
        });

        highPopCheck.setVisibility(View.VISIBLE);
        hingPopText.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.black));

        highPopParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortTypeLive.setValue("HIGH_POP");

                highPopCheck.setVisibility(View.VISIBLE);
                hingPopText.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.black));

                lowPopCheck.setVisibility(View.INVISIBLE);
                nearbyCheck.setVisibility(View.INVISIBLE);
                lowPopText.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.grey_5));
                nearbyText.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.grey_5));
            }
        });

        lowPopParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortTypeLive.setValue("LOW_POP");

                lowPopCheck.setVisibility(View.VISIBLE);
                lowPopText.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.black));

                highPopCheck.setVisibility(View.INVISIBLE);
                nearbyCheck.setVisibility(View.INVISIBLE);
                hingPopText.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.grey_5));
                nearbyText.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.grey_5));
            }
        });


        nearbyParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortTypeLive.setValue("NEARBY");

                nearbyCheck.setVisibility(View.VISIBLE);
                nearbyText.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.black));

                lowPopCheck.setVisibility(View.INVISIBLE);
                highPopCheck.setVisibility(View.INVISIBLE);
                lowPopText.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.grey_5));
                hingPopText.setTextColor(ContextCompat.getColor(SearchActivity.this,R.color.grey_5));
            }
        });


        sortApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearching();
                sortSheet.cancel();
            }
        });

        sortCanelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortSheet.cancel();
            }
        });

    }
    //


    //Filter dialog
    BottomSheetDialog filterSheet;
    ImageView filterCloseBtn;
    MaterialButton filterCancelBtn, filterApplyBtn;
    MaterialCardView instaFilterCard,youFilterCard,twitFilterCard;
    CreateProfInfAbout.ProfCatSelectAdapter filterCatAdapter;
    RecyclerView filterCatRecycler;
    RadioGroup genderGroup;
    Boolean IS_YOUT_FILTER = false, IS_INSTA_FILTER = false, IS_TWITT_FILTER = false;

    void setupFilerDialog(){

        filterSheet = new BottomSheetDialog(this);
        filterSheet.setContentView(R.layout.filter_sheet_dialog);

        filterSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        filterCloseBtn = filterSheet.findViewById(R.id.filter_close_btn);
        filterCancelBtn = filterSheet.findViewById(R.id.filter_cancel_btn);
        filterApplyBtn = filterSheet.findViewById(R.id.filter_apply_btn);
        filterCatRecycler = filterSheet.findViewById(R.id.filter_cat_recycler);
        genderGroup = filterSheet.findViewById(R.id.gender_radio_group);
        instaFilterCard = filterSheet.findViewById(R.id.insta_card_filter);
        youFilterCard = filterSheet.findViewById(R.id.yout_card_filter);
        twitFilterCard = filterSheet.findViewById(R.id.twitter_card_filter);

        if(userType.equals("INFLUENCER")){
            genderGroup.setVisibility(View.GONE);
            instaFilterCard.setVisibility(View.GONE);
            youFilterCard.setVisibility(View.GONE);
            twitFilterCard.setVisibility(View.GONE);
        }

        filterCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterSheet.cancel();
            }
        });

        youFilterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IS_YOUT_FILTER)
                    youFilterCard.setCardBackgroundColor(ContextCompat.getColor(SearchActivity.this,R.color.card_select_color));
                else
                    youFilterCard.setCardBackgroundColor(ContextCompat.getColor(SearchActivity.this,R.color.white));

                IS_YOUT_FILTER = !IS_YOUT_FILTER;

            }
        });

        instaFilterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IS_INSTA_FILTER)
                    instaFilterCard.setCardBackgroundColor(ContextCompat.getColor(SearchActivity.this,R.color.card_select_color));
                else
                    instaFilterCard.setCardBackgroundColor(ContextCompat.getColor(SearchActivity.this,R.color.white));

                IS_INSTA_FILTER = !IS_INSTA_FILTER;

            }
        });

        twitFilterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!IS_TWITT_FILTER)
                    twitFilterCard.setCardBackgroundColor(ContextCompat.getColor(SearchActivity.this,R.color.card_select_color));
                else
                    twitFilterCard.setCardBackgroundColor(ContextCompat.getColor(SearchActivity.this,R.color.white));

                IS_TWITT_FILTER = !IS_TWITT_FILTER;

            }
        });


        List<String> categories = new ArrayList<>();
        categories.add("Action");categories.add("Fitness");categories.add("Travel");categories.add("Film");categories.add("Creative");categories.add("Sports");
        filterCatAdapter = new CreateProfInfAbout.ProfCatSelectAdapter(categories,this);
        filterCatRecycler.setAdapter(filterCatAdapter);
        filterCatRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));


        filterApplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFilter();
            }
        });

        filterCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterCatAdapter.clearSelection();
                genderGroup.clearCheck();
                IS_TWITT_FILTER = false; IS_INSTA_FILTER=false; IS_YOUT_FILTER = false;
                instaFilterCard.setCardBackgroundColor(ContextCompat.getColor(SearchActivity.this,R.color.white));
                youFilterCard.setCardBackgroundColor(ContextCompat.getColor(SearchActivity.this,R.color.white));
                twitFilterCard.setCardBackgroundColor(ContextCompat.getColor(SearchActivity.this,R.color.white));
            }
        });
    }

    void showFilterDialog() {filterSheet.show();}

    void startFilter(){



        binding.searchSwipe.setRefreshing(true);
        if(userType.equals("BRAND")){

            String genderFilter = "";
            List<String> selectCats = filterCatAdapter.getSelecCat();
            switch (genderGroup.getCheckedRadioButtonId())
            {
                case R.id.filer_male_radio_btn: genderFilter = "Male"; break;
                case R.id.filer_female_radio_btn: genderFilter = "Female"; break;
            }

            Query infQuery = FirebaseFirestore.getInstance()
                    .collection("Influencers");

            if(!genderFilter.equals(""))
                infQuery = infQuery.whereEqualTo("gender",genderFilter);


            ArrayList<String> socials = new ArrayList<>();
            if(IS_INSTA_FILTER)
                socials.add("Instagram");

            if(IS_YOUT_FILTER)
                socials.add("Youtube");

            if(IS_TWITT_FILTER)
                socials.add("Twitter");

            infQuery.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            binding.searchSwipe.setRefreshing(false);
                            if(task.isSuccessful()){

                                infSearchList = new ArrayList<>();
                                for(DocumentSnapshot doc : task.getResult()){
                                    Influencer inf = doc.toObject(Influencer.class);

                                    if(socials.size()>0){
                                        Boolean SOCIAL = false;
                                        for (String s : inf.getSocials())
                                            if(socials.contains(s))
                                                SOCIAL = true;
                                        if(!SOCIAL) continue;
                                    }

                                    if(selectCats.size() > 0){
                                        Boolean IS_CAT = false;
                                        for (String s : selectCats)
                                            if(inf.getCategory().contains(s))
                                                IS_CAT = true;
                                        if(!IS_CAT) continue;
                                    }
                                    infSearchList.add(inf);
                                }
                                updateInfluencerRecycler();

                            } else {
                                Log.d(TAG, "onComplete: Inf Error " + task.getException());
                                Toast.makeText(SearchActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                updateEmptyUi();
                            }
                        }
                    });
        } else {
            List<String> selectCats = filterCatAdapter.getSelecCat();
            Query brnQuery = FirebaseFirestore.getInstance()
                    .collection("Brands");

            brnQuery.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            binding.searchSwipe.setRefreshing(false);
                            if(task.isSuccessful()){

                                infSearchList = new ArrayList<>();
                                for(DocumentSnapshot doc : task.getResult()){
                                    Brand inf = doc.toObject(Brand.class);
                                    if(selectCats.size() > 0){
                                        for (String s : selectCats){
                                            if(inf.getCategories().contains(s)){
                                                brandSearchList.add(inf); break;
                                            }
                                        }
                                    } else
                                    brandSearchList.add(inf);
                                }
                                updateBrandRecycler();

                            } else {
                                Log.d(TAG, "onComplete: Inf Error " + task.getException());
                                Toast.makeText(SearchActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                updateEmptyUi();
                            }
                        }
                    });
        }


        filterSheet.dismiss();
    }

    void startSorting(){

        switch (sortTypeLive.getValue()) {

            case "NEARBY":

                break;
            case "HIGH_POP":

                List<Brand> popList = brandSearchList;
                for (int i = 0; i < brandSearchList.size(); i++) {
                    for (int j = i + 1; j < brandSearchList.size(); j++) {
                        int tmp = 0;
                        if (popList.get(i).getPopular() > popList.get(j).getPopular()) {
                            tmp = popList.get(j).getPopular();
                            popList.get(i).setPopular(popList.get(j).getPopular());
                            popList.get(j).setPopular(tmp);
                        }
                    }
                }
                brandSearchList = popList;
                updateBrandRecycler();
                break;

            case "LOW_POP":

                List<Brand> popLowList = brandSearchList;
                for (int i = 0; i < brandSearchList.size(); i++) {
                    for (int j = i + 1; j < brandSearchList.size(); j++) {
                        int tmp = 0;
                        if (popLowList.get(i).getPopular() < popLowList.get(j).getPopular()) {
                            tmp = popLowList.get(j).getPopular();
                            popLowList.get(i).setPopular(popLowList.get(j).getPopular());
                            popLowList.get(j).setPopular(tmp);
                        }
                    }
                }
                brandSearchList = popLowList;
                updateBrandRecycler();
                break;
        }

    }



    @Override
    public void onBrandClick(Brand brand) {
        AppSingleton.getInstance().setSelectedBrand(brand);
        startActivity(new Intent(this, BrandActivity.class));
    }

    @Override
    public void onClickInf(Influencer influencer) {
        AppSingleton.getInstance().setSelectedInfluencer(influencer);
        startActivity(new Intent(this, InfluencerActivity.class));
    }

    Integer myPincode = 682002;
}
