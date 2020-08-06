package com.example.proyekakhir_aplikasimoviecatalogue.ui.search;

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

public class SearchViewModel extends ViewModel {

    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    private MutableLiveData<ArrayList<Data>> listSearchMovie = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Data>> listSearchTvshow = new MutableLiveData<>();

    public void setSearchMovie(String name) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Data> listSearchItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + name;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject searchMovie = list.getJSONObject(i);
                        Data data = new Data();
                        data.setId(searchMovie.getLong("id"));
                        data.setMovieName(searchMovie.getString("title"));
                        data.setDescription(searchMovie.getString("overview"));
                        data.setPhoto(searchMovie.getString("poster_path"));
                        listSearchItems.add(data);
                    }
                    listSearchMovie.postValue(listSearchItems);
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

    public MutableLiveData<ArrayList<Data>> getSearchMovie() {
        return listSearchMovie;
    }

    public void setSearchTvshow(String name) {
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Data> listSearchItems = new ArrayList<>();
        String url = "https://api.themoviedb.org/3/search/tv?api_key=" + API_KEY + "&language=en-US&query=" + name;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject searchTvshow = list.getJSONObject(i);
                        Data data = new Data();
                        data.setId(searchTvshow.getLong("id"));
                        data.setTvshowName(searchTvshow.getString("name"));
                        data.setDescription(searchTvshow.getString("overview"));
                        data.setPhoto(searchTvshow.getString("poster_path"));
                        listSearchItems.add(data);
                    }
                    listSearchTvshow.postValue(listSearchItems);
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

    public MutableLiveData<ArrayList<Data>> getSearchTvshow() {
        return listSearchTvshow;
    }
}
