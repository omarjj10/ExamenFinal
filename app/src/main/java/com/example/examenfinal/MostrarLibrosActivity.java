package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.examenfinal.adaptador.LibroAdapter;
import com.example.examenfinal.entities.Libro;
import com.example.examenfinal.factories.RetrofitFactory;
import com.example.examenfinal.servicio.LibroService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MostrarLibrosActivity extends AppCompatActivity {
    List<Libro> libros = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_libros);
        FloatingActionButton fabButton=findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CrearLibroActivity.class);
                startActivity(intent);
            }
        });
        Retrofit retrofit = RetrofitFactory.build();
        LibroService service = retrofit.create(LibroService.class);
        Call<List<Libro>> call = service.getAll();
        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if(!response.isSuccessful()){
                    Log.e("APP_VJ20202","Error de aplicacion");
                }else {
                    Log.i("APP_VJ20202", "Respuesta correcta");
                    Log.i("APP_VJ20202", new Gson().toJson(response.body()));
                    libros=response.body();
                    LibroAdapter adapter=new LibroAdapter(libros);
                    RecyclerView rv = findViewById(R.id.rvLibros);
                    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv.setHasFixedSize(true);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                Log.e("APP_VJ20202","No hubo conectividad con el servicio web");
            }
        });
    }
}