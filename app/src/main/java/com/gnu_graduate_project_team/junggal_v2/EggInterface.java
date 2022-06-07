package com.gnu_graduate_project_team.junggal_v2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface EggInterface {

    /** 달걀이 채팅 요청 **/
    @POST("/egg/request_chat")
    Call<EggAnswerVO> egg_chat(@Body EggchatVO chat);
    
    /** 달걀이 slot 채우기 요청  --- 추가 작업 예상 **/
//    @POST("/egg/fill_slot")
//    Call<EggAnswerVO> ()
}
