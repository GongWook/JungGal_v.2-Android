package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.lang.UScript;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MypageBusinessSellerRegistActivity extends Activity {

    /** userData **/
    private UserVO userData;

    /** storeData**/
    private Uri store_image_uri;
    private Bitmap store_image_bitmap;
    private RequestBody requestBody_store_image = null;
    private String storeName;
    private String storeOpenTime;
    private String businessSellerId;
    private String businessOpenDate;
    private static Integer share_geo_region = 1;

    /** 가게 위치 및 좌표 **/
    private SharePostGeoInfo sharePostGeoInfo = null;

    /** XML 관련 변수 **/
    private ImageView store_image;
    private EditText store_name;
    private EditText store_open_time;
    private EditText business_seller_id;
    private EditText business_open_date;
    private TextView store_position;
    private Button business_seller_submit_btn;

    /** Thread 사용 **/
    Handler mHandler = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mypage_business_seller_regist_acitivty);

        /** Thread 사용 **/
        mHandler = new Handler();

        /** UserData 초기화 **/
        Intent intent = getIntent();
        userData = (UserVO) intent.getSerializableExtra("userData");
        
        /** XML 초기화 **/
        store_image = findViewById(R.id.store_image);
        store_name = findViewById(R.id.store_name);
        store_open_time = findViewById(R.id.store_open_time);
        business_seller_id = findViewById(R.id.business_seller_id);
        business_seller_submit_btn = (Button) findViewById(R.id.business_seller_submit_btn);


        /** 가게 상표 이미지 클릭 리스너 **/
        store_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 0);
            }
        });

        /** 반찬 판매 위치 등록 클릭 리스너 **/
        store_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MypageBusinessSellerRegistActivity.this,SharePostlocationActivity.class);
                startActivityForResult(intent, share_geo_region);
            }
        });

        /** 등록하기 버튼 클릭 리스너 **/
        business_seller_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                storeName = store_name.getText().toString().trim();
                storeOpenTime = store_open_time.getText().toString().trim();
                businessSellerId = business_seller_id.getText().toString().trim();
                businessOpenDate = business_open_date.getText().toString().trim();

                if(requestBody_store_image==null)
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "상표를 업로드 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(storeName.equals(""))
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "판매 상호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(sharePostGeoInfo==null)
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "가게 위치를 설정해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(storeOpenTime.equals(""))
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "판매 시간을 설정해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(businessSellerId.equals(""))
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "사업자 번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(businessOpenDate.equals(""))
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "개업 일자를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0)
        {
            if(requestCode == 0)
            {
                if (resultCode == RESULT_OK)
                {
                    store_image_uri = data.getData();
                    Glide.with(getApplicationContext()).load(data.getData()).override(500,500).into(store_image);

                    File file = new File(String.valueOf(store_image_uri));
                    InputStream inputStream = null;

                    try
                    {
                        inputStream = getApplicationContext().getContentResolver().openInputStream(store_image_uri);
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                    store_image_bitmap = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    store_image_bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);

                    requestBody_store_image = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray());


                }
            }
        }
        else if(requestCode==share_geo_region)
        {
            if(data.getParcelableExtra("sharePostGeoInfo")!=null)
            {
                sharePostGeoInfo = data.getParcelableExtra("sharePostGeoInfo");
                Log.d("shareRegion",sharePostGeoInfo.getGeoRegion());
                Log.d("sharegeoPoint",sharePostGeoInfo.getLatLng().toString());
            }
        }
        else
        {
            
        }
    }
}
