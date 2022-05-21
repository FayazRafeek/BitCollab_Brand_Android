package com.fyzanz.bitcollab.Model.Data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "favInfluencers")
public class Influencer {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "infId")
    String infId;
    @ColumnInfo(name = "email")
    String email;
    @ColumnInfo(name = "gender")
    String gender;
    @ColumnInfo(name = "profileUrl")
    String profileUrl;
    @ColumnInfo(name = "coverImgUrl")
    String coverImgUrl;
    @ColumnInfo(name = "category")
    ArrayList<String> category;
    @ColumnInfo(name = "displayName")
    String displayName;
    @ColumnInfo(name = "instaId")
    String instaId;
    @ColumnInfo(name = "youtube")
    String youtube;
    String twitter;
    @ColumnInfo(name = "bio")
    String bio;
    @ColumnInfo(name = "palleteColor")
    String palletColor;
    @ColumnInfo(name = "dob")
    String dob;
    @ColumnInfo(name = "phone")
    String phone;
    @ColumnInfo(name = "address")
    String address;
    @ColumnInfo(name = "country")
    String country;
    @ColumnInfo(name = "state")
    String state;
    @ColumnInfo(name = "pincode")
    Integer pincode;
    @ColumnInfo(name = "popular")
    Integer popular;
    @ColumnInfo(name = "socials")
    ArrayList<String> socials;


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

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
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

    public String getPalletColor() {
        return palletColor;
    }

    public void setPalletColor(String palletColor) {
        this.palletColor = palletColor;
    }

    public Integer getPopular() {
        return popular;
    }

    public void setPopular(Integer popular) {
        this.popular = popular;
    }

    public ArrayList<String> getSocials() {
        return socials;
    }

    public void setSocials(ArrayList<String> socials) {
        this.socials = socials;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
}

