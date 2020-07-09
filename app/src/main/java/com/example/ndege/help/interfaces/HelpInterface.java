package com.example.ndege.help.interfaces;

import com.example.ndege.help.models.Help;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface HelpInterface {

    @GET("units/get_all_help/")
    Call<List<Help>> get_all_help();
}
