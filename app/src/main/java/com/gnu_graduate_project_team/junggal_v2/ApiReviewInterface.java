package com.gnu_graduate_project_team.junggal_v2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiReviewInterface {

    /** 리뷰등록 **/
    @POST("review/insertReview")
    Call<ReviewVO> insertReview(@Body ReviewVO reviewVO);

    /** 리뷰 내역 조회 **/
    @POST("review/selectReviewList")
    Call<List<ReviewVO>> selectReviewList(@Body ReviewVO reviewVO);

}
