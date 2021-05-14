package com.cedric.fakestergramapp.Databases;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.cedric.fakestergramapp.Models.ImageModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class LocalDatabaseHelper extends SQLiteOpenHelper
{
    Context context;
    private static final String DATABASENAME = "images.db";
    public static final int DATABASEVERSION = 1;
    private ByteArrayOutputStream convertBitmapToByteArray;
    private byte [] imageInBytes;

    private static String createTableQuery = "create table imageStore(imageName TEXT, imageBitmap BLOB)";

    public LocalDatabaseHelper(@Nullable Context context)
    {
        super(context,DATABASENAME,null,DATABASEVERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        try
        {
            sqLiteDatabase.execSQL(createTableQuery);
            Toast.makeText(context, "Table Created", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    // Method used to store local images into SQLite DB
    public void storeImageLocal(ImageModel imageModel)
    {
        try
        {
            SQLiteDatabase imageDatabase = this.getWritableDatabase();
            Bitmap imageToStore = imageModel.getImageBitmap();
            convertBitmapToByteArray = new ByteArrayOutputStream();
            imageToStore.compress(Bitmap.CompressFormat.JPEG,100,convertBitmapToByteArray);
            imageInBytes = convertBitmapToByteArray.toByteArray();
            ContentValues contentValues = new ContentValues();
            contentValues.put("imageName",imageModel.getImageName());
            contentValues.put("imageBitmap",imageInBytes);

            long checkIfQueryRuns = imageDatabase.insert("imageStore",null,contentValues);
            if (checkIfQueryRuns != -1)
            {
                Toast.makeText(context, "Image Saved Successfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context, "Unable to save image", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("Save To DB","StoreImageLocal: "+e.getMessage());
        }
    }

    public ArrayList<ImageModel> displayImages()
    {
        try
        {
            SQLiteDatabase imageDatabase = this.getReadableDatabase();
            ArrayList<ImageModel> dbImages = new ArrayList<>();
            Cursor cursor = imageDatabase.rawQuery("select * from imageStore",null);

            if (cursor.getCount() != 0)
            {
                while (cursor.moveToNext())
                {
                    String imageName = cursor.getString(0);
                    byte [] image = cursor.getBlob(1);

                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(image,0,image.length);

                    dbImages.add(new ImageModel(imageName,imageBitmap));

                }
                Toast.makeText(context, "Loading Images", Toast.LENGTH_SHORT).show();
                return dbImages;
            }
            else
                {
                    Toast.makeText(context, "No Images Found", Toast.LENGTH_SHORT).show();
                    return null;
                }

        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("RETRIEVE FROM DB","displayImages: "+e.getMessage());
            return null;
        }
    }

}
