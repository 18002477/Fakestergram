package com.cedric.fakestergramapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cedric.fakestergramapp.Adapters.LocalPicAdapter;
import com.cedric.fakestergramapp.Databases.LocalDatabaseHelper;


public class ViewLocalPicsFragment extends Fragment {

    RecyclerView localImageRecycler;
    LocalPicAdapter adapter;
    LocalDatabaseHelper localDatabaseHelper;

    private static ViewLocalPicsFragment instance;

    public static ViewLocalPicsFragment getInstance()
    {
        if (instance == null)
            return new ViewLocalPicsFragment();
        return instance;
    }


    public ViewLocalPicsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_local_pics, container, false);
        try
        {
            localImageRecycler = view.findViewById(R.id.localImage_recycler);
            localDatabaseHelper = new LocalDatabaseHelper(getContext());
            getData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

    private void getData()
    {
        try
        {
            if (localDatabaseHelper.displayImages() != null)
            {
                LocalPicAdapter adapter = new LocalPicAdapter(getContext(),localDatabaseHelper.displayImages());
                localImageRecycler.setHasFixedSize(true);
                localImageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                localImageRecycler.setAdapter(adapter);
            }
            else
                {
                    Toast.makeText(getContext(), "No Images Found in DB", Toast.LENGTH_SHORT).show();
                }
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}