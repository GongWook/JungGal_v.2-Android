package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewActivity extends Activity {

    /** review 기능 개발 완료 coomit 주석 **/

    /** XML 관련 변수 **/
    private ImageView review_list_back_btn;
    private RecyclerView reviewRecyclerView;

    /** PostWriter Data **/
    private String sharePostWriter;
    private String sharePostWriterName;

    /** ReviewVO **/
    private ReviewVO reviewVO = new ReviewVO();

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiReviewInterface reviewInterface = retrofit.create(ApiReviewInterface.class);

    /** Review 내역 **/
    private ArrayList<ReviewVO> reviewList;
    private List<ReviewItem> items = new ArrayList<>();
    ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.review_list_activity);

        /** PostWriter Data 가져오기 **/
        Intent intent = getIntent();
        sharePostWriter = intent.getStringExtra("sharePostWriter");
        sharePostWriterName = intent.getStringExtra("sharePostWriterName");

        /** Review VO Data 초기화 **/
        reviewVO.setPostWriter(sharePostWriter);
        
        /** XML 초기화 **/
        review_list_back_btn = findViewById(R.id.review_list_back_btn);
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView);

        /** recyclerView 등록 **/
        reviewAdapter = new ReviewAdapter(items);
        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        reviewRecyclerView.setLayoutManager(manager);
        reviewRecyclerView.setAdapter(reviewAdapter);

        review_list_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        selectReview();
    }

    /** 리뷰 내역 호출 **/
    public void selectReview()
    {
        Call<List<ReviewVO>> call = reviewInterface.selectReviewList(reviewVO);
        call.enqueue(new Callback<List<ReviewVO>>() {
            @Override
            public void onResponse(Call<List<ReviewVO>> call, Response<List<ReviewVO>> response) {
                reviewList= (ArrayList<ReviewVO>) response.body();
                for(ReviewVO review : reviewList)
                {
                    items.add(new ReviewItem(0, review));
                }

                reviewRecyclerView.setAdapter(reviewAdapter);
            }

            @Override
            public void onFailure(Call<List<ReviewVO>> call, Throwable t) {

            }
        });
    }
}
