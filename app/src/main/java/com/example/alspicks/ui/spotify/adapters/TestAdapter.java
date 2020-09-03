package com.example.alspicks.ui.spotify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alspicks.R;
import com.example.alspicks.ui.spotify.SpotifyFragment;

import java.util.ArrayList;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.MyViewHolder> {

    private ArrayList<SpotifyFragment.SpotifyAlbum> albumArrayList;
    private Context context;

    public TestAdapter(ArrayList<SpotifyFragment.SpotifyAlbum> albums){
        this.albumArrayList = albums;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView album, artist;

        MyViewHolder(View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.tvAlbum);
            artist = itemView.findViewById(R.id.tvArtist);
        }
    }

    class ResultsViewHolder extends RecyclerView.ViewHolder {
        TextView album, artist;

        ResultsViewHolder(View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.tvAlbum);
            artist = itemView.findViewById(R.id.tvArtist);
        }
    }


    @NonNull
    @Override
    public TestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_spotify_layout, parent, false);
        return new TestAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TestAdapter.MyViewHolder holder, final int position) {

        SpotifyFragment.SpotifyAlbum album = albumArrayList.get(position);
        holder.artist.setText(album.getArtist());
        holder.album.setText(album.getName());
    }

    @Override
    public int getItemCount() {
        return albumArrayList.size();
    }
}
