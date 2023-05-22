package com.sanapplications.mygarage.API;

import com.sanapplications.mygarage.Model.ModelResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ModelApi {
    //        @GET("api/vehicles/GetModelsForMakeId/10005?format=json")
//        Call<ModelResponse> getModels();
    @GET("api/vehicles/GetModelsForMakeId/{id}?format=json")
    Call<ModelResponse> getModels(@Path("id") String id);
}