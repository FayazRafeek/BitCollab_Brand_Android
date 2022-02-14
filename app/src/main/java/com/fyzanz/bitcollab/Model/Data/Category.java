package com.fyzanz.bitcollab.Model.Data;

public class Category {

    String catId;
    String name;

    public Category(String catId, String name) {
        this.catId = catId;
        this.name = name;
    }

    public Category() {
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
