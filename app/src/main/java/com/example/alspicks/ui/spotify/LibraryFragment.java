package com.example.alspicks.ui.spotify;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.alspicks.ActivityCallback;
import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.example.alspicks.ui.spotify.adapters.TestAdapter;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

public class LibraryFragment extends Fragment {

    private RecyclerView recyclerView;
    private TestAdapter mAdapter;
    private ArrayList<SpotifyFragment.SpotifyAlbum> mArrayList = new ArrayList<>();

    //Activity callback
    private ActivityCallback mCallback;

    //create instance of this fragment
    public static LibraryFragment newInstance() {
        return new LibraryFragment();
    }

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


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        SharedViewModel svModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);
        mArrayList = svModel.getUserLibraryAlbums();
        View rootView = inflater.inflate(R.layout.fragment_library, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewLibrary);
        mAdapter = new TestAdapter(mArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        mArrayList = svModel.getUserLibraryAlbums();
        mAdapter.notifyDataSetChanged();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mCallback.openDashboardFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return rootView;
    }

}
