package com.gnu_graduate_project_team.junggal_v2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.Coord;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SharePostlocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText geotext;
    private TextView geo_submit;

    /** 사용자 반찬 나눔 위치 **/
    private String region;

    /** 사용자 위치 받아오는 변수 **/
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    /** 반찬 나눔 위치 마커 정보 변수 **/
    private Marker marker = new Marker();
    private LatLng share_latLng = null;
    private Boolean share_latLng_flag = false;

    /** Thread 생성 **/
    Handler mHandler = null;

    /** ReverseGeoCoding API KEY **/
    String APIKEY_ID = "kzaooxgd1b";
    String APIKEY = "CJ4q3kUcNv1DzmKhqzNc4QByJ3516r0H0r0gmz1b";

    /** Retrofit **/
    Retrofit retrofit = NaverApiClient.getNaverApiClient();
    ReverseGeocodingInterface reverseGeocodingInterface = retrofit.create(ReverseGeocodingInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_post_location_activity);

        geotext = (EditText) findViewById(R.id.geo_region);
        geo_submit = (TextView) findViewById(R.id.geo_region_submit);

        mHandler = new Handler();


        /** 사용자 위치  **/
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


        /** 네이버 지도 xml과 연동  **/
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.share_post_map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.share_post_map, mapFragment).commit();
        }

        /** 네이버 지도 api 호출 **/
        mapFragment.getMapAsync(this);

        /** 완료 버튼 클릭 이벤트 리스너 **/
        geo_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(share_latLng_flag==false)
                {
                    Toast.makeText(SharePostlocationActivity.this, "나눔할 위치를 더블클릭 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Intent data = new Intent(SharePostlocationActivity.this, SharePostWriteActivity.class);
                    Intent data = new Intent();
                    SharePostGeoInfo sharePostGeoInfo = new SharePostGeoInfo(region,share_latLng);
                    data.putExtra("sharePostGeoInfo", sharePostGeoInfo);
                    setResult(2, data);
                    finish();
                }


            }
        });


    }

    /** 사용자 위치  **/
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    /** 네이버 지도 api 호출 함수 **/

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        naverMap.setIndoorEnabled(true);

        /** 네이버 지도 더블클릭 이벤트 **/
        naverMap.setOnMapDoubleTapListener((pointF, latLng) -> {

            if(share_latLng !=null)
            {
                marker.setMap(null);
            }
            
            //위치 파악을 위한 toast 메시지 tmp
            //Toast.makeText(this,latLng.latitude+", " + latLng.longitude, Toast.LENGTH_SHORT).show();

            marker.setPosition(latLng);
            marker.setMap(naverMap);
            share_latLng = latLng;
            share_latLng_flag = true;
            String coord = latLng.longitude +","+ latLng.latitude ;

            Call<ReverseGeocoding> call = reverseGeocodingInterface.getGeo(APIKEY_ID,APIKEY, coord,"epsg:4326", "addr","json");

            call.enqueue(new Callback<ReverseGeocoding>() {
                @Override
                public void onResponse(Call<ReverseGeocoding> call, Response<ReverseGeocoding> response) {

                    ReverseGeocoding tmp = response.body();
                    Result result= tmp.results.get(0);

                    if(result.getLand().getNumber2().isEmpty())
                    {
                        region = result.getRegion().getArea1().getName() + " " +
                                result.getRegion().getArea2().getName() + " " +
                                result.getRegion().getArea3().getName() + " " +
                                result.getRegion().getArea4().getName() +
                                result.getLand().getNumber1();
                    }
                    else
                    {
                        region = result.getRegion().getArea1().getName() + " " +
                                result.getRegion().getArea2().getName() + " " +
                                result.getRegion().getArea3().getName() + " " +
                                result.getRegion().getArea4().getName() +
                                result.getLand().getNumber1() + "-" +
                                result.getLand().getNumber2();
                    }



                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    geotext.setText(null);
                                    geotext.setText(region);


                                }
                            });
                        }
                    });

                    t.start();



                }

                @Override
                public void onFailure(Call<ReverseGeocoding> call, Throwable t) {

                }
            });



            return true;
        });


    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("backPressed",9999);
        setResult(9999, data);
        finish();
    }
}
