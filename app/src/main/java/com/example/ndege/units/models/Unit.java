package com.example.ndege.units.models;

import java.io.Serializable;

public class Unit implements Serializable {
    private String unit_name;
    private String image;
    private String closing_time;
    private double location_latitude;
    private String opening_time;
    private String cooking_time;
    private int id;
    private double location_longitude;
    private String phone;
    private int likes;
    private String location_town;
    private Country country;

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getLocation_town() {
        return location_town;
    }

    public void setLocation_town(String location_town) {
        this.location_town = location_town;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUnit_name() {
        return this.unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getClosing_time() {
        return this.closing_time;
    }

    public void setClosing_time(String closing_time) {
        this.closing_time = closing_time;
    }

    public double getLocation_latitude() {
        return this.location_latitude;
    }

    public void setLocation_latitude(double location_latitude) {
        this.location_latitude = location_latitude;
    }

    public String getOpening_time() {
        return this.opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
    }

    public String getCooking_time() {
        return this.cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLocation_longitude() {
        return this.location_longitude;
    }

    public void setLocation_longitude(double location_longitude) {
        this.location_longitude = location_longitude;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "unit_name='" + unit_name + '\'' +
                ", image='" + image + '\'' +
                ", closing_time='" + closing_time + '\'' +
                ", location_latitude=" + location_latitude +
                ", opening_time='" + opening_time + '\'' +
                ", cooking_time='" + cooking_time + '\'' +
                ", id=" + id +
                ", location_longitude=" + location_longitude +
                '}';
    }
}
