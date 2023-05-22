package com.sanapplications.mygarage.API;

import com.sanapplications.mygarage.Model.MakeResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MakeApi {
    @GET("/api/vehicles/getallmakes?format=json")
    Call<MakeResponse> getMakes();

}