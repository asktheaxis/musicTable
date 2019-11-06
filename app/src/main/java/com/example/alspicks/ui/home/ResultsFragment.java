package com.example.alspicks.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alspicks.Album;
import com.example.alspicks.R;
import com.example.alspicks.RecyclerAlbumAdapter;
import com.example.alspicks.SharedViewModel;

import java.util.ArrayList;
import java.util.Objects;

public class ResultsFragment extends Fragment {

    private RecyclerAlbumAdapter mAdapter;
    private ArrayList<Album> mArrayList = new ArrayList<>();
    SharedViewModel svModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(SharedViewModel.class);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mAdapter = new RecyclerAlbumAdapter(mArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        mArrayList = svModel.getAlbumResults();
        mAdapter.notifyDataSetChanged();

        return rootView;
    }
}
