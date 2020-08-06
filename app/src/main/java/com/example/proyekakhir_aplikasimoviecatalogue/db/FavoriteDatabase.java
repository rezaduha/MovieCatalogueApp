package com.example.proyekakhir_aplikasimoviecatalogue.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.DATABASE_NAME;

@Database(entities = {Favorite.class}, version = 1, exportSchema = false)
public abstract class FavoriteDatabase extends RoomDatabase {

    private static FavoriteDatabase instanceFavoriteDatabase;

    public abstract FavoriteDao favoriteDao();

    public static FavoriteDatabase getInstanceFavoriteDatabase(Context context) {
        if (instanceFavoriteDatabase == null) {
            instanceFavoriteDatabase = Room.databaseBuilder(context.getApplicationContext(), FavoriteDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return instanceFavoriteDatabase;
    }
}
