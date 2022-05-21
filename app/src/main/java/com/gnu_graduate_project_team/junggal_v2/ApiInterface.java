package com.gnu_graduate_project_team.junggal_v2;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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

    /**회원가입 정보 보내기 profile 사진 존재시 **/
    @Multipart
    @POST("user/register")
    Call<UserVO> registUser(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);

    /**회원가입 정보 보내기 profile 사진 미존재시 **/
    @Multipart
    @POST("user/register")
    Call<UserVO> registUser_nonProfile(@PartMap Map<String, RequestBody> params);

    /** 로그인 정보 보내기 **/
    @POST("user/login")
    Call<UserVO> login(@Body UserVO user);

    /** 휴대폰 인증 번호 보내기 **/
    @POST("user/auth")
    Call<PhoneVO> phone_auth(@Body PhoneVO phone);

    /** 인증번호 인증하기 **/
    @POST("user/authPass")
    Call<PhoneVO> phone_auth_pass(@Body PhoneVO phone);
}
