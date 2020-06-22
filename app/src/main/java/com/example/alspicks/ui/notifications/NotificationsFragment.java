package com.example.alspicks.ui.notifications;

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
import com.example.alspicks.Album;
import com.example.alspicks.R;
import com.example.alspicks.RecyclerAlbumAdapter;
import com.example.alspicks.SharedViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private ActivityCallback mCallback;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private RecyclerAlbumAdapter mAdapter;
    private ArrayList<Album> mArrayList = new ArrayList<>();
    ArrayList<Album> userAlbums = new ArrayList<>();
    private LayoutInflater neededInflater;
    private ViewGroup neededContainer;
    private Bundle neededSavedInstanceState;
    private String user;
    private View rootView;


    //create instance of this fragment
    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        SharedViewModel svModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);
        mArrayList = svModel.getUserList();
        rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView2);
        mAdapter = new RecyclerAlbumAdapter(mArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                mCallback.openHomeFragment();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        return rootView;
    }
}