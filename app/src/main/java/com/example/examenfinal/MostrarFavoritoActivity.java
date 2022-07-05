package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.examenfinal.adaptador.FavoritoAdapter;
import com.example.examenfinal.dataBase.AppDatabase;
import com.example.examenfinal.entities.Libro;
import com.example.examenfinal.factories.RetrofitFactory;
import com.example.examenfinal.servicio.LibroService;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MostrarFavoritoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_favorito);
        Retrofit retrofit= RetrofitFactory.build();
        LibroService service=retrofit.create(LibroService.class);
        service.getAll().enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if(!response.isSuccessful())
                    return;
                Log.i("APP_VJ20202",new Gson().toJson(response.body()));
                List<Libro>libros=LibroActivity.db.libroDao().getAll();
                FavoritoAdapter adapter = new FavoritoAdapter(libros);
                RecyclerView rv = findViewById(R.id.rvFavorito);
                rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rv.setHasFixedSize(true);
                rv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                Log.e("APP_VJ20202","No hubo conectividad con el servicio web");
            }
        });
    }
}