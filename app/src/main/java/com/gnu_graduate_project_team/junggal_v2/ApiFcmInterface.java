package com.gnu_graduate_project_team.junggal_v2;

import java.util.List;

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
    
    /** request Alarm 조회 **/
    @POST("fcm/requestAlarmSelect")
    Call<List<AlarmVO>> requestAlarmSelect(@Body UserVO user);

    /** request Alarm Accept **/
    @POST("fcm/requestAlarmAccept")
    Call<AlarmVO> requestAlarmAccept(@Body AlarmVO alarmVO);

    /** request Alarm Deny **/
    @POST("fcm/requestAlarmDeny")
    Call<AlarmVO> requestAlarmDeny(@Body AlarmVO alarmVO);

    /** request Alarm Deny **/
    @POST("fcm/successShare")
    Call<AlarmVO> successShare(@Body AlarmVO alarmVO);

    /** response Alarm 조회 **/
    @POST("fcm/responseAlarmSelect")
    Call<List<AlarmVO>> responseAlarmSelect(@Body UserVO user);

    /** Alarm Delete **/
    @POST("fcm/delete_alarm")
    Call<AlarmVO> delete_alarm(@Body AlarmVO alarmVO);



    //test
}
