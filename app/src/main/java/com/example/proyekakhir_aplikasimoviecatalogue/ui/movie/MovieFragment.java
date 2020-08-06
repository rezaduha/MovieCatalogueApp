package com.example.proyekakhir_aplikasimoviecatalogue.ui.movie;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyekakhir_aplikasimoviecatalogue.Data;
import com.example.proyekakhir_aplikasimoviecatalogue.R;
import com.example.proyekakhir_aplikasimoviecatalogue.adapter.CardViewAdapter;
import com.example.proyekakhir_aplikasimoviecatalogue.ui.search.SearchMovieActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment {

    private ProgressBar progressBar;
    private FloatingActionButton fabSearch;

    private CardViewAdapter adapter;
    private MovieViewModel movieViewModel;

    public MovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);
        fabSearch = view.findViewById(R.id.fab_action_search);

        RecyclerView recyclerView = view.findViewById(R.id.rv_movie);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CardViewAdapter(this.getContext());
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        movieViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(MovieViewModel.class);
        movieViewModel.setMovie();
        movieViewModel.getMovie().observe(getViewLifecycleOwner(), new Observer<ArrayList<Data>>() {
            @Override
            public void onChanged(ArrayList<Data> data) {
                if (data != null) {
                    adapter.setData(data);
                    showLoading(false);
                }
            }
        });
        showLoading(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabSearch.getVisibility() == View.VISIBLE) {
                    fabSearch.hide();
                } else if (dy < 0 && fabSearch.getVisibility() != View.VISIBLE) {
                    fabSearch.show();
                }
            }
        });

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchMovieActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showLoading(boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
