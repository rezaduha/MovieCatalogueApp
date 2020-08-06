package com.example.proyekakhir_aplikasimoviecatalogue;

import android.os.Parcel;
import android.os.Parcelable;

public class Data implements Parcelable {

    private long id;
    private String movieName;
    private String tvshowName;
    private String description;
    private String photo;
    private String release;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getTvshowName() {
        return tvshowName;
    }

    public void setTvshowName(String tvshowName) {
        this.tvshowName = tvshowName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public Data() {
    }

    protected Data(Parcel in) {
        id = in.readLong();
        movieName = in.readString();
        tvshowName = in.readString();
        description = in.readString();
        photo = in.readString();
        release = in.readString();
    }

    public static final Creator<Data> CREATOR = new Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(movieName);
        dest.writeString(tvshowName);
        dest.writeString(description);
        dest.writeString(photo);
        dest.writeString(release);
    }
}
