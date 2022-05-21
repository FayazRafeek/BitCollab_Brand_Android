package com.fyzanz.bitcollab.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.fyzanz.bitcollab.Model.Data.BasicResponse;
import com.fyzanz.bitcollab.Model.Data.Brand;
import com.fyzanz.bitcollab.Model.Data.Category;
import com.fyzanz.bitcollab.Model.Data.Influencer;
import com.fyzanz.bitcollab.Model.Repository.MainRepo;
import com.fyzanz.bitcollab.Model.Repository.UserDataRepo;

import java.util.List;

public class MainViewModel extends ViewModel {


    MainRepo mainRepo;
    UserDataRepo userDataRepo;
    public MainViewModel() {
        mainRepo = MainRepo.getInstance();
        userDataRepo = UserDataRepo.getInstance();
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

    //Fetch Popular Influencer
    public LiveData<BasicResponse> popularInfListResp;
    public MutableLiveData<List<Influencer>> popularInfListLive = new MutableLiveData<>();
    public LiveData<BasicResponse> getPopularInf(){
        popularInfListResp = mainRepo.getPopularInf();
        return popularInfListResp;
    }
    public LiveData<List<Influencer>> getPopularInfListLive() {
        return popularInfListLive;
    }
    public void setPopularInfListLive(List<Influencer> popularInfListLive) {
        this.popularInfListLive.setValue(popularInfListLive);
    }
    //

    //Fetch Popular Brands
    public MutableLiveData<List<Brand>> popularBrnListLive = new MutableLiveData<>();
    public LiveData<BasicResponse> getPopularBrand(){
        return mainRepo.getPopularBrands();
    }
    public LiveData<List<Brand>> getPopularBrnListLive() {
        return popularBrnListLive;
    }
    public void setPopularBrnListLive(List<Brand> popularInfListLive) {
        this.popularBrnListLive.setValue(popularInfListLive);
    }
    //

    //Check user profile completion
    public LiveData<BasicResponse> checkInfProfStatus(){
        return userDataRepo.checkInfProfStatus();
    }

    public LiveData<BasicResponse> checkBrnProfStatus(){
        return userDataRepo.checkBrnProfStatus();
    }
    //


    //Check inf in fav
    public Boolean checkInfInFav(String infId){
        return mainRepo.checkIsFav(infId);
    }
    public Boolean checkBrnInFav(String brandId){
        return mainRepo.checkBrnIsFav(brandId);
    }

    //Get Brand Fav List
    public List<Brand> getAllFavBrands(){
        return mainRepo.getFavBrands();
    }
    public List<Influencer> getAllFavInfluencer(){
        return mainRepo.getFavInfluencers();
    }

    //Add/Remove Brand Fav
    public void addInftoFav(Influencer influencer){
        mainRepo.addToFav(influencer);
    }
    public void removeInfFromFav(Influencer influencer){
        mainRepo.removeFromfav(influencer);
    }
    public void addBrandtoFav(Brand brand){
        mainRepo.addBrandtoFav(brand);
    }
    public void removeBrandFromFav(Brand brand){
        mainRepo.removeBrandFromfav(brand);
    }

    //



    //Category
    public LiveData<BasicResponse> fetchPopCatInf(String category){
        return mainRepo.getPopCatInfluencer(category);
    }

    public LiveData<BasicResponse> fetchNearbyCatInf(String category){
        return mainRepo.getNearbyCatInfluencer(category);
    }

    public LiveData<BasicResponse> fetchCatInfCount(String category){
        return mainRepo.getCatInfluencerCount(category);
    }

    public LiveData<BasicResponse> fetchPopCatBrn(String category){
        return mainRepo.getPopCatBrand(category);
    }

    public LiveData<BasicResponse> fetchNearbyCatBrand(String category){
        return mainRepo.getNearbyCatBrand(category);
    }

    public LiveData<BasicResponse> fetchCatBrandCount(String category){
        return mainRepo.getCatBrandCount(category);
    }
    //


    //Campaign

    public LiveData<BasicResponse> fetchRecCampaign(){
        return mainRepo.fetchRecCampaigns();
    }
    public LiveData<BasicResponse> fetchBrnCampaign(String brandId){
        return mainRepo.fetchBrnCampaigns(brandId);
    }
}
