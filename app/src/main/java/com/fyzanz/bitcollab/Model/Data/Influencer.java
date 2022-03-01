package com.fyzanz.bitcollab.Model.Data;

import java.util.ArrayList;

public class Influencer {

    String infId;
    String email;
    Boolean isPopular;
    String firstName, lastName;
    String profileUrl, coverImgUrl;

    ArrayList<String> category;

    String displayName;
    String dob;
    String phone, address, country, state, pincode;

    String instaId, youtube;
    String bio;

    Integer palletColor;


    public Influencer(String infId, String email, Boolean isPopular, String firstName, String lastName, String profileUrl, ArrayList<String> category) {
        this.infId = infId;
        this.email = email;
        this.isPopular = isPopular;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileUrl = profileUrl;
        this.category = category;
    }

    public Influencer(String infId, Boolean isPopular, String firstName) {
        this.infId = infId;
        this.isPopular = isPopular;
        this.firstName = firstName;
    }

    public Influencer(String infId, String firstName, String lastName) {
        this.infId = infId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Influencer() {
    }

    public String getInfId() {
        return infId;
    }

    public void setInfId(String infId) {
        this.infId = infId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getPopular() {
        return isPopular;
    }

    public void setPopular(Boolean popular) {
        isPopular = popular;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public ArrayList<String> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }


    public String getInstaId() {
        return instaId;
    }

    public void setInstaId(String instaId) {
        this.instaId = instaId;
    }

    public String getYoutube() {
        return youtube;
    }

    public void setYoutube(String youtube) {
        this.youtube = youtube;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public Integer getPalletColor() {
        return palletColor;
    }

    public void setPalletColor(Integer palletColor) {
        this.palletColor = palletColor;
    }


    public void setDataFromFav(FavInfluencer fav){
        this.infId = fav.getInfId();
        this.displayName = fav.getDisplayName();
        this.email = fav.getEmail();
        this.profileUrl = fav.getProfileUrl();
        this.coverImgUrl = fav.getCoverImgUrl();
        this.bio = fav.getBio();
        this.category = fav.getCategory();
        this.instaId = fav.getInstaId();
        this.youtube = fav.getYoutube();
    }
}

