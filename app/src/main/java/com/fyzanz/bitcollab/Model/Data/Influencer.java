package com.fyzanz.bitcollab.Model.Data;

import java.util.ArrayList;

public class Influencer {

    String infId;
    String email;
    Boolean isPopular;
    String firstName, lastName;
    String profileUrl;

    ArrayList<String> category;

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
}

