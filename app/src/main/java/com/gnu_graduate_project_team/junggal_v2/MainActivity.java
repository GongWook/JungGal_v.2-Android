package com.gnu_graduate_project_team.junggal_v2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.naver.maps.geometry.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    /** 토큰 발급 관련 변수 **/
    private Boolean TokenIssueFlag=false;

    /** 알람 BroadCastRecevier **/
    private BroadcastReceiver receiver;

    /** 사용자 ID값 변수 **/
    private String userId;
    private UserVO user;
    private UserVO userData;

    /** 사용자 위치 받아오는 변수 **/
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private LatLng userLocation;

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
    ApiFcmInterface apiFcmInterface = retrofit.create(ApiFcmInterface.class);
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);

    /** 데이터베이스 마커 정보 **/
    ArrayList<MarkerVO> markerVOArrayList;

    /** UI 마커 정보 **/
    ArrayList<Marker> markerArrayList = new ArrayList<>();

    /** xml 관련 변수 **/
    private ImageView share_icon;
    private ImageView alarmBtn;
    private TextView alarmText;
    private ImageView chatRoomBtn;
    private ImageView myPageBtn;
    private ImageView searchBtn;
    private ImageView filterBtn;

    /** 알람 총 갯수 **/
    private Integer alarmCnt;
    private Integer requestCnt;
    private Integer responseCnt;

    /** Filtering Flag Array **/
    private boolean[] booleans = new boolean[9];
    private MarkerFlagVO markerFlagVO = new MarkerFlagVO(booleans);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent fcm = new Intent(getApplicationContext(), MyFirebaseMessagingService.class);
        startService(fcm);

        TokenIssueFlag = PreferenceManager.getBoolean(MainActivity.this,"TokenIssueFlag");
        userId = PreferenceManager.getString(MainActivity.this,"user_id");

        /** xml 관련 변수 초기화 **/
        share_icon = (ImageView) findViewById(R.id.food_share);
        alarmBtn = (ImageView) findViewById(R.id.alarmBtn);
        alarmText = (TextView) findViewById(R.id.alarmText);
        chatRoomBtn = (ImageView) findViewById(R.id.chatRoomBtn);
        myPageBtn = (ImageView) findViewById(R.id.myPageBtn);
        searchBtn = (ImageView) findViewById(R.id.searchBtn);
        filterBtn = (ImageView) findViewById(R.id.filterBtn);

        /** Thread 관련 **/
        mHandler = new Handler();

        /** FCM 토큰값 발행 성공 리스너 **/
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {

                Log.d("Token :", s+"");

                if(!TokenIssueFlag)
                {

                    Log.d("Token : ", s+"");
                    //서버에 userID와 Token값 전송
                    FcmTokenVO token = new FcmTokenVO(s,userId);
                    Call<FcmTokenVO> call = apiFcmInterface.tokenRegist(token);
                    call.enqueue(new Callback<FcmTokenVO>() {
                        @Override
                        public void onResponse(Call<FcmTokenVO> call, Response<FcmTokenVO> response) {
                            FcmTokenVO checkToken = response.body();
                            if(checkToken.getUser_id().equals("null"))
                            {
                                Toast.makeText(MainActivity.this, "어플을 껐다가 다시켜주세요.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                //서버에 Token값 저장 성공 시 전역 변수에 데이터 저장
                                PreferenceManager.setBoolean(MainActivity.this,"TokenIssueFlag",true);
                            }


                        }

                        @Override
                        public void onFailure(Call<FcmTokenVO> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "인터넷 연결을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        /** 달걀이 채팅 버튼 **/
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EggChatActivity.class);
                intent.putExtra("userLatitude",userLocation.latitude);
                intent.putExtra("userLongitude",userLocation.longitude);
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

        /** 음식 나눔 클릭 이벤트 **/
        share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SharePostWriteActivity.class);
                startActivity(intent);
            }
        });

        /** 알람 아이콘 클릭 이벤트 **/
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyGlobals.getInstance().setAlarmCnt(0);
                alarmCnt=0;
                alarmText.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                intent.putExtra("requestCnt", requestCnt);
                startActivity(intent);
            }
        });

        /** 채팅 룸 클릭 이벤트 **/
        chatRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatRoomListActivity.class);
                startActivity(intent);
            }
        });

        /** 음식 나눔 클릭 이벤트 **/
        share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SharePostWriteActivity.class);
                startActivity(intent);
            }
        });

        /** 필터링 아이콘 클릭 이벤트 리스너 **/
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilteringActivity.class);
                intent.putExtra("checkFlagArr",markerFlagVO);
                launcher.launch(intent);

            }
        });

        /** 알람 BroadCastRecevier **/
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String tmp = intent.getStringExtra("alarm");

                if(tmp.equals("alarm"))
                {
                    alarmCnt=0;
                    /** 알람 개수 조회 **/
                    alarmUiSetting();
                }
            }
        };

        /** 알람 개수 조회 **/
        alarmUiSetting();

    }

    @Override
    protected void onResume() {
        super.onResume();

        /** 기존 마커 제거 **/
        eraseMarker();

        /** Alarm Cnt 초기화 **/
        alarmCnt=0;

        /** 마이페이지 서비스 호출 이벤트 리스너 **/
        myPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                intent.putExtra("userData", user);
                startActivity(intent);
            }
        });

        /** BroadCast 받기 **/
        IntentFilter intentFilter = new IntentFilter("com.gnu.alarm");
        this.registerReceiver(receiver, intentFilter);

        /** Thread 사용 Handler **/
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


        /** xml 관련 변수 초기화 **/
        share_icon = (ImageView) findViewById(R.id.food_share);
        alarmBtn = (ImageView) findViewById(R.id.alarmBtn);
        alarmText = (TextView) findViewById(R.id.alarmText);


        /** 음식 나눔 클릭 이벤트 **/
        share_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SharePostWriteActivity.class);
                startActivity(intent);
            }
        });

        /** 알람 아이콘 클릭 이벤트 **/
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alarmText.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                intent.putExtra("requestCnt", requestCnt);
                startActivity(intent);
            }
        });

        /** 검색 버튼 클릭 이벤트 리스너 **/
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("Latitude",userLocation.latitude);
                intent.putExtra("Longitude",userLocation.longitude);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(receiver !=null)
        {
            Log.d("onpause ","test");
            unregisterReceiver(receiver);
        }
    }
    
    /** startforResult 대체 **/
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()==RESULT_OK)
                    {
                        Intent intent = result.getData();
                        markerFlagVO = (MarkerFlagVO) intent.getSerializableExtra("checkFlagArr");
                        booleans = markerFlagVO.getFilteringFlag();
                        Log.d("flag test", markerFlagVO.toString());
                        eraseMarker();
                        onResume();
                    }
                    else
                    {
                        Log.d("delete resume test ","test");
                        onResume();
                    }
                }
            });

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
                    userLocation = naverMap.getCameraPosition().target;
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
            marker.setTag(m.getShare_post_name()+"_"+m.getShare_post_id()+"_"+m.getLatitude()+"_"+m.getLongitude());

            /** infowoindow adapter 나중에 터질 수도 있을 우려가 있음 checking **/
            infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(getApplicationContext()) {
                @NonNull
                @Override
                public CharSequence getText(@NonNull InfoWindow infoWindow) {
                    String tmp = (String) infoWindow.getMarker().getTag();
                    String[] postname = tmp.split("_");
                    return (CharSequence) postname[0];
                }
            });

            /** infowindow 클릭시 리스너 **/
            infoWindow.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull  Overlay overlay) {
                    String tmp = (String) infoWindow.getMarker().getTag();
                    String[] postData = tmp.split("_");
                    Intent intent = new Intent(MainActivity.this, SharePostActivity.class);
                    intent.putExtra("sharePostID", Integer.parseInt(postData[1]));
                    intent.putExtra("Latitude",postData[2]);
                    intent.putExtra("Longitude",postData[3]);
                    //startActivity(intent);
                    launcher.launch(intent);
                    return true;
                }
            });

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
            if(m.getIcon().equals(OverlayImage.fromResource(R.drawable.korea_marker)))
            {
                if(booleans[0]!=true)
                {
                    m.setMap(naverMap);
                }
            }
            else if(m.getIcon().equals(OverlayImage.fromResource(R.drawable.china_marker)))
            {
                if(booleans[1]!=true)
                {
                    m.setMap(naverMap);
                }
            }
            else if(m.getIcon().equals(OverlayImage.fromResource(R.drawable.japan_marker)))
            {
                if(booleans[2]!=true)
                {
                    m.setMap(naverMap);
                }
            }
            else if(m.getIcon().equals(OverlayImage.fromResource(R.drawable.western_marker)))
            {
                if(booleans[3]!=true)
                {
                    m.setMap(naverMap);
                }
            }
            else if(m.getIcon().equals(OverlayImage.fromResource(R.drawable.meat_marker)))
            {
                if(booleans[4]!=true)
                {
                    m.setMap(naverMap);
                }
            }
            else if(m.getIcon().equals(OverlayImage.fromResource(R.drawable.seafood_marker)))
            {
                if(booleans[5]!=true)
                {
                    m.setMap(naverMap);
                }
            }
            else if(m.getIcon().equals(OverlayImage.fromResource(R.drawable.healthy_marker)))
            {
                if(booleans[6]!=true)
                {
                    m.setMap(naverMap);
                }
            }
            else if(m.getIcon().equals(OverlayImage.fromResource(R.drawable.snack_marker)))
            {
                if(booleans[7]!=true)
                {
                    m.setMap(naverMap);
                }
            }
            else
            {
                if(booleans[7]!=true)
                {
                    m.setMap(naverMap);
                }
            }

            //m.setMap(naverMap);
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

    /** 알람 갯수 조회 **/
    public void alarmUiSetting()
    {

        try {
            Thread.sleep(2000);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        user = new UserVO();
        user.setId(userId);
        Call<UserVO> call = apiInterface.postWriterSelectAlarm(user);

        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                user = response.body();

                requestCnt = user.getRequestAlarmCnt();
                responseCnt = user.getResponseAlarmCnt();
                Log.d("requestCnt",requestCnt.toString());
                Log.d("responseCnt",responseCnt.toString());
                alarmCnt += requestCnt + responseCnt;

                Log.d("alarmCnt test ", alarmCnt.toString());

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                if(requestCnt + responseCnt!=0)
                                {
                                    String alarmString = requestCnt + responseCnt+"개";
                                    alarmText.setVisibility(View.VISIBLE);
                                    alarmText.setText(alarmString);
                                    Log.d("알람 갯수",alarmString);
                                }
                                else
                                {
                                    alarmText.setVisibility(View.INVISIBLE);
                                }


                            }
                        });
                    }
                });

                t.start();

            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable t) {
                Log.d("noti count test ", t.toString());
            }
        });


    }


}