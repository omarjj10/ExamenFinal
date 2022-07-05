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

public class FavoritoAdapter extends RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder>{
    List<Libro> libros;
    public FavoritoAdapter(List<Libro> libros){
        this.libros=libros;
    }
    @NonNull
    @Override
    public FavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorito,parent,false);
        return new FavoritoViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull FavoritoViewHolder vh, int position) {
        View itemView = vh.itemView;
        Libro libro=libros.get(position);
        TextView tvTitulo = itemView.findViewById(R.id.tvTituloFavorito);
        TextView tvResumen=itemView.findViewById(R.id.tvResumenFavorito);
        tvTitulo.setText(libro.titulo);
        tvResumen.setText(libro.resumen);
    }
    @Override
    public int getItemCount() {
        return libros.size();
    }

    class FavoritoViewHolder extends RecyclerView.ViewHolder{

        public FavoritoViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
