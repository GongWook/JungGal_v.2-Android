package com.gnu_graduate_project_team.junggal_v2;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiSellerInterface {

    /**회원가입 정보 보내기 profile 사진 존재시 **/
    @Multipart
    @POST("seller/register")
    Call<BusinessManVO> registSeller(@PartMap Map<String, RequestBody> params, @Part MultipartBody.Part file);
}
