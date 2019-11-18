package com.example.alspicks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerResultsAdapter extends RecyclerView.Adapter<RecyclerResultsAdapter.ResultsViewHolder> {

    private ArrayList<Album> albumArrayList;
    private Context context;

    public RecyclerResultsAdapter(ArrayList<Album> albums){
        this.albumArrayList = albums;
    }

    class ResultsViewHolder extends RecyclerView.ViewHolder {
        TextView album, artist, year;
        ImageButton albumArt;

        ResultsViewHolder(View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.tvAlbum);
            artist = itemView.findViewById(R.id.tvArtist);
            year = itemView.findViewById(R.id.tvYear);
            albumArt = itemView.findViewById(R.id.albumButton);
        }
    }


    @NonNull
    @Override
    public ResultsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_layout, parent, false);
        return new ResultsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultsViewHolder holder, final int position) {

        Album album = albumArrayList.get(position);
        holder.artist.setText(album.getArtist());
        holder.album.setText(album.getName());
        holder.year.setText(album.getYear());
        Picasso.with(holder.albumArt.getContext()).load(album.coverImage).into(holder.albumArt);
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
