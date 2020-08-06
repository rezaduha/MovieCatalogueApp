package com.example.proyekakhir_aplikasimoviecatalogue.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite;
import com.example.proyekakhir_aplikasimoviecatalogue.db.FavoriteDao;
import com.example.proyekakhir_aplikasimoviecatalogue.db.FavoriteDatabase;

import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.TABLE_NAME;

public class MovieCatalogueFavoriteProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.proyekakhir_aplikasimoviecatalogue.provider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
    private static final int CODE_DIR = 1;
    private static final int CODE_ITEM = 2;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, TABLE_NAME, CODE_DIR);
        MATCHER.addURI(AUTHORITY, TABLE_NAME + "/*", CODE_ITEM);
    }

    public MovieCatalogueFavoriteProvider() {
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)) {
            case CODE_DIR:
                return "vnd.android.cursor.dir/" + AUTHORITY + "." + TABLE_NAME;

            case CODE_ITEM:
                return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final int code = MATCHER.match(uri);
        if (code == CODE_DIR || code == CODE_ITEM) {
            final Context context = getContext();
            if (context == null) {
                return null;
            }
            FavoriteDao favoriteDao = FavoriteDatabase.getInstanceFavoriteDatabase(context).favoriteDao();

            final Cursor cursor;
            if (code == CODE_DIR) {
                cursor = favoriteDao.selectFavoriteByCatalogueName(selection);
            } else {
                cursor = favoriteDao.selectFavoriteByCatalogueNameAndId(selection, ContentUris.parseId(uri));
            }
            cursor.setNotificationUri(context.getContentResolver(), uri);
            return cursor;
        } else {
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (MATCHER.match(uri)) {
            case CODE_DIR:
                final Context context = getContext();
                if (context == null) {
                    return null;
                }
                if (values == null) {
                    return null;
                }

                final long id = FavoriteDatabase.getInstanceFavoriteDatabase(context).favoriteDao().insertFavoriteToProvider(Favorite.fromContentValues(values));
                context.getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);

            case CODE_ITEM:
                throw new IllegalArgumentException("Invalid URI,cannot insert with ID: " + uri);

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        switch (MATCHER.match(uri)) {
            case CODE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);

            case CODE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                if (values == null) {
                    return 0;
                }

                final Favorite favorite = Favorite.fromContentValues(values);
                favorite.setId(ContentUris.parseId(uri));
                final int count = FavoriteDatabase.getInstanceFavoriteDatabase(context).favoriteDao().update(favorite);
                context.getContentResolver().notifyChange(uri, null);
                return count;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case CODE_DIR:
                throw new IllegalArgumentException("Invalid URI, cannot update without ID" + uri);

            case CODE_ITEM:
                final Context context = getContext();
                if (context == null) {
                    return 0;
                }
                final int count = FavoriteDatabase.getInstanceFavoriteDatabase(context).favoriteDao().deleteFavoriteById(ContentUris.parseId(uri));
                context.getContentResolver().notifyChange(uri, null);
                return count;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }
}
