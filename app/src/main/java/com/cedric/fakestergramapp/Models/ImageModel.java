package com.cedric.fakestergramapp.Models;

import android.graphics.Bitmap;

public class ImageModel
{
    // local DB
    String imageName;
    Bitmap imageBitmap;

    // cloud DB
    String imageUrl;
    String userID;


    // Empty Constructor
    public ImageModel() { }

    // local DB constructor
    public ImageModel(String imageName, Bitmap imageBitmap) {
        this.imageName = imageName;
        this.imageBitmap = imageBitmap;
    }

    // Cloud DB Constructor
    public ImageModel(String imageName, String imageUrl) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    // Cloud DB Constructor With User
    public ImageModel(String imageName, String imageUrl, String userID) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.userID = userID;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
