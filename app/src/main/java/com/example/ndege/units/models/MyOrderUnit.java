package com.example.ndege.units.models;

public class MyOrderUnit implements java.io.Serializable {
    private static final long serialVersionUID = 781492315472422384L;
    private String unit_name;
    private String closing_time;
    private double location_latitude;
    private String opening_time;
    private String profile_pic;
    private String cooking_time;
    private int id;
    private double location_longitude;

    public String getUnit_name() {
        return this.unit_name;
    }

    public void setUnit_name(String unit_name) {
        this.unit_name = unit_name;
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

    public String getProfile_pic() {
        return this.profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
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
}
