package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistActivity extends Activity {

    private EditText regist_email;
    private EditText regist_pw;
    private EditText regist_check;
    private EditText regist_name;
    private EditText regist_introduce;
    private ImageView regist_profile;
    private Button regist_profile_button;
    private Button regist_button;

    private String user_email;
    private String user_pw;
    private String check_pw;
    private String user_name;
    private String user_introduce;
    private String user_phone_number;
    private Bitmap user_profile;
    private Uri user_profile_uri;
    private RequestBody requestBody_user_profile = null;

    private Map<String, RequestBody> user_info;
    private UserVO userData;

       //private ~~~~ user_profile

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.regist_form_activity);

        Retrofit retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Intent intent=getIntent();
        user_phone_number = intent.getStringExtra("user_phone_number");


        regist_email = (EditText) findViewById(R.id.regist_email);
        regist_pw = (EditText) findViewById(R.id.regist_password);
        regist_check = (EditText) findViewById(R.id.regist_checkpw);
        regist_name = (EditText) findViewById(R.id.regist_name);
        regist_introduce = (EditText) findViewById(R.id.regist_introduce);
        regist_profile = (ImageView) findViewById(R.id.regist_profile);
        regist_profile_button = (Button) findViewById(R.id.regist_profile_button);
        regist_button = (Button) findViewById(R.id.regist_button);

        user_info = new HashMap<>();

        // 회원 프로필 등록
        regist_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                //intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 0);
            }
        });
        
        // 회원 등록
        regist_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserVO user = new UserVO();

                user_email = regist_email.getText().toString().trim();
                user_pw = regist_pw.getText().toString().trim();
                check_pw = regist_check.getText().toString().trim();
                user_name =regist_name.getText().toString().trim();
                user_introduce = regist_introduce.getText().toString().trim();

                if(user_email.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(user_pw.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(check_pw.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"비밀번호를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!user_pw.equals(check_pw))
                {
                    Toast.makeText(getApplicationContext(),"비밀번호가 동일하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(user_name.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"사용하실 이름을 적어주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(user_introduce.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"간단한 자기소개를 적어주세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestBody id = RequestBody.create(MediaType.parse("text/plain"),user_email );
                    RequestBody pw = RequestBody.create(MediaType.parse("text/plain"),user_pw );
                    RequestBody name = RequestBody.create(MediaType.parse("text/plain"),user_name );
                    RequestBody phone_num = RequestBody.create(MediaType.parse("text/plain"),user_phone_number );
                    RequestBody introduce = RequestBody.create(MediaType.parse("text/plain"),user_introduce );

                    user_info.put("id", id);
                    user_info.put("pw", pw);
                    user_info.put("name", name);
                    user_info.put("phone_number", phone_num);
                    user_info.put("introduce", introduce);

                    userData = new UserVO();
                    userData.setId(user_email);
                    userData.setPw(user_pw);
                    userData.setName(user_name);
                    userData.setPhone_number(user_phone_number);
                    userData.setIntroduce(user_introduce);



                    //profile
                    if(requestBody_user_profile!=null)
                    {
                        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("profile","profile",requestBody_user_profile);
                        RequestBody profile_flag = RequestBody.create(MediaType.parse("text/plain"),"true" );
                        user_info.put("profile_flag", profile_flag);

                        Call<UserVO> call = apiInterface.registUser(user_info, uploadFile);

                        call.enqueue(new Callback<UserVO>() {
                            @Override
                            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                                UserVO checkuser = response.body();
                                Log.d("test성공",checkuser.getId());
                                if(checkuser.getId().equals("null"))
                                {
                                    Toast.makeText(getApplicationContext(),"등록된 Email 입니다.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    userData.setProfile_flag(true);
                                    Regist_Success(userData);
                                    Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                                    startActivity(intent);

                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserVO> call, Throwable t) {
                                Log.d("test실패",t.getMessage());
                                Toast.makeText(getApplicationContext(),"가입 실패.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Call<UserVO> call = apiInterface.registUser_nonProfile(user_info);

                        call.enqueue(new Callback<UserVO>() {
                            @Override
                            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                                UserVO checkuser = response.body();
                                Log.d("test성공",checkuser.getId());
                                if(checkuser.getId().equals("null"))
                                {
                                    Toast.makeText(getApplicationContext(),"등록된 Email 입니다.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    userData.setProfile_flag(false);
                                    Regist_Success(userData);
                                    Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<UserVO> call, Throwable t) {
                                Log.d("test실패",t.getMessage());
                                Toast.makeText(getApplicationContext(),"가입 실패.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }




                    /**
                    Call<UserVO> call = apiInterface.registUser(user);

                    call.enqueue(new Callback<UserVO>() {
                        @Override
                        public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                            UserVO checkuser = response.body();
                            Log.d("test성공",checkuser.getId());
                            if(checkuser.getId().equals("null"))
                            {
                                Toast.makeText(getApplicationContext(),"등록된 Email 입니다.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"가입 성공.", Toast.LENGTH_SHORT).show();
                                PreferenceManager.setBoolean(RegistActivity.this, "first_check_flag",true);
                                //Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                            }
                        }

                        @Override
                        public void onFailure(Call<UserVO> call, Throwable t) {
                            Log.d("test실패",t.getMessage());
                            Toast.makeText(getApplicationContext(),"가입 실패.", Toast.LENGTH_SHORT).show();
                        }
                    });

                     */

                }


            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                user_profile_uri = data.getData();
                Glide.with(getApplicationContext()).load(data.getData()).override(500,500).into(regist_profile);
                Log.d("이미지 경로 : ", data.getData().toString());

                File file = new File(String.valueOf(user_profile_uri));
                InputStream inputStream = null;

                try
                {
                    inputStream = getApplicationContext().getContentResolver().openInputStream(user_profile_uri);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

                user_profile = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                user_profile.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

                requestBody_user_profile = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray());

                /**
                Glide.with(getApplicationContext()).asBitmap().load(data.getData()).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        user_profile = resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

                 */


            }
        }
    }

    private void Regist_Success(UserVO uservo)
    {
        Toast.makeText(getApplicationContext(),"가입 성공.", Toast.LENGTH_SHORT).show();
        PreferenceManager.setBoolean(RegistActivity.this, "first_check_flag",true);
        PreferenceManager.setString(RegistActivity.this,"user_id", user_email);
        PreferenceManager.setString(RegistActivity.this,"user_pw",user_pw);
        PreferenceManager.setString(RegistActivity.this,"user_name", user_name);

        MyApplication.user_data = uservo;

    }


}
