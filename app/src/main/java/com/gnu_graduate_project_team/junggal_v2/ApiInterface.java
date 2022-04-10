package com.gnu_graduate_project_team.junggal_v2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    /*


    @FormUrlEncoded
    @POST("user/register")
    Call<String> registUser(
            @Field("id") String id,
            @Field("pw") String pw,
            @Field("name") String name,
            @Field("phone_number") String phone_number,
            @Field("introduce") String introduce,
            @Field("share_point") Double share_point
    );
*/

    //회원가입 정보 보내기
    @POST("user/register")
    Call<UserVO> registUser(@Body UserVO userVO);
}
