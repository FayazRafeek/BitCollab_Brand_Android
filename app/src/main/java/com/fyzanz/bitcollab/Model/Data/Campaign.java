package com.fyzanz.bitcollab.Model.Data;

import java.util.ArrayList;

public class Campaign {

    String campaignId;
    String brandId;

    String campaignName, promoteTitle, campaignDesc;
    String estBudget;

    ArrayList<String> topics;

    Boolean intsa, yout, twitt;

    String startDate, endDate;

    String bannerUrl;

    ArrayList<String> refFiles;

    String palletColor;

    String brandLogoUrl;

    String brandName;


    public Campaign() {
    }

    public Campaign(String campaignId, String brandId) {
        this.campaignId = campaignId;
        this.brandId = brandId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getPromoteTitle() {
        return promoteTitle;
    }

    public void setPromoteTitle(String promoteTitle) {
        this.promoteTitle = promoteTitle;
    }

    public String getCampaignDesc() {
        return campaignDesc;
    }

    public void setCampaignDesc(String campaignDesc) {
        this.campaignDesc = campaignDesc;
    }

    public String getEstBudget() {
        return estBudget;
    }

    public void setEstBudget(String estBudget) {
        this.estBudget = estBudget;
    }

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }

    public Boolean getIntsa() {
        return intsa;
    }

    public void setIntsa(Boolean intsa) {
        this.intsa = intsa;
    }

    public Boolean getYout() {
        return yout;
    }

    public void setYout(Boolean yout) {
        this.yout = yout;
    }

    public Boolean getTwitt() {
        return twitt;
    }

    public void setTwitt(Boolean twitt) {
        this.twitt = twitt;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public ArrayList<String> getRefFiles() {
        return refFiles;
    }

    public void setRefFiles(ArrayList<String> refFiles) {
        this.refFiles = refFiles;
    }

    public String getPalletColor() {
        return palletColor;
    }

    public void setPalletColor(String palletColor) {
        this.palletColor = palletColor;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}
