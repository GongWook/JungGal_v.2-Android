package com.gnu_graduate_project_team.junggal_v2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegistActivity_Test extends AppCompatActivity {

    private EditText user_id;
    private EditText user_pw;
    private EditText user_name;
    private EditText user_phone;
    private EditText user_introduce;
    private Button regist_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_test);

        Retrofit retrofit = ApiClient.getApiClient();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        user_id = (EditText) findViewById(R.id.user_id);
        user_pw = (EditText) findViewById(R.id.user_pw);
        user_name = (EditText) findViewById(R.id.user_name);
        user_phone = (EditText) findViewById(R.id.user_phone);
        user_introduce = (EditText) findViewById(R.id.user_introduce);
        regist_btn = (Button) findViewById(R.id.regist_btn);

        regist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = user_id.getText().toString().trim();
                String userPw = user_pw.getText().toString().trim();
                String userName = user_name.getText().toString().trim();
                String userPhone = user_phone.getText().toString().trim();
                String userIntroduce = user_introduce.getText().toString().trim();

                UserVO userVO = new UserVO();
                userVO.setId(userId);
                userVO.setPw(userPw);
                userVO.setName(userName);
                userVO.setPhone_number(userPhone);
                userVO.setIntroduce(userIntroduce);

                Call<UserVO> call = apiInterface.registUser(userVO);

                call.enqueue(new Callback<UserVO>() {
                    @Override
                    public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                        UserVO checkuser = response.body();
                        Log.d("test성공",checkuser.getId());
                    }

                    @Override
                    public void onFailure(Call<UserVO> call, Throwable t) {
                        Log.d("test실패",t.getMessage());
                    }
                });


            }
        });

    }
}
