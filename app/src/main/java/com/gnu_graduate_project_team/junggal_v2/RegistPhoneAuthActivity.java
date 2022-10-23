package com.gnu_graduate_project_team.junggal_v2;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistPhoneAuthActivity extends Activity {

    private EditText phoneNum;
    private EditText authNum;
    private EditText real_name;
    private Button auth_btn;
    private Button auht_pass_btn;

    private String phone_num;
    private String userRealNmae;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.regist_phone_auth_activity);

        Retrofit retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        phoneNum = (EditText) findViewById(R.id.phone_num);
        authNum = (EditText) findViewById(R.id.auth_num);
        real_name = (EditText) findViewById(R.id.real_name);
        auth_btn = (Button) findViewById(R.id.auth_get_button);
        auht_pass_btn = (Button) findViewById(R.id.auth_next_button);

        // 인증 번호 받는 버튼//
        auth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                phone_num = phoneNum.getText().toString().trim();
                userRealNmae = real_name.getText().toString().trim();
                if(phone_num.equals(""))
                {
                    Toast.makeText(RegistPhoneAuthActivity.this, "휴대폰 번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(userRealNmae.equals(""))
                {
                    Toast.makeText(RegistPhoneAuthActivity.this, "이름을 정자로 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    PhoneVO phone_info = new PhoneVO();
                    phone_info.setPhone_number(phone_num);

                    Call<PhoneVO> call = apiInterface.phone_auth(phone_info);
                    call.enqueue(new Callback<PhoneVO>() {
                        @Override
                        public void onResponse(Call<PhoneVO> call, Response<PhoneVO> response) {
                            Toast.makeText(RegistPhoneAuthActivity.this,"인증번호 전송 성공",Toast.LENGTH_SHORT).show();
                            auth_btn.setEnabled(false);
                            phoneNum.setEnabled(false);
                        }

                        @Override
                        public void onFailure(Call<PhoneVO> call, Throwable t) {
                            Toast.makeText(RegistPhoneAuthActivity.this,"네트워크를 확인해 주세요.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });


        auht_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneVO phone_info = new PhoneVO();

                String phone_num = phoneNum.getText().toString().trim();
                String auth_num = authNum.getText().toString().trim();

                phone_info.setPhone_number(phone_num);
                phone_info.setAuth_number(auth_num);

                Call<PhoneVO> call = apiInterface.phone_auth_pass(phone_info);
                call.enqueue(new Callback<PhoneVO>() {
                    @Override
                    public void onResponse(Call<PhoneVO> call, Response<PhoneVO> response) {
                        PhoneVO auth_state = response.body();
                        if(auth_state.getSuccess_flag())
                        {
                            Intent intent = new Intent(RegistPhoneAuthActivity.this, RegistActivity.class);
                            intent.putExtra("user_phone_number",phone_num);
                            intent.putExtra("real_name",userRealNmae);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(RegistPhoneAuthActivity.this,"인증번호를 확인해 주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PhoneVO> call, Throwable t) {
                        Toast.makeText(RegistPhoneAuthActivity.this,"네트워크를 확인해 주세요.",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });




    }
}
