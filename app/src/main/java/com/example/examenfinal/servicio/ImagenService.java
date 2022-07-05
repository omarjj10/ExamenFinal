package com.example.examenfinal.servicio;

import com.example.examenfinal.entities.ImagePost;
import com.example.examenfinal.entities.Imagen;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImagenService {
    @POST("image")
    Call<Imagen>create(@Body ImagePost body);
}
