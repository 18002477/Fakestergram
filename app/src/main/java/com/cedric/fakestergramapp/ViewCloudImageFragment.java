package com.cedric.fakestergramapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cedric.fakestergramapp.Adapters.CloudPicAdapter;
import com.cedric.fakestergramapp.Models.ImageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewCloudImageFragment extends Fragment {

    RecyclerView cloudRecycler;
    CloudPicAdapter cloudPicAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //DatabaseReference myRef = database.getReference("PhotoMemories");
    private List<ImageModel> cloudPicsList;
    FirebaseAuth mAuth;
    //FirebaseUser currentUser;


    private static ViewCloudImageFragment instance;

    public static ViewCloudImageFragment getInstance()
    {
        if (instance == null)
            return new ViewCloudImageFragment();
        return instance;
    }

    public ViewCloudImageFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_cloud_image, container, false);

        mAuth = FirebaseAuth.getInstance();
        // Current user can only view images they have stored in the DB
        DatabaseReference myRef = database.getReference("PhotoMemories").child(mAuth.getCurrentUser().getUid());
        cloudRecycler = view.findViewById(R.id.cloudImage_recycler);
        cloudRecycler.setHasFixedSize(true);
        cloudRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        cloudPicsList = new ArrayList<>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot cloudImages: snapshot.getChildren())
                {
                    //String user = mAuth.getCurrentUser().getUid();

                    ImageModel imageModel = cloudImages.getValue(ImageModel.class);

                    cloudPicsList.add(imageModel);

                }
                cloudPicAdapter = new CloudPicAdapter(getContext(),cloudPicsList);
                cloudRecycler.setAdapter(cloudPicAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
}