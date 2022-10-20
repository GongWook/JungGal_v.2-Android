package com.gnu_graduate_project_team.junggal_v2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiChatInterface {

    /** 채팅방 존재 여부 확인 **/
    @POST("chat/selectChatRoom")
    Call<ChatRoomVO> selectChatRoom(@Body ChatRoomVO chatRoomVO);

    /** 채팅방 List 조회 **/
    @POST("chat/chatRoomListSelect")
    Call<List<ChatRoomVO>> chatRoomListSelect(@Body UserVO userVO);

    /** 채팅방 생성 **/
    @POST("chat/insertChatRoom")
    Call<ChatRoomVO> insertChatRoom(@Body ChatRoomVO chatRoomVO);

    /** 채팅 등록 **/
    @POST("chat/insertChat")
    Call<ChatVO> insertChat(@Body ChatVO chatVO);

    /** 채팅 내역 조회 **/
    @POST("chat/selectChatList")
    Call<List<ChatVO>> selectChatList(@Body ChatRoomVO chatRoomVO);


}
