package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lohiya on 16-Apr-16.
 */
public class PopularMovies implements Parcelable {
    String title;
    String poster;
    String overview;
    Double rating;
    String release;


    public PopularMovies(String title, String poster, String overview, Double rating, String release) {
        this.title = title;
        this.poster = "http://image.tmdb.org/t/p/w342/" + poster;
        this.overview = overview;
        this.rating = rating;
        this.release = release;
    }

    private PopularMovies(Parcel in) {
        title = in.readString();
        poster = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        release = in.readString();
    }

    public static final Creator<PopularMovies> CREATOR = new Creator<PopularMovies>() {
        @Override
        public PopularMovies createFromParcel(Parcel in) {
            return new PopularMovies(in);
        }

        @Override
        public PopularMovies[] newArray(int size) {
            return new PopularMovies[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(overview);
        dest.writeDouble(rating);
        dest.writeString(release);
    }
}
