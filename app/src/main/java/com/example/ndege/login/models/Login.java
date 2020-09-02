package com.example.ndege.login.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("is_ndege_reseller")
    @Expose
    private boolean is_ndege_reseller;

    public boolean isIs_ndege_reseller() {
        return is_ndege_reseller;
    }

    public void setIs_ndege_reseller(boolean is_ndege_reseller) {
        this.is_ndege_reseller = is_ndege_reseller;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
