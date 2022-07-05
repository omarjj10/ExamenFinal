package com.example.examenfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.examenfinal.adaptador.LibroAdapter;
import com.example.examenfinal.entities.Libro;
import com.example.examenfinal.factories.RetrofitFactory;
import com.example.examenfinal.servicio.LibroService;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditarLibroActivity extends AppCompatActivity {
    Libro libro = new Libro();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_libro);
        Button btnEditar = findViewById(R.id.btnEditarLibro);
        EditText edTitulo = findViewById(R.id.editNuevoTitulo);
        EditText edResumen = findViewById(R.id.editNuevoResumen);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edTitulo.getText().toString().isEmpty() && !edResumen.getText().toString().isEmpty()){
                    Log.i("APP_VJ20202","si tiene texto");
                    Retrofit retrofit = RetrofitFactory.build();
                    LibroService service = retrofit.create(LibroService.class);
                    libro.titulo=String.valueOf(edTitulo.getText());
                    libro.resumen=String.valueOf(edResumen.getText());
                    Call<Libro> call = service.update(LibroAdapter.codigo,libro);
                    call.enqueue(new Callback<Libro>() {
                        @Override
                        public void onResponse(Call<Libro> call, Response<Libro> response) {
                            Log.i("APP_VJ20202", new Gson().toJson(response.body()));
                        }

                        @Override
                        public void onFailure(Call<Libro> call, Throwable t) {
                            Log.i("APP_VJ20202","No nos podemos conectar al servicio web");
                        }
                    });
                }else{
                    Log.i("APP_VJ20202","no tiene texto");
                    mostrarDialogo();
                }
            }
        });
    }
    private void mostrarDialogo(){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Tienes que llenar todos los campos de texto")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i("APP_VJ20202","Se cancelo la accion");
                    }
                })
                .show();
    }
}