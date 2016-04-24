package com.example.android.popularmovies;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Lohiya on 11-Apr-16.
 */
public class PopularMoviesAdapter extends ArrayAdapter<PopularMovies> {

    private static final String LOG_TAG = PopularMoviesAdapter.class.getSimpleName();

    public PopularMoviesAdapter(Activity context, ArrayList<PopularMovies> popularMovies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, popularMovies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gets the AndroidFlavor object from the ArrayAdapter at the appropriate position
        PopularMovies popularMovies = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate( R.layout.movie_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load(popularMovies.poster).into(imageView);

        return convertView;
    }
}

