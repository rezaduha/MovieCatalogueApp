package com.example.proyekakhir_aplikasimoviecatalogue.ui.favorite;


import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyekakhir_aplikasimoviecatalogue.R;
import com.example.proyekakhir_aplikasimoviecatalogue.adapter.CardViewAdapter;
import com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite;
import com.example.proyekakhir_aplikasimoviecatalogue.provider.MovieCatalogueFavoriteProvider;

import java.util.ArrayList;

import static com.example.proyekakhir_aplikasimoviecatalogue.helper.MappingHelper.mapCursortoArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvshowFavoriteFragment extends Fragment {

    private static final int LOADER_FAVORITE = 1;

    RecyclerView rvFavoriteTvshow;
    private CardViewAdapter adapter;

    public TvshowFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFavoriteTvshow = view.findViewById(R.id.rv_tvshow_favorite);

        rvFavoriteTvshow.setHasFixedSize(true);
        rvFavoriteTvshow.setLayoutManager(new LinearLayoutManager(this.getContext()));
        adapter = new CardViewAdapter(this.getContext());
        LoaderManager.getInstance(this).initLoader(LOADER_FAVORITE, null, loaderCallbacks);
    }

    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            if (id == LOADER_FAVORITE) {
                if (getContext() != null) {
                    return new CursorLoader(getContext(),
                            MovieCatalogueFavoriteProvider.CONTENT_URI,
                            null,
                            "tvshow",
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
                ArrayList<Favorite> listFavorite = mapCursortoArrayList(cursor);
                if (listFavorite.size() > 0) {
                    adapter.setListFavorite(listFavorite);
                } else {
                    adapter.setListFavorite(new ArrayList<Favorite>());
                }
            }
            rvFavoriteTvshow.setAdapter(adapter);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            if (loader.getId() == LOADER_FAVORITE) {
                adapter.setListFavorite(null);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LoaderManager.getInstance(this).restartLoader(LOADER_FAVORITE, null, loaderCallbacks);
    }
}
