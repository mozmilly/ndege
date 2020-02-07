package com.example.ndege.units.models;

public class MenuItems {
    private String image;
    private MenuCategory menu_category;
    private double price;
    private String description;
    private String item_name;
    private int id;
    private boolean is_negotiable;

    private int items_in_stock;
    private int was_price;

    private int minimum_order;


    public int getMinimum_order() {
        return minimum_order;
    }

    public void setMinimum_order(int minimum_order) {
        this.minimum_order = minimum_order;
    }

    public int getWas_price() {
        return was_price;
    }

    public void setWas_price(int was_price) {
        this.was_price = was_price;
    }

    public int getItems_in_stock() {
        return items_in_stock;
    }

    public void setItems_in_stock(int items_in_stock) {
        this.items_in_stock = items_in_stock;
    }

    public boolean isIs_negotiable() {
        return is_negotiable;
    }

    public void setIs_negotiable(boolean is_negotiable) {
        this.is_negotiable = is_negotiable;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public MenuCategory getMenu_category() {
        return menu_category;
    }

    public void setMenu_category(MenuCategory menu_category) {
        this.menu_category = menu_category;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItem_name() {
        return this.item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
