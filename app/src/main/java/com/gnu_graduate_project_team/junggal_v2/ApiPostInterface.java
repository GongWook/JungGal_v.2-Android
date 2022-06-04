package com.gnu_graduate_project_team.junggal_v2;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiPostInterface {

    /** 나눔 게시물 Upload **/
    @Multipart
    @POST("share_post/upload")
    Call<SharePostVO> sharePostUpload(@PartMap Map<String, RequestBody> params, @Part ArrayList<MultipartBody.Part> files);

    /** 나눔 게시물 Select **/
    @POST("share_post/selectPost")
    Call<SharePostVO> selectPost(@Body MarkerVO marker);

    /** 나눔 게시물 작성자 Select **/
    @POST("share_post/selectUser")
    Call<UserVO> selectWriter(@Body UserVO user);

    /** 나눔 게시물 이미지 Select **/
    @POST("share_post/display")
    Call<SharePostImageVO> getPostImg(@Body SharePostVO postVO);


}
