package com.fyzanz.bitcollab.Model.Data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "favBrands")
public class Brand {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "brandId")
    String brandId;
    @ColumnInfo(name = "userName")
    String userName;
    @ColumnInfo(name = "email")
    String email;
    @ColumnInfo(name = "brandName")
    String brandName;
    @ColumnInfo(name = "tagline")
    String tagline;
    @ColumnInfo(name = "logoImgUrl")
    String logoImgUrl;
    @ColumnInfo(name = "coverImgUrl")
    String coverImgUrl;
    @ColumnInfo(name = "address")
    String address;
    @ColumnInfo(name = "country")
    String country;
    @ColumnInfo(name = "state")
    String state;
    @ColumnInfo(name = "pincode")
    String pincode;
    @ColumnInfo(name = "phone")
    String phone;
    @ColumnInfo(name = "bio")
    String bio;
    @ColumnInfo(name = "websiteUrl")
    String websiteUrl;
    @ColumnInfo(name = "categories")
    ArrayList<String> categories;
    @ColumnInfo(name = "palleteColor")
    Integer palleteColor;

    public Brand() {
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getLogoImgUrl() {
        return logoImgUrl;
    }

    public void setLogoImgUrl(String logoImgUrl) {
        this.logoImgUrl = logoImgUrl;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public Integer getPalleteColor() {
        return palleteColor;
    }

    public void setPalleteColor(Integer palleteColor) {
        this.palleteColor = palleteColor;
    }
}
