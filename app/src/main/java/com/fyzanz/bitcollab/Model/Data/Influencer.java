package com.fyzanz.bitcollab.Model.Data;

public class Influencer {

    String infId;
    String name;
    String profileUrl;


    public Influencer(String infId, String name, String profileUrl) {
        this.infId = infId;
        this.name = name;
        this.profileUrl = profileUrl;
    }

    public Influencer(String name) {
        this.name = name;
    }

    public Influencer() {
    }


    public String getInfId() {
        return infId;
    }

    public void setInfId(String infId) {
        this.infId = infId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}

