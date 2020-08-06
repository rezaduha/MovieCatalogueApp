package com.example.proyekakhir_aplikasimoviecatalogue.helper;

import android.database.Cursor;

import com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite;

import java.util.ArrayList;

import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_CATALOGUE;
import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_DESCRIPTION;
import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_ID;
import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_NAME;
import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_PHOTO;

public class MappingHelper {

    public static ArrayList<Favorite> mapCursortoArrayList(Cursor cursor) {
        ArrayList<Favorite> favoriteArrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String catalogue = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATALOGUE));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
            String photo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO));
            favoriteArrayList.add(new Favorite(id, catalogue, name, description, photo));
        }
        return favoriteArrayList;
    }
}
