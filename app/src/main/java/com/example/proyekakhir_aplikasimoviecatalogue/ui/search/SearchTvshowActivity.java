package com.example.proyekakhir_aplikasimoviecatalogue.ui.search;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyekakhir_aplikasimoviecatalogue.Data;
import com.example.proyekakhir_aplikasimoviecatalogue.R;
import com.example.proyekakhir_aplikasimoviecatalogue.adapter.CardViewAdapter;

import java.util.ArrayList;

public class SearchTvshowActivity extends AppCompatActivity {

    private final String STATE_QUERY_SEARCH_TVSHOW = "state_query_search_tvshow";

    private ArrayList<Data> listTvshowData = new ArrayList<>();
    private CardViewAdapter adapter;
    private SearchViewModel searchViewModel;

    private ImageView imgBanner;
    private SearchView searchView;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tvshow);

        imgBanner = findViewById(R.id.img_banner_search);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.search_tvshow));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.rv_search_tvshow);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CardViewAdapter(this);
        adapter.setData(listTvshowData);
        recyclerView.setAdapter(adapter);

        searchViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(SearchViewModel.class);
        searchViewModel.getSearchTvshow().observe(this, getTvshowSearched);

        if (savedInstanceState != null) {
            query = savedInstanceState.getString(STATE_QUERY_SEARCH_TVSHOW);
        }

        showBannerSearch(true);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_QUERY_SEARCH_TVSHOW, searchView.getQuery().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchMenu = menu.findItem(R.id.search);

        searchView = (SearchView) searchMenu.getActionView();
        searchView.onActionViewExpanded();
        if (query != null) {
            searchView.setQuery(query, false);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String inputText) {
                searchViewModel.setSearchTvshow(inputText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showBannerSearch(boolean state) {
        if (state) {
            imgBanner.setVisibility(View.VISIBLE);
        } else {
            imgBanner.setVisibility(View.GONE);
        }
    }

    private Observer<ArrayList<Data>> getTvshowSearched = new Observer<ArrayList<Data>>() {
        @Override
        public void onChanged(ArrayList<Data> data) {
            if (data != null) {
                listTvshowData = data;
                adapter.setData(data);
                showBannerSearch(false);
            }
        }
    };
}
