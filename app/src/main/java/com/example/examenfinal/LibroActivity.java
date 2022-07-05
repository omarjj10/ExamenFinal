package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.examenfinal.Dao.LibroDao;
import com.example.examenfinal.adaptador.LibroAdapter;
import com.example.examenfinal.dataBase.AppDatabase;
import com.example.examenfinal.entities.Libro;
import com.example.examenfinal.factories.RetrofitFactory;
import com.example.examenfinal.servicio.LibroService;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Retrofit;

public class LibroActivity extends AppCompatActivity {
    public static AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro);
        db=AppDatabase.getDatabase(getApplicationContext());
        String libroJSON=getIntent().getStringExtra("Libro");
        Libro libro = new Gson().fromJson(libroJSON,Libro.class);
        ImageView ivAvatar =findViewById(R.id.ivImagen);
        TextView tvTitulo = findViewById(R.id.tvTitulo);
        TextView tvResumen = findViewById(R.id.tvResumen);
        Picasso.get().load("https://i.ibb.co/9h3wSr8/libro.png").into(ivAvatar);
        tvTitulo.setText(libro.titulo);
        tvResumen.setText(libro.resumen);
        Button btnEditar = findViewById(R.id.btnEditar);
        Button btnDondeComprar = findViewById(R.id.btnDondeComprar);
        Button btnAgregarFavorito = findViewById(R.id.btnAgregarFavorito);
        Button btnQuitarFavorito = findViewById(R.id.btnQuitarFavorito);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditarLibroActivity.class);
                startActivity(intent);
            }
        });
        btnAgregarFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                añadirBaseDatos(libro);
                List<Libro> libros=db.libroDao().getAll();
                for(Libro libro1:libros){
                    Log.i("APP_VJ20202",libro1.getTitulo());
                    Log.i("APP_VJ20202",libro1.getResumen());
                }
            }
        });
        btnQuitarFavorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarBD(LibroAdapter.codigo);
            }
        });
    }
    private void añadirBaseDatos(Libro libro){
        LibroDao dao=db.libroDao();
        dao.create(libro);
    }
    private void eliminarBD(int d){
        LibroDao dao = db.libroDao();
        dao.eliminar(d);
    }
}