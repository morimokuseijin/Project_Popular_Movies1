package com.morimoku.project_popular_movies1;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    public static Movie[] MoviesJSONParse(Context context,String moviePosterJSONStr) throws JSONException {
       final String LOG_TAG = "FetchingMovieDataURL";
        final String urlImage = "https://image.tmdb.org/t/p/w185";
        final String Id = "id";
        final String ORIGINAL_TITLE = "original_title";
       final String RELEASE_DATE = "release_date";
        final String VOTE_AVERAGE ="vote_average";
        final String OVERVIEW ="overview";
         final String POSTER_PATH ="poster_path";
        final String RESULT = "results";
        final String PARAM_LANGUAGE = "language";
       final String language = "en-US";
        JSONObject moviesNames = new JSONObject(moviePosterJSONStr);
        JSONArray moviesArray = moviesNames.getJSONArray(RESULT);
        Movie [] movieResults = new Movie[moviesArray.length()];


        for (int i = 0; i< moviesArray.length();i++){
            String id,poster_path,vote_average,overview,release_date,title;
            Movie movie = new Movie();
            id= moviesArray.getJSONObject(i).optString(Id);
            poster_path = moviesArray.getJSONObject(i).optString(POSTER_PATH);
            title = moviesArray.getJSONObject(i).optString(ORIGINAL_TITLE);
            release_date = moviesArray.getJSONObject(i).optString(RELEASE_DATE);
            vote_average= moviesArray.getJSONObject(i).optString(VOTE_AVERAGE);
            overview = moviesArray.getJSONObject(i).optString(OVERVIEW);



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
