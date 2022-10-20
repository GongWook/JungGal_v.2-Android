package com.gnu_graduate_project_team.junggal_v2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface NaverDirection5Interface {

    @GET("v1/driving")
    Call<NaverDirection5ResultPath> getPath(
            @Header("X-NCP-APIGW-API-KEY-ID") String apiKeyID,
            @Header("X-NCP-APIGW-API-KEY") String apiKey,
            @Query("start") String start,
            @Query("goal") String goal,
            @Query("option") String option
    );
}
