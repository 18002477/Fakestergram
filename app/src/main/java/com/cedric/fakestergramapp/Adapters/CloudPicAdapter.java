package com.cedric.fakestergramapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cedric.fakestergramapp.Models.ImageModel;
import com.cedric.fakestergramapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CloudPicAdapter extends RecyclerView.Adapter<CloudPicAdapter.MyViewHolder> {

    Context context;
    List<ImageModel> photoList = new ArrayList<>();
    //ImageModel imageModel;

    public CloudPicAdapter(){}

    public CloudPicAdapter(Context context, List<ImageModel> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(context).inflate(R.layout.local_pics_items,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        ImageModel imageModel = photoList.get(position);

        holder.localImageName.setText(imageModel.getImageName());
        Picasso.get().load(imageModel.getImageUrl()).fit()
                .centerCrop().into(holder.localImage);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView localImage;
        TextView localImageName;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            localImage = itemView.findViewById(R.id.adapter_image);
            localImageName = itemView.findViewById(R.id.adapter_imageName);

        }
    }
}
