package com.example.proyekakhir_aplikasimoviecatalogue.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyekakhir_aplikasimoviecatalogue.Data;
import com.example.proyekakhir_aplikasimoviecatalogue.R;
import com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_CATALOGUE;
import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_DESCRIPTION;
import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_ID;
import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_NAME;
import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.COLUMN_PHOTO;
import static com.example.proyekakhir_aplikasimoviecatalogue.helper.MappingHelper.mapCursortoArrayList;
import static com.example.proyekakhir_aplikasimoviecatalogue.provider.MovieCatalogueFavoriteProvider.CONTENT_URI;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DATA = "extra_data";
    public static final String EXTRA_DATA_FAVORITE = "extra_data_favorite";

    private Data data;
    private Favorite favorite;
    private ArrayList<Favorite> favoriteList;

    private TextView txtDescription;
    private RoundedImageView image;
    private FloatingActionButton fabFavorite;

    private String title, dataName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        txtDescription = findViewById(R.id.txt_description);
        image = findViewById(R.id.img_photo);
        fabFavorite = findViewById(R.id.fab_action_favorite);

        if (getIntent().getParcelableExtra(EXTRA_DATA) != null) {
            data = getIntent().getParcelableExtra(EXTRA_DATA);

            assert data != null;
            if (data.getMovieName() != null) {
                title = data.getMovieName();
                dataName = "movie";
            } else if (data.getTvshowName() != null) {
                title = data.getTvshowName();
                dataName = "tvshow";
            }
            if (getSupportActionBar() != null) {
                if (data.getMovieName() != null) {
                    getSupportActionBar().setTitle(title);
                }
                if (data.getTvshowName() != null) {
                    getSupportActionBar().setTitle(title);
                }
            }
            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500/" + data.getPhoto())
                    .placeholder(R.drawable.ic_burst_image)
                    .error(R.drawable.ic_broken_image)
                    .into(image);

            txtDescription.setText(data.getDescription());

            Cursor cursor = getContentResolver().query(CONTENT_URI, null, dataName, null, null);
            assert cursor != null;
            favoriteList = mapCursortoArrayList(cursor);
            onClickFavoriteDetail();
        } else if (getIntent().getParcelableExtra(EXTRA_DATA_FAVORITE) != null) {
            favorite = getIntent().getParcelableExtra(EXTRA_DATA_FAVORITE);

            getSupportActionBar().setTitle(favorite.getName());
            Glide.with(getApplicationContext())
                    .load("https://image.tmdb.org/t/p/w500/" + favorite.getPhoto())
                    .into(image);
            txtDescription.setText(favorite.getDescription());

            Cursor cursor = getContentResolver().query(CONTENT_URI, null, favorite.getCatalogue(), null, null);
            assert cursor != null;
            favoriteList = mapCursortoArrayList(cursor);
            onClickFavoriteFavorite();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void onClickFavoriteDetail() {
        boolean btnCondition = false;
        for (Favorite dataFavorite : favoriteList) {
            if (dataFavorite.getId() == data.getId()) {
                btnCondition = true;
                fabFavorite.setImageResource(R.drawable.ic_favorite_true);
                fabFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + data.getId()), null, null);
                        Toast.makeText(DetailActivity.this, getString(R.string.remove), Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                });
            }
        }
        if (!btnCondition) {
            fabFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertToFavorite(data.getId(),
                            dataName,
                            title,
                            data.getDescription(),
                            data.getPhoto());
                    Toast.makeText(DetailActivity.this, getString(R.string.add), Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
            });
        }
    }

    private void onClickFavoriteFavorite() {
        boolean btnCondition = false;
        for (Favorite dataFavorite : favoriteList) {
            if (dataFavorite.getId() == favorite.getId()) {
                btnCondition = true;
                fabFavorite.setImageResource(R.drawable.ic_favorite_true);
                fabFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContentResolver().delete(Uri.parse(CONTENT_URI + "/" + favorite.getId()), null, null);
                        Toast.makeText(DetailActivity.this, getString(R.string.remove), Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                });
            }
        }
        if (!btnCondition) {
            fabFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    insertToFavorite(favorite.getId(),
                            favorite.getCatalogue(),
                            favorite.getName(),
                            favorite.getDescription(),
                            favorite.getPhoto());
                    Toast.makeText(DetailActivity.this, getString(R.string.add), Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }
            });
        }
    }

    private void insertToFavorite(Long id, String dataName, String name, String description, String photo) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_CATALOGUE, dataName);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PHOTO, photo);
        getContentResolver().insert(CONTENT_URI, values);
    }
}
