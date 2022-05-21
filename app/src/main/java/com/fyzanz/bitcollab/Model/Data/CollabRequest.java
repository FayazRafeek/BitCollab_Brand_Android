package com.fyzanz.bitcollab.Model.Data;

public class CollabRequest {

    String reqId;
    String type;

    String brandId;
    String brandName;
    String brandLogoUrl;
    String brandCat;
    String brandLoc;

    String infId;
    String infName;
    String infLogoUrl;
    String infLocation, infCat;

    String reqTitle, campaignName, reqBody;

    String ts;

    String status;

    public CollabRequest() {
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public String getInfId() {
        return infId;
    }

    public void setInfId(String infId) {
        this.infId = infId;
    }

    public String getInfName() {
        return infName;
    }

    public void setInfName(String infName) {
        this.infName = infName;
    }

    public String getInfLogoUrl() {
        return infLogoUrl;
    }

    public void setInfLogoUrl(String infLogoUrl) {
        this.infLogoUrl = infLogoUrl;
    }

    public String getReqTitle() {
        return reqTitle;
    }

    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getReqBody() {
        return reqBody;
    }

    public void setReqBody(String reqBody) {
        this.reqBody = reqBody;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getBrandLoc() {
        return brandLoc;
    }

    public void setBrandLoc(String brandLoc) {
        this.brandLoc = brandLoc;
    }


    public String getBrandCat() {
        return brandCat;
    }

    public void setBrandCat(String brandCat) {
        this.brandCat = brandCat;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfLocation() {
        return infLocation;
    }

    public void setInfLocation(String infLocation) {
        this.infLocation = infLocation;
    }

    public String getInfCat() {
        return infCat;
    }

    public void setInfCat(String infCat) {
        this.infCat = infCat;
    }
}
