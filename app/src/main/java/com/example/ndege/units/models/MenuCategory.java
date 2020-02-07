
package com.example.ndege.units.models;

import com.google.gson.annotations.SerializedName;

public class MenuCategory {

    @SerializedName("cat_name")
    private String mCatName;
    @SerializedName("id")
    private Long mId;

    public String getCatName() {
        return mCatName;
    }

    public void setCatName(String catName) {
        mCatName = catName;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    @Override
    public String toString() {
        return "MenuCategory{" +
                "mCatName='" + mCatName + '\'' +
                ", mId=" + mId +
                '}';
    }
}
