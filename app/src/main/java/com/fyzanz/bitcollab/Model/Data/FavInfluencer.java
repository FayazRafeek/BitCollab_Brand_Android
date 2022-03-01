package com.fyzanz.bitcollab.Model.Data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "favInfluencers")
public class FavInfluencer {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "infId")
    String infId;
    @ColumnInfo(name = "email")
    String email;
    @ColumnInfo(name = "isPopular")
    Boolean isPopular;
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
    @ColumnInfo(name = "bio")
    String bio;
    @ColumnInfo(name = "palleteColor")
    Integer palletColor;

    public FavInfluencer() {
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

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
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

    public Integer getPalletColor() {
        return palletColor;
    }

    public void setPalletColor(Integer palletColor) {
        this.palletColor = palletColor;
    }


    @Ignore
    public void setData(Influencer influencer){
        this.infId = influencer.getInfId();
        this.displayName = influencer.getDisplayName();
        this.instaId = influencer.getInstaId();
        this.category = influencer.getCategory();
        this.youtube = influencer.getYoutube();
        this.palletColor = influencer.getPalletColor();
        this.bio = influencer.getBio();
        this.profileUrl = influencer.getProfileUrl();
        this.coverImgUrl = influencer.getCoverImgUrl();
        this.email = influencer.getEmail();
    }
}
