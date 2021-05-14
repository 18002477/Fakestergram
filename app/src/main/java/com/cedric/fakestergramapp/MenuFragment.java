package com.cedric.fakestergramapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class MenuFragment extends Fragment {

    ImageView localImage,cloudImage;
    
    private static MenuFragment instance;
    
    public static MenuFragment getInstance()
    {
        if (instance == null)
            return new MenuFragment();
        return instance;
    }
    
    public MenuFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        localImage = view.findViewById(R.id.img_localPics);
        cloudImage = view.findViewById(R.id.img_cloudPics);

        localImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openLocal = new Intent(getContext(),LocalPics.class);
                startActivity(openLocal);
            }
        });

        cloudImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openCloud = new Intent(getContext(),CloudPics.class);
                startActivity(openCloud);
            }
        });

        return view;
    }
}