package com.example.proyekakhir_aplikasimoviecatalogue.ui.movie;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyekakhir_aplikasimoviecatalogue.BuildConfig;
import com.example.proyekakhir_aplikasimoviecatalogue.Data;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieViewModel extends ViewModel {

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<Data>> listMovie = new MutableLiveData<>();

    public void setMovie() {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Data> listItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY + "&language=en-US";

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        Data data = new Data();

                        data.setId(movie.getLong("id"));
                        data.setMovieName(movie.getString("title"));
                        data.setDescription(movie.getString("overview"));
                        data.setPhoto(movie.getString("poster_path"));
                        listItems.add(data);
                    }
                    listMovie.postValue(listItems);
                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());

            }
        });
    }

    public MutableLiveData<ArrayList<Data>> getMovie() {
        return listMovie;
    }
}
