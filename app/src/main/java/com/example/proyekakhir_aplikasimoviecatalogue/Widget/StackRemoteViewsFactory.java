package com.example.proyekakhir_aplikasimoviecatalogue.Widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.room.Room;

import com.bumptech.glide.Glide;
import com.example.proyekakhir_aplikasimoviecatalogue.R;
import com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite;
import com.example.proyekakhir_aplikasimoviecatalogue.db.FavoriteDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.DATABASE_NAME;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;

    StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems.clear();
        FavoriteDatabase database = Room.databaseBuilder(mContext, FavoriteDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        List<Favorite> mFavoriteList = database.favoriteDao().findByCatalogue("movie");

        for (Favorite favorite : mFavoriteList) {
            try {
                mWidgetItems.add(Glide.with(mContext.getApplicationContext())
                        .asBitmap()
                        .load("https://image.tmdb.org/t/p/w500/" + favorite.getPhoto())
                        .submit()
                        .get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        database.close();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
        rv.setImageViewBitmap(R.id.imageWidget, mWidgetItems.get(position));

        Bundle extras = new Bundle();
        extras.putInt(MovieFavoriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.imageWidget, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
