package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ReviewRegistActivity extends Activity {

    /** XML 관련 변수 **/
    private ImageView reviewBackBtn;
    private TextView reviewSubmit;
    private RatingBar ratingBar;
    private EditText review_content;

    /** get extra data **/
    private String postWriterId;
    private String applyUser;

    /** review 관련 변수 **/
    private ReviewVO reviewVO;
    private float ratingScore;
    private String content;
    private int alarmId;
    private String ApplyUserName;

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiReviewInterface apiReviewInterface = retrofit.create(ApiReviewInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.review_regist_activity);

        /** PostWriterID 받기 **/
        Intent intent = getIntent();
        postWriterId = intent.getStringExtra("postWriterId");
        applyUser = intent.getStringExtra("applyUser");
        alarmId = intent.getIntExtra("alarmId",0);
        Log.d("alarmId test",alarmId+"");

        /** XML 초기화 **/
        reviewBackBtn = findViewById(R.id.reviewBackBtn);
        reviewSubmit = findViewById(R.id.reviewSubmit);
        ratingBar = findViewById(R.id.ratingBar);
        review_content = findViewById(R.id.review_content);

        /** Review VO 초기화 **/
        reviewVO = new ReviewVO();

        /** Apply User Name 초기화 **/
        ApplyUserName = PreferenceManager.getString(ReviewRegistActivity.this,"user_name");
        Log.d("ApplyUserName test",ApplyUserName);


        /** 제출하기 버튼 클릭 리스너 **/
        reviewSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                content = review_content.getText().toString().trim();

                if(ratingScore==0.0)
                {
                    Toast.makeText(ReviewRegistActivity.this, "별점을 등록해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(content.equals(""))
                {
                    Toast.makeText(ReviewRegistActivity.this, "리뷰를 작성해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    reviewVO.setPostWriter(postWriterId);
                    reviewVO.setApplyUser(applyUser);
                    reviewVO.setApplyUserName(ApplyUserName);
                    reviewVO.setReviewPoint(ratingScore);
                    reviewVO.setContent(content);
                    reviewVO.setAlarmId(alarmId);

                    Call<ReviewVO> call = apiReviewInterface.insertReview(reviewVO);
                    call.enqueue(new Callback<ReviewVO>() {
                        @Override
                        public void onResponse(Call<ReviewVO> call, Response<ReviewVO> response) {
                            Toast.makeText(ReviewRegistActivity.this, "리뷰를 남겨주셔서 감사합니다 😊", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK,intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ReviewVO> call, Throwable t) {
                            Toast.makeText(ReviewRegistActivity.this, "리뷰 등록이 실패 되었습니다.\n네트워크 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        
        /** 뒤로가기 버튼 클릭 리스너 **/
        reviewBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /** ratingBar 클릭 이벤트 리스너 **/
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingScore = rating;
            }
        });



    }
}
