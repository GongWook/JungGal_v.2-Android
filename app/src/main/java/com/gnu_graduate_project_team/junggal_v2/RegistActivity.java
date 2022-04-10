package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

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
    //private ~~~~ user_profile

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.regist_form_activity);

        Retrofit retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        regist_email = (EditText) findViewById(R.id.regist_email);
        regist_pw = (EditText) findViewById(R.id.regist_password);
        regist_check = (EditText) findViewById(R.id.regist_checkpw);
        regist_name = (EditText) findViewById(R.id.regist_name);
        regist_introduce = (EditText) findViewById(R.id.regist_introduce);
        regist_profile = (ImageView) findViewById(R.id.regist_profile);
        regist_profile_button = (Button) findViewById(R.id.regist_profile_button);
        regist_button = (Button) findViewById(R.id.regist_button);

        // 회원 프로필 등록
        regist_profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("imagle/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 0);;
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

                    user.setId(user_email);
                    user.setPw(user_pw);
                    user.setName(user_name);
                    user.setIntroduce(user_email);
                    //앞 인증 후 코드 수정하기
                    user.setPhone_number("000-0000-0000");
                    //프로필 setting도 수정하기

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


                }


            }
        });



    }



}
