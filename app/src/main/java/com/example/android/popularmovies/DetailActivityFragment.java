package com.example.android.popularmovies;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // The detail Activity called via intent.  Inspect the intent for movie data.
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(getString(R.string.movie_key))) {
            PopularMovies movie = intent.getParcelableExtra(getString(R.string.movie_key));
            ((TextView) rootView.findViewById(R.id.title)).setText(movie.title);
            ((TextView) rootView.findViewById(R.id.rating)).setText(Html.fromHtml("<b>" + getString(R.string.rating_label) + ":</b>" + movie.rating + "/10"));
            ((TextView) rootView.findViewById(R.id.release)).setText(Html.fromHtml("<b>" + getString(R.string.release_label) + ":</b> " + movie.release));
            ((TextView) rootView.findViewById(R.id.summary)).setText(Html.fromHtml("<b>" + getString(R.string.plot_synopsis_label) + ":</b> " + movie.overview));
            Picasso.with(getContext()).load(movie.poster).into((ImageView) rootView.findViewById(R.id.poster));
        }

        return rootView;
    }


}
