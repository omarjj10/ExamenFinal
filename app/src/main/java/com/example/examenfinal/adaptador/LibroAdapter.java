package com.example.examenfinal.adaptador;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.examenfinal.LibroActivity;
import com.example.examenfinal.R;
import com.example.examenfinal.entities.Libro;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder>{
    public static int codigo;
    List<Libro> libros;
    public LibroAdapter(List<Libro> libros){
        this.libros=libros;
    }
    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro,parent,false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder vh, int position) {
        View itemView = vh.itemView;
        Libro libro=libros.get(position);
        TextView tvTitulo = itemView.findViewById(R.id.tvTitulo);
        ImageView ivAvatar = itemView.findViewById(R.id.ivImagen);
        tvTitulo.setText(libro.titulo);
        Picasso.get().load(libro.caratula).into(ivAvatar);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(itemView.getContext(), LibroActivity.class);
                String libroJSON=new Gson().toJson(libro);
                intent.putExtra("Libro",libroJSON);
                codigo=libro.id;
                itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    class LibroViewHolder extends RecyclerView.ViewHolder{

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
