package com.example.moviecataloguefavorite.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviecataloguefavorite.R;
import com.example.moviecataloguefavorite.adapter.CardViewAdapter;
import com.example.moviecataloguefavorite.db.Favorite;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.moviecataloguefavorite.db.Favorite.CONTENT_URI;
import static com.example.moviecataloguefavorite.helper.MappingHelper.mapCursortoArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int LOADER_FAVORITE = 1;
    private String MovieCataloguePackageName = "com.example.proyekakhir_aplikasimoviecatalogue";

    RecyclerView rvMovieCatalogueFavorite;
    private CardViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FloatingActionButton fabMovieCatalogue = findViewById(R.id.fab_movie_catalogue);
        rvMovieCatalogueFavorite = findViewById(R.id.rv_movie_catalogue_favorite);

        rvMovieCatalogueFavorite.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        adapter = new CardViewAdapter(this.getApplicationContext());
        rvMovieCatalogueFavorite.setAdapter(adapter);

        rvMovieCatalogueFavorite.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabMovieCatalogue.getVisibility() == View.VISIBLE) {
                    fabMovieCatalogue.hide();
                } else if (dy < 0 && fabMovieCatalogue.getVisibility() != View.VISIBLE) {
                    fabMovieCatalogue.show();
                }
            }
        });

        fabMovieCatalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage(MovieCataloguePackageName);
                startActivity(intent);
            }
        });

        LoaderManager.getInstance(this).initLoader(LOADER_FAVORITE, null, loaderCallbacks);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            if (id == LOADER_FAVORITE) {
                if (getApplicationContext() != null) {
                    return new CursorLoader(getApplicationContext(),
                            CONTENT_URI,
                            null,
                            "movie",
                            null,
                            null);
                } else {
                    throw new IllegalArgumentException();
                }
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
            if (loader.getId() == LOADER_FAVORITE) {
                ArrayList<Favorite> favorites = mapCursortoArrayList(cursor);
                if (favorites.size() > 0) {
                    adapter.setListFavorite(favorites);
                } else {
                    adapter.setListFavorite(new ArrayList<Favorite>());
                }
                rvMovieCatalogueFavorite.setAdapter(adapter);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            if (loader.getId() == LOADER_FAVORITE) {
                adapter.setListFavorite(null);
            }
        }
    };

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LoaderManager.getInstance(this).restartLoader(LOADER_FAVORITE, null, loaderCallbacks);
    }

    public boolean doubleTapParam = false;

    @Override
    public void onBackPressed() {
        if (doubleTapParam) {
            super.onBackPressed();
            return;
        }

        this.doubleTapParam = true;
        Toast.makeText(this, getString(R.string.double_tap_alert), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleTapParam = false;
            }
        }, 2000);
    }
}
