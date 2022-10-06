package com.gnu_graduate_project_team.junggal_v2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiFcmInterface {

    /** FCM Token값 저장하기 **/
    @POST("fcm/register")
    Call<FcmTokenVO> tokenRegist(@Body FcmTokenVO tokenVO);

    /** FcmApplyMessage (반찬 나눔 신청 메시지) 전송 **/
    @POST("fcm/applyfor")
    Call<FcmApplyMessage> sharePostApply(@Body FcmApplyMessage fcmApplyMessage);

    //test
}
