package com.gnu_graduate_project_team.junggal_v2;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SellPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SellPostVO> sellDataList = null;

    public SellPostAdapter(ArrayList<SellPostVO> dataList) {
        sellDataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == SellPostViewType.WITHOUT_CHECK)
        {
            view = inflater.inflate(R.layout.sell_post_item_without_check, parent, false);
            return new WithOutCheckViewHolder(view);
        }
        else
        {
            view = inflater.inflate(R.layout.activity_chat_right, parent, false);
            return new WithCheckViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof WithOutCheckViewHolder)
        {
            ((WithOutCheckViewHolder) holder).sell_post_item_name.setText(sellDataList.get(position).getSell_post_item_name());
            ((WithOutCheckViewHolder) holder).sell_post_story.setText(sellDataList.get(position).getSell_post_story());
            ((WithOutCheckViewHolder) holder).sell_post_item_price.setText(sellDataList.get(position).getSell_post_item_price());
        }
        else
        {
            ((WithCheckViewHolder) holder).sell_post_item_name.setText(sellDataList.get(position).getSell_post_item_name());
            ((WithCheckViewHolder) holder).sell_post_story.setText(sellDataList.get(position).getSell_post_story());
            ((WithCheckViewHolder) holder).sell_post_item_price.setText(sellDataList.get(position).getSell_post_item_price());
            ((WithCheckViewHolder) holder).sell_post_item_check.setText(sellDataList.get(position).getSell_post_item_check());
        }

    }

    @Override
    public int getItemCount() {
        return sellDataList.size();
    }

    public class WithOutCheckViewHolder extends RecyclerView.ViewHolder{
        EditText sell_post_item_name;
        ImageView dish_photo;
        EditText sell_post_story;
        EditText sell_post_item_price;

        WithOutCheckViewHolder(View itemView)
        {
            super(itemView);

            sell_post_item_name = itemView.findViewById(R.id.sell_post_item_name);
            dish_photo = itemView.findViewById(R.id.dish_photo);
            sell_post_story = itemView.findViewById(R.id.sell_post_story);
            sell_post_item_price = itemView.findViewById(R.id.sell_post_item_price);
        }
    }

    public class WithCheckViewHolder extends RecyclerView.ViewHolder{
        EditText sell_post_item_name;
        ImageView dish_photo;
        EditText sell_post_story;
        EditText sell_post_item_price;
        CheckBox sell_post_item_check;

        WithCheckViewHolder(View itemView)
        {
            super(itemView);

            sell_post_item_name = itemView.findViewById(R.id.sell_post_item_name);
            dish_photo = itemView.findViewById(R.id.dish_photo);
            sell_post_story = itemView.findViewById(R.id.sell_post_story);
            sell_post_item_price = itemView.findViewById(R.id.sell_post_item_price);
            sell_post_item_check = itemView.findViewById(R.id.sell_post_item_check);
        }
    }



}
