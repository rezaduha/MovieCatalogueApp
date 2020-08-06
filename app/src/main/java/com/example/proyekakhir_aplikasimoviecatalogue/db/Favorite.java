package com.example.proyekakhir_aplikasimoviecatalogue.db;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.example.proyekakhir_aplikasimoviecatalogue.db.Favorite.TABLE_NAME;

@Entity(tableName = TABLE_NAME)
public class Favorite implements Parcelable {

    public static final String DATABASE_NAME = "favoritedb";
    public static final String TABLE_NAME = "table_favorite";
    public static final String COLUMN_CATALOGUE = "catalogue";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "title";
    public static final String COLUMN_DESCRIPTION = "overview";
    public static final String COLUMN_PHOTO = "poster";

    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    private long id;
    @ColumnInfo(name = COLUMN_CATALOGUE)
    private String catalogue;
    @ColumnInfo(name = COLUMN_NAME)
    private String name;
    @ColumnInfo(name = COLUMN_DESCRIPTION)
    private String description;
    @ColumnInfo(name = COLUMN_PHOTO)
    private String photo;

    public Favorite() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(String catalogue) {
        this.catalogue = catalogue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Favorite(long id, String catalogue, String name, String description, String photo) {
        this.id = id;
        this.catalogue = catalogue;
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    public static Favorite fromContentValues(ContentValues values) {
        final Favorite favorite = new Favorite();

        if (values.containsKey(COLUMN_ID)) {
            favorite.id = values.getAsLong(COLUMN_ID);
        }
        if (values.containsKey(COLUMN_CATALOGUE)) {
            favorite.catalogue = values.getAsString(COLUMN_CATALOGUE);
        }
        if (values.containsKey(COLUMN_NAME)) {
            favorite.name = values.getAsString(COLUMN_NAME);
        }
        if (values.containsKey(COLUMN_DESCRIPTION)) {
            favorite.description = values.getAsString(COLUMN_DESCRIPTION);
        }
        if (values.containsKey(COLUMN_PHOTO)) {
            favorite.photo = values.getAsString(COLUMN_PHOTO);
        }
        return favorite;
    }

    protected Favorite(Parcel in) {
        id = in.readLong();
        catalogue = in.readString();
        name = in.readString();
        description = in.readString();
        photo = in.readString();
    }

    public static final Creator<Favorite> CREATOR = new Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel in) {
            return new Favorite(in);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(catalogue);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(photo);
    }
}
