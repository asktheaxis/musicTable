package com.example.alspicks.ui.home;

import android.content.Context;
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

import com.example.alspicks.ActivityCallback;
import com.example.alspicks.Album;
import com.example.alspicks.R;
import com.example.alspicks.RecyclerResultsAdapter;
import com.example.alspicks.SharedViewModel;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

public class ResultsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerResultsAdapter mAdapter;
    private ArrayList<Album> mArrayList = new ArrayList<>();

    //Activity callback
    private ActivityCallback mCallback;

    //create instance of this fragment
    public static ResultsFragment newInstance() {
        return new ResultsFragment();
    }




    //override the onAttach and onDetach methods in fragment lifecycle
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (ActivityCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        SharedViewModel svModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);
        mArrayList = svModel.getAlbumResults();
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        mAdapter = new RecyclerResultsAdapter(mArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        mArrayList = svModel.getAlbumResults();
        mAdapter.notifyDataSetChanged();
        return rootView;
    }
}
