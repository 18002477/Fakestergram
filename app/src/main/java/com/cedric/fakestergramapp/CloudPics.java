package com.cedric.fakestergramapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CloudPics extends AppCompatActivity {

    ImageView storeCloudPics,viewCloudPics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_pics);

        viewCloudPics = findViewById(R.id.img_viewCloudImages);
        storeCloudPics = findViewById(R.id.img_storeCloudImages);

        storeCloudPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.load_cloudImagePlaceHolder,StoreCloudPicsFragment.getInstance());
                transaction.commitAllowingStateLoss();
            }
        });

        viewCloudPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.load_cloudImagePlaceHolder,ViewCloudImageFragment.getInstance());
                transaction.commitAllowingStateLoss();
            }
        });
    }
}