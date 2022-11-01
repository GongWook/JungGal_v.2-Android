package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MypageProfileUpdateActivity extends Activity {

    /** User 관련 변수 **/
    private UserVO userData;
    private Uri user_profile_uri;
    private Bitmap user_profile;
    private byte[] user_profile_byte;
    private RequestBody requestBody_user_profile = null;

    /** XML 관련 변수 **/
    private ImageView mypageUpdateProfile;
    private EditText myPageUpdateName;
    private EditText myPageUpdateIntroduce;
    private Button myPageUpdateProfileBtn;
    private Button myProfileUpdateSubmitBtn;
    private Button myPageProfileResetBtn;

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);
    private Map<String, RequestBody> user_info;

    /** Thread 사용 **/
    Handler mHandler = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mypage_profile_update_activity);

        /** Thread 관련 **/
        mHandler = new Handler();

        /** User 관련 **/
        Intent intent = getIntent();
        userData = (UserVO) intent.getSerializableExtra("userData");
        user_profile_byte = intent.getByteArrayExtra("userImage");
        user_info = new HashMap<>();
        Log.d("userData test",userData.toString());

        /** XML 초기화 **/
        mypageUpdateProfile = (ImageView) findViewById(R.id.mypageUpdateProfile);
        myPageUpdateName = (EditText) findViewById(R.id.myPageUpdateName);
        myPageUpdateProfileBtn = (Button) findViewById(R.id.myPageUpdateProfileBtn);
        myPageUpdateIntroduce = (EditText) findViewById(R.id.myPageUpdateIntroduce);
        myProfileUpdateSubmitBtn = (Button) findViewById(R.id.myProfileUpdateSubmitBtn);
        myPageProfileResetBtn = (Button) findViewById(R.id.myPageProfileResetBtn);

        /** 로직 흐름 **/
        uiSetting();

        /** 프로필 변경 버튼 이벤트 리스너 **/
        myPageUpdateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 0);
            }
        });

        /** 프로필 사용 안함 버튼 에빈트 리스너 **/
        myPageProfileResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileReset();
                userData.setProfile_change_flag(true);
                userData.setProfile_flag(false);
            }
        });

        /** 프로필 수정 submit 버튼 이벤트 리스너 **/
        myProfileUpdateSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userData.setIntroduce(myPageUpdateIntroduce.getText().toString().trim());

                //RequestBody name = RequestBody.create(MediaType.parse("text/plain"),user_name );
                RequestBody id = RequestBody.create(MediaType.parse("text/plain"),userData.getId() );
                RequestBody introduce = RequestBody.create(MediaType.parse("text/plain"), userData.getIntroduce() );


                //user_info.put("name", name);
                user_info.put("id", id);
                user_info.put("introduce", introduce);

                //profile
                if(userData.getProfile_change_flag()!=null && userData.getProfile_change_flag()==true && userData.getProfile_flag()==true)
                {
                    MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("profile","profile",requestBody_user_profile);
                    RequestBody profile_flag = RequestBody.create(MediaType.parse("text/plain"),"true" );
                    RequestBody profile_change_flag = RequestBody.create(MediaType.parse("text/plain"),"true" );
                    user_info.put("profile_flag", profile_flag);
                    user_info.put("profile_change_flag", profile_change_flag);

                    Call<UserVO> call = apiInterface.updateUserProfile(user_info, uploadFile);

                    call.enqueue(new Callback<UserVO>() {
                        @Override
                        public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                            UserVO checkuser = response.body();
                            Log.d("test성공",checkuser.getId());
                            Toast.makeText(getApplicationContext(),"프로필 수정 성공.", Toast.LENGTH_SHORT).show();

                            finish();
                        }

                        @Override
                        public void onFailure(Call<UserVO> call, Throwable t) {
                            Log.d("test실패",t.getMessage());
                            Toast.makeText(getApplicationContext(),"수정 실패.", Toast.LENGTH_SHORT).show();

                            finish();
                        }
                    });
                }
                else if(userData.getProfile_flag()==true)
                {
                    RequestBody profile_flag = RequestBody.create(MediaType.parse("text/plain"),"true" );
                    user_info.put("profile_flag", profile_flag);

                    Call<UserVO> call = apiInterface.updateUser_nonProfile(user_info);

                    call.enqueue(new Callback<UserVO>() {
                        @Override
                        public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                            UserVO checkuser = response.body();
                            Toast.makeText(getApplicationContext(),"프로필 수정 성공.", Toast.LENGTH_SHORT).show();


                        }

                        @Override
                        public void onFailure(Call<UserVO> call, Throwable t) {
                            Log.d("test실패",t.getMessage());
                            Toast.makeText(getApplicationContext(),"수정 실패.", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else
                {
                    RequestBody profile_flag = RequestBody.create(MediaType.parse("text/plain"),"false" );
                    user_info.put("profile_flag", profile_flag);

                    Call<UserVO> call = apiInterface.updateUser_nonProfile(user_info);

                    call.enqueue(new Callback<UserVO>() {
                        @Override
                        public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                            UserVO checkuser = response.body();
                            Toast.makeText(getApplicationContext(),"프로필 수정 성공.", Toast.LENGTH_SHORT).show();



                        }

                        @Override
                        public void onFailure(Call<UserVO> call, Throwable t) {
                            Log.d("test실패",t.getMessage());
                            Toast.makeText(getApplicationContext(),"수정 실패.", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

                intent.putExtra("userData", userData);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        /** 로직 흐름 **/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                user_profile_uri = data.getData();
                Glide.with(getApplicationContext()).load(data.getData()).override(500,500).into(mypageUpdateProfile);

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
                userData.setProfile_flag(true);
                userData.setProfile_change_flag(true);
            }
        }
    }

    public void uiSetting()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(userData.getProfile_flag()==true)
                        {
                            user_profile = BitmapFactory.decodeByteArray(user_profile_byte,0,user_profile_byte.length);
                            mypageUpdateProfile.setImageBitmap(user_profile);
                        }
                        myPageUpdateName.setText(userData.getName());
                        myPageUpdateIntroduce.setText(userData.getIntroduce());

                    }
                });
            }
        });

        t.start();

    }

    public void profileReset()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mypageUpdateProfile.setImageResource(R.drawable.man);

                    }
                });
            }
        });

        t.start();
    }
}
