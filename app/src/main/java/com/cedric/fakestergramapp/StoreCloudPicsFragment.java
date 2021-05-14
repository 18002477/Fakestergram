package com.cedric.fakestergramapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cedric.fakestergramapp.Models.ImageModel;
import com.cedric.fakestergramapp.Permissions.PicPermissions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class StoreCloudPicsFragment extends Fragment {

    public static final int PICK_CODE = 104;
    public static final int REQUEST_CODE = 101;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Uri imageData;
    ImageView chooseCloudImage, storeCloudImage, cloudImagePane;
    EditText cloudImageName;
    Bitmap imageToStore;
    String storedImageUri;

    private StorageReference mStorageRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("PhotoMemories");


    private static StoreCloudPicsFragment instance;

    public static StoreCloudPicsFragment getInstance()
    {
        if (instance == null)
            return new StoreCloudPicsFragment();
        return instance;
    }

    public StoreCloudPicsFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        mStorageRef = FirebaseStorage.getInstance().getReference("PhotoMemories");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_cloud_pics, container, false);
        chooseCloudImage = view.findViewById(R.id.img_chooseCloudPic);
        storeCloudImage = view.findViewById(R.id.img_saveCloudPic);
        cloudImagePane = view.findViewById(R.id.img_cloudImagePane);
        cloudImageName = view.findViewById(R.id.et_nameCloudImage);
        mAuth = FirebaseAuth.getInstance();

        chooseCloudImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (!PicPermissions.hasPermissions(getContext()))
                {
                    ActivityCompat.requestPermissions(getActivity(),permissions,REQUEST_CODE);
                }
                else
                {
                    chooseImage();
                }
            }
        });

        storeCloudImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (!PicPermissions.hasPermissions(getContext()))
                {
                    ActivityCompat.requestPermissions(getActivity(),permissions,REQUEST_CODE);
                }
                else
                {
                    storeImage();
                }
            }
        });



        return view;
    }

    private String GetFileExtensions (Uri uri)
    {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void chooseImage()
    {
        Intent chooseImage = new Intent(Intent.ACTION_GET_CONTENT);
        chooseImage.setType("image/*");
        startActivityForResult(Intent.createChooser(chooseImage,"Pick an image"),PICK_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_CODE && data != null)
        {
            imageData = data.getData();
            try
            {
                imageToStore = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageData);
            }
            catch (IOException e)
            {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            cloudImagePane.setImageBitmap(imageToStore);
        }
    }

    private void storeImage()
    {
        try
        {
            if (imageData != null)
            {
                final StorageReference fileRef = mStorageRef.child(System.currentTimeMillis()+"."+GetFileExtensions(imageData));
                UploadTask uploadTask = fileRef.putFile(imageData);

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            currentUser = mAuth.getCurrentUser();
                            storedImageUri = task.getResult().toString();
                            ImageModel imageModel = new ImageModel(cloudImageName.getText().toString().trim(),storedImageUri);
                            String uploadID = myRef.push().getKey();
                            myRef.child(currentUser.getUid()).child(uploadID).setValue(imageModel);

                            Toast.makeText(getContext(), "Image uploaded to the cloud", Toast.LENGTH_SHORT).show();

                            cloudImagePane.setImageResource(R.drawable.photo_icon);
                            cloudImageName.getText().clear();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else 
                {
                    Toast.makeText(getContext(), "Oops Something went wrong", Toast.LENGTH_SHORT).show();
                }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}