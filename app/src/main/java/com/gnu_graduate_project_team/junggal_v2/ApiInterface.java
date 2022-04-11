package com.gnu_graduate_project_team.junggal_v2;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

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
    @Multipart
    @POST("user/register")
    Call<UserVO> registUser(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    @Multipart
    @POST("user/register")
    Call<UserVO> registUser_nonProfile(@PartMap Map<String, RequestBody> params);
}
