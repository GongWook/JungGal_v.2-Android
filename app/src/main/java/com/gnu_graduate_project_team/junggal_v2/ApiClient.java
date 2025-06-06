package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://203.255.3.238:3000";

    public static Retrofit getApiClient()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

}
