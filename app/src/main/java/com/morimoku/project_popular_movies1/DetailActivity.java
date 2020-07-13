package com.morimoku.project_popular_movies1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView mMoviePosterDisplay;
    TextView mMovieTitleDisplay;
    TextView mMovieRateDisplay;
    TextView mMovieReleaseDisplay;
    TextView mMoviePlotSynopsisDisplay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mMoviePosterDisplay = (ImageView)findViewById(R.id.iv_detail_movie_poster);
        mMovieTitleDisplay = (TextView)findViewById(R.id.tv_detail_title);
        mMovieRateDisplay = (TextView)findViewById(R.id.tv_detail_rate);
        mMovieReleaseDisplay  = (TextView)findViewById(R.id.tv_detail_release_date);
        mMoviePlotSynopsisDisplay = (TextView)findViewById(R.id.tv_plot_synopsis);


        String poster = getIntent().getStringExtra("poster");
        String title = getIntent().getStringExtra("title");
        String rate = getIntent().getStringExtra("rate");
        String release = getIntent().getStringExtra("release");
        String overview = getIntent().getStringExtra("overview");

        mMovieTitleDisplay.setText(title);
        mMoviePlotSynopsisDisplay.setText(overview);
        mMovieRateDisplay.setText(rate);
        mMovieReleaseDisplay.setText(release);
        Picasso.get()
                .load(poster)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(mMoviePosterDisplay);
    }
}