package com.example.examenfinal.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.examenfinal.entities.Libro;

import java.util.List;

@Dao
public interface LibroDao {
    @Query("SELECT * FROM libro")
    List<Libro> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void create(Libro libro);
    @Query("DELETE FROM libro WHERE id=:id")
    void eliminar(int id);
}
