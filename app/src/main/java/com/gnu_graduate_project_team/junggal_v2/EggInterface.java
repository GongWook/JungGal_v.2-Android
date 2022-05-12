package com.gnu_graduate_project_team.junggal_v2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface EggInterface {

    /** 달걀이 채팅하기 **/
    @POST("/egg/chat")
    Call<EggAnswerVO> egg_chat(@Body EggchatVO chat);
}
