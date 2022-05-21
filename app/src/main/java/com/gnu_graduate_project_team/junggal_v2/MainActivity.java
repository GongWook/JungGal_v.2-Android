package com.gnu_graduate_project_team.junggal_v2;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.UiThread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.naver.maps.geometry.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    /** 사용자 위치 받아오는 변수 **/
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;


    /** 카메라 관련 변수 **/
    private double zoomLevel;
    private Boolean initflag = false;
    private Boolean moveflag = false;
    private LatLng cameraPosition;
    private final int REASON_GESTURE = -1;
    private final int REASON_INIT = -3;

    /** Thread 사용 **/
    Handler mHandler = null;

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiPostMarkerInterface markerInterface = retrofit.create(ApiPostMarkerInterface.class);

    /** 데이터베이스 마커 정보 **/
    ArrayList<MarkerVO> markerVOArrayList;

    /** UI 마커 정보 **/
    ArrayList<Marker> markerArrayList = new ArrayList<>();

    private ImageView share_icon;

    @Override
    protected void onResume() {
        super.onResume();

        mHandler = new Handler();

        /** 사용자 위치  **/
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


        /** 네이버 지도 xml과 연동  **/
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        /** 네이버 지도 api 호출 **/
        mapFragment.getMapAsync(this);

        share_icon = (ImageView) findViewById(R.id.food_share);


        /** 음식 나눔 클릭 이벤트 **/
        share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SharePostWriteActivity.class);
                startActivity(intent);
            }
        });

        Log.d("resum_test", "success");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mHandler = new Handler();

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        /** 사용자 위치  **/
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);


        /** 네이버 지도 xml과 연동  **/
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        /** 네이버 지도 api 호출 **/
        mapFragment.getMapAsync(this);

        share_icon = (ImageView) findViewById(R.id.food_share);




        /** 음식 나눔 클릭 이벤트 **/
        share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SharePostWriteActivity.class);
                startActivity(intent);
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
        uiSettings.setLocationButtonEnabled(false);

        naverMap.setIndoorEnabled(true);

        /** zoom level 설정 **/
        naverMap.setMinZoom(11.0);
        naverMap.setMaxZoom(17.0);

        /** 카메라 변경 이벤트 **/
        naverMap.addOnCameraChangeListener(new NaverMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(int reason, boolean b) {

                if( reason == REASON_GESTURE)
                {
                    moveflag = true;
                }
                else if (reason == REASON_INIT)
                {
                    naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
                    initflag = true;
                }

            }
        });


        /** 카메라 대기상태 리스너 **/
        naverMap.addOnCameraIdleListener(new NaverMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                if(moveflag == true)
                {
                    cameraPosition = naverMap.getCameraPosition().target;
                    moveflag = false;

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    eraseMarker();
                                    markerCall(cameraPosition.longitude +" "+cameraPosition.latitude);

                                }
                            });
                        }
                    });

                    t.start();
                }

                if (initflag == true)
                {
                    cameraPosition = naverMap.getCameraPosition().target;
                    initflag = false;

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {

                                    markerCall(cameraPosition.longitude +" "+cameraPosition.latitude);
                                }
                            });
                        }
                    });

                    t.start();

                }

            }
        });




    }

    /** 마커 정보 데이터 베이스로 부터 가져오기 **/
    public void markerCall(String point)
    {
        Point position = new Point(point);
        Call<List<MarkerVO>> call = markerInterface.sharePostMarker(position);
        call.enqueue(new Callback<List<MarkerVO>>() {
            @Override
            public void onResponse(Call<List<MarkerVO>> call, Response<List<MarkerVO>> response) {
                markerVOArrayList = (ArrayList<MarkerVO>) response.body();
                markerMaker();

            }

            @Override
            public void onFailure(Call<List<MarkerVO>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "인터넷 연결을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });



    }

    /** 네이버 마커 만들기 **/
    public void markerMaker()
    {
        Marker marker;
        InfoWindow infoWindow = new InfoWindow();
        for(MarkerVO m : markerVOArrayList)
        {
            marker = new Marker();
            marker.setPosition(new LatLng(m.getLatitude(),m.getLongitude()));
            switch(m.getShare_post_icon())
            {
                case 1:
                    marker.setIcon(OverlayImage.fromResource(R.drawable.korea_marker));
                    break;
                case 2:
                    marker.setIcon(OverlayImage.fromResource(R.drawable.china_marker));
                    break;
                case 3:
                    marker.setIcon(OverlayImage.fromResource(R.drawable.japan_marker));
                    break;
                case 4:
                    marker.setIcon(OverlayImage.fromResource(R.drawable.western_marker));
                    break;
                case 5:
                    marker.setIcon(OverlayImage.fromResource(R.drawable.meat_marker));
                    break;
                case 6:
                    marker.setIcon(OverlayImage.fromResource(R.drawable.seafood_marker));
                    break;
                case 7:
                    marker.setIcon(OverlayImage.fromResource(R.drawable.healthy_marker));
                    break;
                case 8:
                    marker.setIcon(OverlayImage.fromResource(R.drawable.snack_marker));
                    break;
                case 9:
                    marker.setIcon(OverlayImage.fromResource(R.drawable.food_ingredients_marker));
                    break;

            }
            marker.setTag(m.getShare_post_name());

            /** infowoindow adapter **/
            infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
                @NonNull
                @Override
                public CharSequence getText(@NonNull InfoWindow infoWindow) {
                    return (CharSequence) infoWindow.getMarker().getTag();
                }
            });

            /** infowindow 클릭시 리스너

            /** Marker 클릭시 리스너 **/
            marker.setOnClickListener(overlay -> {

                Marker tmp = (Marker)overlay;

                if (tmp.getInfoWindow() == null) {
                    // 현재 마커에 정보 창이 열려있지 않을 경우 엶
                    infoWindow.open(tmp);
                } else {
                    // 이미 현재 마커에 정보 창이 열려있을 경우 닫음
                    infoWindow.close();
                }

                return true;
            });

            markerArrayList.add(marker);

        }

        for(Marker m : markerArrayList)
        {
            m.setMap(naverMap);
        }
    }

    /** 지도위 마커 지우기 **/
    public void eraseMarker()
    {
        for(Marker m : markerArrayList)
        {
            m.setMap(null);
        }
        markerArrayList = new ArrayList<>();
    }




}