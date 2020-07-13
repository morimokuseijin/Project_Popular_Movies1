package com.morimoku.project_popular_movies1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.MovieOnClickHandler{

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    String query = "popular";
    TextView errorMessage;
    ProgressBar mLoadingIndicator;
    private Movie[] movieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        errorMessage = (TextView)findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar)findViewById(R.id.pb_loading_indicator);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview_movies);
        int mNoOfColumns = calculateNoOfColumns(getApplicationContext());
        GridLayoutManager layoutManager = new GridLayoutManager(this, mNoOfColumns);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);


        recyclerView.setAdapter(recyclerAdapter);

        loadMovieData();


    }
public int calculateNoOfColumns(Context context) {
    DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
    float heiWid = displayMetrics.widthPixels / displayMetrics.density;
    int noOfColumns = (int)(heiWid/180);
    return noOfColumns;
}
    private void loadMovieData() {
        String queryType = query;
        showJsonDataResults();
        new FetchMovieData().execute(queryType);
    }
    private void showJsonDataResults() {
        errorMessage.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(int adapterPosition) {


        Context context = this;
        Class destinationClass = DetailActivity.class;

        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, adapterPosition);
        intentToStartDetailActivity.putExtra("id", movieData[adapterPosition].getMovieId());
        intentToStartDetailActivity.putExtra("title", movieData[adapterPosition].getMovieTitle());
        intentToStartDetailActivity.putExtra("poster", movieData[adapterPosition].getMoviePosterPath());
        intentToStartDetailActivity.putExtra("rate", movieData[adapterPosition].getMovieVoteAverage());
        intentToStartDetailActivity.putExtra("release", movieData[adapterPosition].getMovieReleaseDate());
        intentToStartDetailActivity.putExtra("overview", movieData[adapterPosition].getMovieOverview());

        startActivity(intentToStartDetailActivity);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();

        if (menuItemSelected == R.id.action_popular) {
            query = "popular";
            loadMovieData();
            return true;
        }

        if (menuItemSelected == R.id.action_top_rated) {
            query = "top_rated";
            loadMovieData();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    public class FetchMovieData extends AsyncTask<String, Void, Movie[]> {
  private final String LOG_TAG = "FetchingMovieDataURL";
        final String urlImage = "http://image.tmdb.org/t/p/w185/";
        private static final String Id = "id";
        private static final String ORIGINAL_TITLE = "original_title";
        private static final String RELEASE_DATE = "release_date";
        private static final String VOTE_AVERAGE ="vote_average";
        private static final String OVERVIEW ="overview";
        private static final String POSTER_PATH ="poster_path";
        private static final String RESULT = "results";
        private static final String PARAM_LANGUAGE = "language";
        private static final String language = "en-US";

        @Override
           protected void onPreExecute(){
           super.onPreExecute();
          mLoadingIndicator.setVisibility(View.VISIBLE);
}
        @Override
        protected Movie[] doInBackground(String... params) {
            if (params.length == 0){
                return null;
            }

            String sortBy = params[0];
            URL movieRequestUrl = NetworkUtils.buildUrl(sortBy);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);

                movieData
                        = JsonUtils.MoviesJSONParse(MainActivity.this, jsonMovieResponse);

                return movieData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
@Override
protected void onPostExecute(Movie[] movies){
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null){
                showJsonDataResults();
                recyclerAdapter = new RecyclerAdapter(movies,MainActivity.this);
                recyclerView.setAdapter(recyclerAdapter);

            }
}




        }

    }
