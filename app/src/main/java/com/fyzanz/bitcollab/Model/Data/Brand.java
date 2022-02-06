package com.fyzanz.bitcollab.Model.Data;

public class Brand {

    String brandId;
    String userName, email;

    Boolean isProfileComplete = false;


    public Brand() {
    }

    public Brand(String brandId, String userName, String email, Boolean isProfileComplete) {
        this.brandId = brandId;
        this.userName = userName;
        this.email = email;
        this.isProfileComplete = isProfileComplete;
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

    public Boolean getProfileComplete() {
        return isProfileComplete;
    }

    public void setProfileComplete(Boolean profileComplete) {
        isProfileComplete = profileComplete;
    }
}
