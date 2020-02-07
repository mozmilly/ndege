package com.example.ndege.units.models;

public class DashTiles implements java.io.Serializable {
    private static final long serialVersionUID = 3413804593040713294L;
    private String name;
    private boolean state;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
