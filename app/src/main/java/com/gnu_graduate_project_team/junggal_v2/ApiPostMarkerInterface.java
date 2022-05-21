package com.gnu_graduate_project_team.junggal_v2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiPostMarkerInterface {

    /** 나눔 게시물 마커 정보 불러오기 **/
    @POST("share_post/marklist")
    Call<List<MarkerVO>> sharePostMarker(@Body Point point);

}
