package com.example.ndege.units.menucategories.models;

public class MenuCategory {
    private String image;
    private String cat_name;
    private int main_category;
    private int id;

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCat_name() {
        return this.cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public int getMain_category() {
        return this.main_category;
    }

    public void setMain_category(int main_category) {
        this.main_category = main_category;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return cat_name;
    }
}
