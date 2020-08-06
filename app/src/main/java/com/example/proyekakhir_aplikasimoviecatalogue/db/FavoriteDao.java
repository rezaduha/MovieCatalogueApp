package com.example.proyekakhir_aplikasimoviecatalogue.db;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_CATALOGUE;
import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_ID;
import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.TABLE_NAME;

@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM " + TABLE_NAME)
    List<Favorite> selectAll();

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CATALOGUE + " LIKE :catalogue")
    Cursor selectFavoriteByCatalogueName(String catalogue);

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CATALOGUE + " LIKE :catalogue AND " + COLUMN_ID + " LIKE :Id")
    Cursor selectFavoriteByCatalogueNameAndId(String catalogue, long Id);

    @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CATALOGUE + " LIKE :catalogue")
    List<Favorite> findByCatalogue(String catalogue);

    @Insert
    long insertFavoriteToProvider(Favorite favorites);

    @Query("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = :id")
    int deleteFavoriteById(Long id);

    @Update
    int update(Favorite favorite);

    @Insert
    void insertFavorite(Favorite... favorites);

    @Delete
    void deleteFavorite(Favorite... favorites);
}
