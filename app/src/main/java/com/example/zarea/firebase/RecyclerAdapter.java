package com.example.zarea.firebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Zarea on 27/09/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private Movie list;
    private Context mContext;

    public RecyclerAdapter(Movie list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }


    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerHolder holder, final int position) {

        Results movie = list.getResults()[position];
        Picasso.with(mContext)
                .load("https://image.tmdb.org/t/p/w500/" + movie.getPoster_path())
                .placeholder(R.drawable.loading)
                .error(R.drawable.photo)
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return list.getResults().length;
    }

    class RecyclerHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public RecyclerHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.movie_picture);

        }
    }
}


