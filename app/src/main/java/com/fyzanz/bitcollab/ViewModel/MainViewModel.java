package com.fyzanz.bitcollab.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Category;
import com.fyzanz.bitcollab.Model.Repository.MainRepo;

public class MainViewModel extends ViewModel {


    MainRepo mainRepo;
    public MainViewModel() {
        mainRepo = MainRepo.getInstance();
    }

    //Screen Change handle
    Category selectedCategory;
    MutableLiveData<String> screenLive;
    public MutableLiveData<String> getCurrentScreen() {
        if(screenLive == null) screenLive = new MutableLiveData<>("HOME");
        return screenLive;
    }
    public void setSelectedCategory(Category category) { this.selectedCategory = category;}
    public Category getSelectedCategory() { return selectedCategory; }

    public LiveData<BasicResponse> categoryListResp;
    public LiveData<BasicResponse> getInfCategory(){
        categoryListResp = mainRepo.getInfCategories();
        return categoryListResp;
    }

    public LiveData<BasicResponse> popularInfListResp;
    public LiveData<BasicResponse> getPopularInf(){
        popularInfListResp = mainRepo.getPopularInf();
        return popularInfListResp;
    }

}
