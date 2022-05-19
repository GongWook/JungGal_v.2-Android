package com.gnu_graduate_project_team.junggal_v2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ReverseGeocodingInterface {

    @GET("v2/gc")
    Call<ReverseGeocoding> getGeo(
            @Header("X-NCP-APIGW-API-KEY-ID") String apiKeyID,
            @Header("X-NCP-APIGW-API-KEY") String apiKey,
            @Query("coords") String coords,
            @Query("sourcecrs") String sourcecrs,
            @Query("orders") String orders,
            @Query("output") String output
    );
}
