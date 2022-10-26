package com.gnu_graduate_project_team.junggal_v2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReviewItem> items;

    public ReviewAdapter(List<ReviewItem> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_activity, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReviewVO reviewVO = (ReviewVO) items.get(position).getObject();
        ((ReviewHolder)holder).setModelData(reviewVO);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    static class ReviewHolder extends RecyclerView.ViewHolder
    {
        private TextView reviewUserName;
        private RatingBar reviewRatingBar;
        private TextView reviewContent;
        private TextView reviewTime;
        private ImageView reviewIcon;
        private ReviewVO reviewVO;


        public ReviewHolder(View itemView) {
            super(itemView);

            reviewUserName = itemView.findViewById(R.id.reviewUserName);
            reviewRatingBar = itemView.findViewById(R.id.reviewRatingBar);
            reviewContent = itemView.findViewById(R.id.reviewContent);
            reviewTime = itemView.findViewById(R.id.reviewTime);
            reviewIcon = itemView.findViewById(R.id.reviewIcon);
        }

        public void setModelData(ReviewVO reviewVO)
        {
            this.reviewVO = reviewVO;
            reviewUserName.setText(reviewVO.getApplyUserName());
            reviewRatingBar.setRating(reviewVO.getReviewPoint());
            reviewContent.setText(reviewVO.getContent());
            reviewTime.setText(reviewVO.calcTime());

            float reviewPoint = reviewVO.getReviewPoint();

            if(reviewPoint==5.0)
            {
                reviewIcon.setImageResource(R.drawable.five_point_custom);
            }
            else if(reviewPoint==4.0)
            {
                reviewIcon.setImageResource(R.drawable.four_point_custom);
            }
            else if(reviewPoint==3.0)
            {
                reviewIcon.setImageResource(R.drawable.three_point_custom);
            }
            else if(reviewPoint==2.0)
            {
                reviewIcon.setImageResource(R.drawable.two_point_custom);
            }
            else
            {
                reviewIcon.setImageResource(R.drawable.one_point_custom);
            }
        }
    }
}
