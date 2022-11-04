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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup;

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

public class MypageBusinessSellerRegistActivity extends Activity{

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
    private String useBank;
    private String accountNumber;
    private Integer share_icon_number;
    private SharePostGeoInfo sharePostGeoInfo = null;

    /** return result intent **/
    private static Integer share_geo_region = 1;
    private static Integer share_icon = 2;


    /** request Data **/
    private Map<String, RequestBody> storeInfo;
    private BusinessManVO businessManData;


    /** XML 관련 변수 **/
    private ImageView store_image;
    private EditText store_name;
    private EditText store_open_time;
    private EditText business_seller_id;
    private EditText business_open_date;
    private TextView store_position;
    private Button business_seller_submit_btn;
    private MultiLineRadioGroup multi_line_radio_group;
    private EditText account_Number;
    private TextView store_marker;

    /** 라디오 버튼 관련 flag **/
    private Boolean firstFlag = false;
    private Boolean secondFlag = false;

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiSellerInterface apiSellerInterface = retrofit.create(ApiSellerInterface.class);

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

        /** 사업자 관련 초기화 **/
        storeInfo = new HashMap<>();
        businessManData = new BusinessManVO();
        
        /** XML 초기화 **/
        store_image = findViewById(R.id.store_image);
        store_name = findViewById(R.id.store_name);
        store_position = findViewById(R.id.store_position);
        store_open_time = findViewById(R.id.store_open_time);
        business_seller_id = findViewById(R.id.business_seller_id);
        business_open_date = findViewById(R.id.business_open_date);
        business_seller_submit_btn = (Button) findViewById(R.id.business_seller_submit_btn);
        multi_line_radio_group = findViewById(R.id.multi_line_radio_group);
        account_Number = findViewById(R.id.accountNumber);
        store_marker = findViewById(R.id.store_marker);


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

        /** 라디오 버튼 클릭 리스너 **/
        multi_line_radio_group.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ViewGroup group, RadioButton button) {
                useBank = button.getText().toString();
            }
        });

        store_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MypageBusinessSellerRegistActivity.this,SharePostIconActivity.class);
                startActivityForResult(intent1, share_icon);
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
                accountNumber = account_Number.getText().toString().trim();

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
                else if(share_icon_number==null)
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "나눔 마커를 설정해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(storeOpenTime.equals(""))
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "판매 시간을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(businessSellerId.equals(""))
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "사업자 번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(businessOpenDate.equals(""))
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "개업 일자를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(useBank.equals(""))
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "사용 은행을 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(accountNumber.equals(""))
                {
                    Toast.makeText(MypageBusinessSellerRegistActivity.this, "계좌 번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    RequestBody store_name = RequestBody.create(MediaType.parse("text/plain"),storeName);
                    RequestBody share_position = RequestBody.create(MediaType.parse("text/plain"),sharePostGeoInfo.getLatLng().longitude+" " + sharePostGeoInfo.getLatLng().latitude);
                    RequestBody store_icon = RequestBody.create(MediaType.parse("text/plain"),share_icon_number.toString());
                    RequestBody store_open_time = RequestBody.create(MediaType.parse("text/plain"),storeOpenTime);
                    RequestBody business_id = RequestBody.create(MediaType.parse("text/plain"),businessSellerId);
                    RequestBody opening_date = RequestBody.create(MediaType.parse("text/plain"),businessOpenDate);
                    RequestBody use_bank = RequestBody.create(MediaType.parse("text/plain"),useBank);
                    RequestBody account = RequestBody.create(MediaType.parse("text/plain"),accountNumber);
                    RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),userData.getId());
                    RequestBody user_name = RequestBody.create(MediaType.parse("text/plain"),userData.getReal_name());

                    storeInfo.put("store_name",store_name);
                    storeInfo.put("share_position",share_position);
                    storeInfo.put("store_icon",store_icon);
                    storeInfo.put("store_open_time",store_open_time);
                    storeInfo.put("business_id",business_id);
                    storeInfo.put("opening_date",opening_date);
                    storeInfo.put("use_bank",use_bank);
                    storeInfo.put("account",account);
                    storeInfo.put("user_id",user_id);
                    storeInfo.put("user_name",user_name);

                    MultipartBody.Part store_image = MultipartBody.Part.createFormData("store_image","store_image",requestBody_store_image);

                    Call<BusinessManVO> call = apiSellerInterface.registSeller(storeInfo,store_image);
                    call.enqueue(new Callback<BusinessManVO>() {
                        @Override
                        public void onResponse(Call<BusinessManVO> call, Response<BusinessManVO> response) {

                        }

                        @Override
                        public void onFailure(Call<BusinessManVO> call, Throwable t) {

                        }
                    });
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
        else if( requestCode == share_icon)
        {
            share_icon_number = data.getIntExtra("sharePostIconNumber",9999);
            Log.d("sharePostIconNumber",share_icon_number.toString());
        }
        else if( requestCode == 9999)
        {
            Log.d("Backpress",":9999");
        }
    }
}
