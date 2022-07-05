package com.example.examenfinal.factories;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitImagenFactory {
    public static Retrofit build(Context context){
        return new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    private static OkHttpClient getClient(){
        OkHttpClient httpClient=new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder=chain.request().newBuilder();
                requestBuilder.header("Content-type","application/json");
                requestBuilder.header("Authorization","Client-ID 8bcc638875f89d9");
                return chain.proceed(requestBuilder.build());
            }
        }).build();
        return httpClient;
    }
}
