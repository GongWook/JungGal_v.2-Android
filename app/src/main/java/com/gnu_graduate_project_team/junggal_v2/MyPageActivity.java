package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MyPageActivity extends Activity {

    /** user정보와 관련된 변수 **/
    private UserVO userData;
    private Bitmap bitmap;
    private byte[] tmp;

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiPostInterface apiPostInterface = retrofit.create(ApiPostInterface.class);

    /** XML 변수 **/
    private TextView myPageName;
    private TextView myPagePoint;
    private ImageView sellerBadge;
    private ImageView myPageUserProfile;
    private ImageView myPageBackBtn;
    private Button myPageChangeProfileBtn;
    private Button sellerRegistBtn;

    /** Thread 사용 **/
    Handler mHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mypage_activity);

        /** Thread 관련 **/
        mHandler = new Handler();

        /** XML 변수 초기화 **/
        myPageName = (TextView) findViewById(R.id.myPageName);
        myPagePoint = (TextView) findViewById(R.id.myPagePoint);
        sellerBadge = (ImageView) findViewById(R.id.sellerBadge);
        myPageUserProfile = (ImageView) findViewById(R.id.myPageUserProfile);
        myPageChangeProfileBtn = (Button) findViewById(R.id.myPageChangeProfileBtn);
        sellerRegistBtn = (Button) findViewById(R.id.sellerRegistBtn);
        myPageBackBtn = (ImageView) findViewById(R.id.myPageBackBtn);

        
        /** user Data get **/
        Intent intent = getIntent();
        userData = (UserVO) intent.getSerializableExtra("userData");


        /** 뒤로가기 버튼 이벤트 리스너 **/
        myPageBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /** 판매자 등록 버튼 **/
        sellerRegistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userData.getSeller_auth()==false)
                {
                    Intent sellerRegistIntent = new Intent(MyPageActivity.this, MypageSellerChoooseActivity.class);
                    sellerRegistIntent.putExtra("userData",userData);
                    startActivity(sellerRegistIntent);
                }
                else
                {
                    Toast.makeText(MyPageActivity.this, "이미 등록된 사용자 입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiSetting();

        /** 프로필 수정하기 Click 이벤트 리스너 **/
        myPageChangeProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileUpdateIntent = new Intent(MyPageActivity.this, MypageProfileUpdateActivity.class);
                profileUpdateIntent.putExtra("userData",userData);
                profileUpdateIntent.putExtra("userImage",tmp);
                startActivityForResult(profileUpdateIntent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK)
        {
            userData = (UserVO) data.getSerializableExtra("userData");
            onResume();
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

                        myPageName.setText(userData.getName());
                        myPagePoint.setText(userData.getShare_point().toString());
                        userGetProfile();

                        //sellerBadge여부
                        if(userData.getSeller_auth())
                        {
                            sellerBadge.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        t.start();
    }

    /** 게시글 작성자 서버에서 Select **/
    public void userGetProfile()
    {

        Call<UserVO> call = apiPostInterface.selectWriter(userData);
        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                UserVO userVO = response.body();

                if(userVO.getProfile_flag()==true)
                {
                    tmp = Base64.decode(userVO.getImagedata(),Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(tmp,0,tmp.length);
                    myPageUserProfile.setImageBitmap(bitmap);
                }
                else
                {
                    myPageUserProfile.setImageResource(R.drawable.man);
                }


            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable t) {
                Toast.makeText(MyPageActivity.this, "작성자 사진 로드 실패\n네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
