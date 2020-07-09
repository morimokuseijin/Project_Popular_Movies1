package com.morimoku.project_popular_movies1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecycleAdapterViewHolder>{
    String url = "http://image.tmdb.org/t/p/w185/";
    private Movie[] mMovieData;
    private final MovieOnClickHandler mClickHandler;

    public RecyclerAdapter(Movie[] movies, MovieOnClickHandler clickHandler){
        mMovieData = movies;
        mClickHandler = clickHandler;

    }

    public interface MovieOnClickHandler{
        void onClick(int position);
    }
     public class RecycleAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final ImageView mMovieImageView;


         public RecycleAdapterViewHolder(@NonNull View itemView) {
             super(itemView);
             mMovieImageView = (ImageView)itemView.findViewById(R.id.movie_ranking);
             itemView.setOnClickListener(this);
         }

         @Override
         public void onClick(View v) {
             int adapterPosition = getAdapterPosition();
             mClickHandler.onClick(adapterPosition);

         }
     }

    @Override
    public RecycleAdapterViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutForListItem = R.layout.movie_item_list;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachImmediately = false;

        View view = layoutInflater.inflate(layoutForListItem,parent,shouldAttachImmediately);
        return new RecycleAdapterViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterViewHolder holder, int position) {
        String movieToBind = mMovieData[position].getMoviePosterPath();

        Picasso.get()
                .load(movieToBind)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.mMovieImageView);
    }

    @Override
    public int getItemCount() {
        if(null == mMovieData){
            return 0;

        }
        return mMovieData.length;
    }



}
