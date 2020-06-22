package com.example.alspicks.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.alspicks.ActivityCallback;
import com.example.alspicks.Album;
import com.example.alspicks.R;
import com.example.alspicks.SharedViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class HomeFragment extends Fragment implements View.OnClickListener{


    private Button btnSend, btnView;
    private ImageView imageView1, imageView2, imageView3;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int artistId = 0;
    private ArrayList<Album> arrayResults = new ArrayList<>();
    private ArrayList<Album> arrayDisplayResults = new ArrayList<>();
    private Set<String> titles = new HashSet<String>();
    //Activity callback
    private ActivityCallback mCallback;
    private static final String TAG = "MainActivity";




    //create instance of this fragment
    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        btnSend = root.findViewById(R.id.BtnSend);
        btnView = root.findViewById(R.id.BtnView);

        //all our buttons should use the same on click, each has it's own case below
        btnSend.setOnClickListener(this);
        btnView.setOnClickListener(this);

        SharedViewModel svModel = ViewModelProviders.of(requireNonNull(getActivity())).get(SharedViewModel.class);
        svModel.buildCurrentUserList();


        return root;
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.BtnSend) {
            mCallback.openDashboardFragment();}
        if (v.getId() == R.id.BtnView){
            mCallback.openDialogueFragment();
        }

    }
}
