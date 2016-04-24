package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private PopularMoviesAdapter moviesAdapter;

    private ArrayList<PopularMovies> movieList;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            movieList = new ArrayList<PopularMovies>();
        }
        else {
            movieList = savedInstanceState.getParcelableArrayList("movies");
        }

        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        moviesAdapter = new PopularMoviesAdapter(getActivity(), new ArrayList<PopularMovies>());

        // Get a reference to the ListView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.movies_grid);
        gridView.setAdapter(moviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopularMovies movieContent = moviesAdapter.getItem(position);
                Intent intent = new Intent(getActivity(),DetailActivity.class);
                intent.putExtra(getString(R.string.movie_key),movieContent);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort_by = prefs.getString(getString(R.string.sort_by_key), getString(R.string.sort_by_default));
        movieTask.execute(sort_by);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<PopularMovies>> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private ArrayList<PopularMovies> getMovieDataFromJson(String movieJsonStr, int numMovies)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TMD_RESULTS = "results";
            final String TMD_ORIGINAL_TITLE = "original_title";
            final String TMD_POSTER_PATH = "poster_path";
            final String TMD_OVERVIEW = "overview";
            final String TMD_VOTE_AVERAGE = "vote_average";
            final String TMD_RELEASE_DATE = "release_date";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray resultArray = movieJson.getJSONArray(TMD_RESULTS);

            ArrayList<PopularMovies> arraylist = new ArrayList<PopularMovies>();

            for (int i = 0; i < resultArray.length(); i++) {


                JSONObject movieDetail = resultArray.getJSONObject(i);

                String title = movieDetail.getString(TMD_ORIGINAL_TITLE);
                String poster = movieDetail.getString(TMD_POSTER_PATH);
                String overview = movieDetail.getString(TMD_OVERVIEW);
                Double rating = movieDetail.getDouble(TMD_VOTE_AVERAGE);
                String release = movieDetail.getString(TMD_RELEASE_DATE);

                arraylist.add(new PopularMovies(title, poster, overview, rating, release));

            }

            return arraylist;
        }

        @Override
        protected ArrayList<PopularMovies> doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            int numMovies = 10;

            try {

                final String MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?" ;

                final String SORT_BY_PARAM = "sort_by";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)//To be put in build.config
                        .build();

                URL url = new URL(builtUri.toString());


                // Create the request to TheMovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                movieJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }

            }
            if (movieJsonStr != null) {
                try {
                    return getMovieDataFromJson(movieJsonStr, numMovies);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<PopularMovies> result) {

            if (result != null) {
                moviesAdapter.clear();
                moviesAdapter.addAll(result);
            }
            else {
                CharSequence text = "Check Your Internet Connection ";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(getContext(), text, duration);
                toast.show();
            }
        }
    }
}