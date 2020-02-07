package com.example.ndege.units.models;

import java.io.Serializable;

public class MyCart implements Serializable{
    private MenuItems menuItems;

    private int quantity;

    public MyCart() {
    }

    public MyCart(MenuItems menuItems, int quantity) {
        this.menuItems = menuItems;
        this.quantity = quantity;
    }

    public MenuItems getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(MenuItems menuItems) {
        this.menuItems = menuItems;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "MyCart{" +
                "menuItems=" + menuItems +
                ", quantity=" + quantity +
                '}';
    }
}
