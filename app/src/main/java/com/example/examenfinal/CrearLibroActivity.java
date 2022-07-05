package com.example.examenfinal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.examenfinal.entities.ImagePost;
import com.example.examenfinal.entities.Imagen;
import com.example.examenfinal.entities.Libro;
import com.example.examenfinal.factories.RetrofitFactory;
import com.example.examenfinal.factories.RetrofitImagenFactory;
import com.example.examenfinal.servicio.ImagenService;
import com.example.examenfinal.servicio.LibroService;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CrearLibroActivity extends AppCompatActivity {
    Libro libro=new Libro();
    static final int REQUEST_IMAGE_CAPTURE = 1000;
    static final int REQUEST_CAMERA_PERMISSION = 100;
    static final int REQUEST_PICK_IMAGE=1001;
    ImageView ivPreview;
    private EditText edTitulo;
    private EditText edResumen;
    private String imageBASE64;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_libro);
        Button crear = findViewById(R.id.btnCrearLibro);
        Button btnTomarFoto = findViewById(R.id.btnTakePhoto);
        Button btnAbrirGaleria = findViewById(R.id.btnAbrirGaleria);
        ivPreview = findViewById(R.id.ivPreview);
        edTitulo=findViewById(R.id.editTitulo);
        edResumen=findViewById(R.id.editResumen);
        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //voy a solicitar si tiene permiso de camara
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    tomarFoto();
                    //si no, solicito los permisos
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                }
            }
        });
        btnAbrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirGaleria();
            }
        });
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edTitulo.getText().toString().isEmpty() && !edResumen.getText().toString().isEmpty()){
                    Log.i("APP_VJ20202","si tiene texto");
                    saveMovie();
                }else{
                    Log.i("APP_VJ20202","no tiene texto");
                    mostrarDialogo();
                }
            }
        });
    }
    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try{
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e){
            //error
        }
    }
    private void abrirGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),REQUEST_PICK_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            byte[] byteArray=byteArrayOutputStream.toByteArray();
            imageBASE64= Base64.encodeToString(byteArray,Base64.DEFAULT);
            Log.i("APP_VJ20202",imageBASE64);
            ivPreview.setImageBitmap(imageBitmap);
        }
        if(requestCode==REQUEST_PICK_IMAGE && resultCode==RESULT_OK && data != null){
            try{
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                Bitmap imageBitmap = BitmapFactory.decodeStream(bufferedInputStream);
                imageBitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                byte[] byteArray=byteArrayOutputStream.toByteArray();
                imageBASE64= Base64.encodeToString(byteArray,Base64.DEFAULT);
                Log.i("APP_VJ20202",imageBASE64);
                ivPreview.setImageBitmap(imageBitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
    }
    private void saveMovie(){
        Retrofit retrofit= RetrofitFactory.build();
        LibroService service = retrofit.create(LibroService.class);
        libro.titulo=String.valueOf(edTitulo.getText());
        libro.resumen=String.valueOf(edResumen.getText());
        ImagePost imagePost=new ImagePost();
        imagePost.image=imageBASE64;
        Retrofit retrofitImage= RetrofitImagenFactory.build(getApplicationContext());
        ImagenService imageService=retrofitImage.create(ImagenService.class);
        Call<Imagen> callImage=imageService.create(imagePost);
        callImage.enqueue(new Callback<Imagen>() {
            @Override
            public void onResponse(Call<Imagen> call, Response<Imagen> response) {
                libro.caratula=response.body().data.link;
                Call<Libro> callPelicula=service.create(libro);
                callPelicula.enqueue(new Callback<Libro>() {
                    @Override
                    public void onResponse(Call<Libro> call, Response<Libro> response) {
                        if(response.isSuccessful()){
                            Log.i("APP_VJ20202", new Gson().toJson(response.body()));
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Libro> call, Throwable t) {
                        Log.e("APP_VJ20202","No nos podemos conectar al servicio web");
                    }
                });
            }

            @Override
            public void onFailure(Call<Imagen> call, Throwable t) {

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