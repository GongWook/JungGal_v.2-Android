package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MypageSellerChoooseActivity extends Activity {

    /** XML 관련 변수 **/
    private ImageView sellerChooseBackBtn;
    private Button individual_seller_btn;
    private Button business_seller_btn;

    /** User 관련 변수 **/
    private UserVO userData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mypage_seller_choose_acitivty);

        /** XML 초기화 **/
        sellerChooseBackBtn = (ImageView)findViewById(R.id.sellerChooseBackBtn);
        individual_seller_btn = (Button) findViewById(R.id.individual_seller_btn);
        business_seller_btn = (Button) findViewById(R.id.business_seller_btn);

        /** User Data 받아오기 **/
        Intent intent = getIntent();
        userData = (UserVO) intent.getSerializableExtra("userData");
        
        /** 뒤로가기 버튼 클릭 리스너 **/
        sellerChooseBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        /** 개인 판매자 등록 클릭 리스너 **/
        individual_seller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MypageSellerChoooseActivity.this, "서비스 준비중 입니다.", Toast.LENGTH_SHORT).show();
                
            }
        });
    
        /** 사업자 판매자 등록 클릭 리스너 **/
        business_seller_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent businessSellerRegistIntent= new Intent(MypageSellerChoooseActivity.this, MypageBusinessSellerRegistActivity.class);
                businessSellerRegistIntent.putExtra("userData",userData);
                startActivity(businessSellerRegistIntent);
            }
        });

    }
}
