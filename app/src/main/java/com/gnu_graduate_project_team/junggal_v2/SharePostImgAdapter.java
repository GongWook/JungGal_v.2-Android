package com.gnu_graduate_project_team.junggal_v2;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SharePostImgAdapter extends RecyclerView.Adapter<SharePostImgAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Bitmap> sliderImage;

    public SharePostImgAdapter(Context context, ArrayList<Bitmap> sliderImage) {
        this.context = context;
        this.sliderImage = sliderImage;
    }

    @NonNull
    @Override
    public SharePostImgAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slider, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SharePostImgAdapter.MyViewHolder holder, int position) {
        holder.bindSliderImage(sliderImage.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderImage.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlider);
        }

        public void bindSliderImage(Bitmap bitmap)
        {
            imageView.setImageBitmap(bitmap);
        }

    }
}
