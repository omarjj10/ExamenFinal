package com.example.examenfinal.factories;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    public static Retrofit build(){
        return new Retrofit.Builder()
                .baseUrl("https://62c416397d83a75e39efdeb8.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
