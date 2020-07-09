package com.morimoku.project_popular_movies1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    String query = "popular";
    TextView errorMessage;
    ProgressBar mLoadingIndicator;

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
    public class FetchMovieData extends AsyncTask<String, Void, Movie[]> {
  private final String LOG_TAG = "FetchingMovieDataURL";
        final String urlImage = "http://image.tmdb.org/t/p/w185/";
        private static final String Id = "id";
        private static final String ORIGINAL_TITLE = "original_title";
        private static final String RELEASE_DATE = "release_date";
        private static final String VOTE_AVERAGE ="vote_average";
        private static final String OVERVIEW ="overview";
        private static final String POSTER_PATH ="poster_path";
        private static final String RESULT = "result";
        private static final String PARAM_LANGUAGE = "language";
        private static final String language = "en-US";

        @Override
           protected void onPreExecute(){
           super.onPreExecute();
          mLoadingIndicator.setVisibility(View.VISIBLE);
}
        @Override
        protected Movie[] doInBackground(String... strings) {
           if (strings.length ==0){
               return null;
           }
            HttpsURLConnection httpsURLConnection = null;
            BufferedReader bufferedReader = null;
             String moviePosterJSONStr = null;
             String sortBy = strings[0];

             try {
                 final String MOVIES_LINK = "https://api.themoviedb.org/3/movie/popular?";
                 final String SORT_API_KEY = "api_key";
                 final String API_KEY = "44bf7479f242490b9485b9a3ced0aa43";

                 Uri builtUri = Uri.parse(MOVIES_LINK).buildUpon()
                         .appendEncodedPath(sortBy)
                         .appendQueryParameter(SORT_API_KEY,API_KEY)
                         .appendQueryParameter(PARAM_LANGUAGE, language)
                         .build();

                         URL url = new URL(builtUri.toString());
                 Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                 httpsURLConnection = (HttpsURLConnection)url.openConnection();
                 httpsURLConnection.setRequestMethod("GET");
                 httpsURLConnection.connect();

                 InputStream inputStream = httpsURLConnection.getInputStream();
                 Scanner scanner = new Scanner(inputStream);
                 scanner.useDelimiter("\\A");
                 StringBuilder builder = new StringBuilder();
                 bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                 moviePosterJSONStr = bufferedReader.toString();


             } catch (MalformedURLException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }finally {
                 if (httpsURLConnection != null){
                     httpsURLConnection.disconnect();
                 }
                 if (bufferedReader != null){
                     try {
                         bufferedReader.close();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
                 try {
                     return MoviesJSONParse(moviePosterJSONStr);
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }


            return null;
        }
@Override
protected void onPostExecute(Movie[] movies){
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movies != null){
                showJsonDataResults();
                /*
                recyclerAdapter = new RecyclerAdapter(movies,MainActivity.this);

                recyclerView.setAdapter(recyclerAdapter);
                 */
            }
}
        private Movie[] MoviesJSONParse(String moviePosterJSONStr) throws JSONException {

            JSONObject moviesNames = new JSONObject(moviePosterJSONStr);
            JSONArray moviesArray = moviesNames.getJSONArray(RESULT);
         Movie [] movieResults = new Movie[moviesArray.length()];


            for (int i = 0; i< moviesArray.length();i++){
                Movie movie = new Movie();
                String id,poster_path, vote_average,overview,  release_date,title;
                JSONObject movieObj = moviesArray.getJSONObject(i);
                id= movieObj.optString(Id);
                poster_path = movieObj.optString(POSTER_PATH);
                title = movieObj.optString(ORIGINAL_TITLE);
                release_date = movieObj.optString(RELEASE_DATE);
                vote_average= movieObj.optString(VOTE_AVERAGE);
                overview = movieObj.optString(OVERVIEW);



                movie.setMovieId(id);
                movie.setMoviePosterPath(urlImage + poster_path);
                movie.setMovieTitle(title);
                movie.setMovieReleaseDate(release_date);
                movie.setMovieVoteAverage(vote_average);
                movie.setMovieOverview(overview);

                movieResults[i] = movie;

            }

                    return movieResults;

            }




        }

    }
