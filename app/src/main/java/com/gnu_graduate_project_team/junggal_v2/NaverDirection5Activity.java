package com.gnu_graduate_project_team.junggal_v2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NaverDirection5Activity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    double latitude;
    double longitude;

    double present_latitude;
    double present_longitude;

    private String duration;
    private String distance;

    ArrayList<LatLng> latLngArrayList = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationClient;

    /** xml 초기화 **/
    private ImageView DirectionBackBtn;
    private TextView carRemainTime;
    private TextView walkRemainTime;

    /** Thread 사용 **/
    Handler mHandler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.naver_direction5_activity);

        /** Thread 사용 **/
        mHandler = new Handler();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                present_latitude = location.getLatitude();
                present_longitude = location.getLongitude();
                Log.d("present latitude", " " + present_latitude);
            }
        });

        Intent intent = getIntent();
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);

        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map_fragment);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        /** xml 초기화 **/
        DirectionBackBtn = findViewById(R.id.DirectionBackBtn);
        carRemainTime = findViewById(R.id.carRemainTime);
        walkRemainTime = findViewById(R.id.walkRemainTime);

        /** Back Btn Click Listener **/
        DirectionBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
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

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        String APIKEY_ID = "kzaooxgd1b";
        String APIKEY = "CJ4q3kUcNv1DzmKhqzNc4QByJ3516r0H0r0gmz1b";

        PathOverlay drawpath = new PathOverlay();
        // 네이버 지도 ui setting

        UiSettings uiSettings = naverMap.getUiSettings();

        uiSettings.setLocationButtonEnabled(true);
        naverMap.setLocationSource(locationSource);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NaverDirection5Interface naverapi = retrofit.create(NaverDirection5Interface.class);

        Call<NaverDirection5ResultPath> call = naverapi.getPath(APIKEY_ID,APIKEY,present_longitude+", "+present_latitude,longitude+", " +latitude,"trafast");

        call.enqueue(new Callback<NaverDirection5ResultPath>() {
            @Override
            public void onResponse(Call<NaverDirection5ResultPath> call, Response<NaverDirection5ResultPath> response) {
                if(response.isSuccessful()){
                    NaverDirection5ResultPath resultPath = response.body();
                    NaverDirection5Trafast t = resultPath.getRoute().getTrafast()[0];
                    NaverDirection5Summary s = t.getSummary();
                    duration = s.getDuration();
                    distance = s.getDistance();
                    uiSetting();

                    Double[][] tmppath = t.getPath();


                    for(int i =0; i<tmppath.length; i++){

                        latLngArrayList.add(i,new LatLng(resultPath.getRoute().getTrafast()[0].getPath()[i][1],resultPath.getRoute().getTrafast()[0].getPath()[i][0]));

                        //Log.d("Testing Success", " " + resultPath.getRoute().getTrafast()[0].getPath()[i][0]);
                        //Log.d("Testing Success", " " + resultPath.getRoute().getTrafast()[0].getPath()[i][1]);
                    }


                    Log.d("Testing Success", ""+latLngArrayList.subList(0,latLngArrayList.size()));

                    drawpath.setCoords(latLngArrayList.subList(0,latLngArrayList.size()));
                    drawpath.setWidth(18);
                    drawpath.setOutlineWidth(2);
                    drawpath.setProgress(0);
                    drawpath.setPassedColor(Color.GRAY);
                    //drawpath.setPatternImage(OverlayImage.fromResource(R.drawable.arrow_path));
                    //drawpath.setPatternInterval(10);
                    drawpath.setMap(naverMap);


                    //Log.d("Testing Success", " " + t.toString());

                }else{
                    Log.d("Testing failed", "onResponse : 실패");
                }
            }

            @Override
            public void onFailure(Call<NaverDirection5ResultPath> call, Throwable t) {

            }
        });
    }

    public void uiSetting()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        //duration distance
                        //

                        Log.d("distance", distance+"");

                        Integer intCarRemainTime = (Integer.parseInt(duration)/1000/60);
                        double doubleWalkRemainTime = ((Integer.parseInt(distance)/1000.0)/5)*60*10;
                        Integer intWalkRmainTime=(int)doubleWalkRemainTime;

                        Log.d("intWalkRemainTime", intWalkRmainTime+"");

                        String stringCarRemainTime="";
                        String stringWalkRemainTime="";


                        if (intCarRemainTime<1)
                        {
                            stringCarRemainTime+="1분";
                        }
                        else if(intCarRemainTime>=1 && intCarRemainTime<60)
                        {
                            stringCarRemainTime+=intCarRemainTime+"분";
                        }
                        else
                        {
                            stringCarRemainTime+=" "+intCarRemainTime/60+"시 "+intCarRemainTime%60+"분";
                        }

                        if(intWalkRmainTime/10<1)
                        {
                            stringWalkRemainTime+=intWalkRmainTime%10+"초 ";
                        }
                        else if(intWalkRmainTime/10>=1 && intWalkRmainTime/10<60)
                        {
                            stringWalkRemainTime+= intWalkRmainTime/10+"분 "+intWalkRmainTime%10+"초 ";
                        }
                        else
                        {
                            stringWalkRemainTime+= intWalkRmainTime/600+"시 ";
                            intWalkRmainTime-= intWalkRmainTime/600 * 600;
                            stringWalkRemainTime+= intWalkRmainTime/10+"분";
                        }



                        carRemainTime.setText(stringCarRemainTime);
                        walkRemainTime.setText(stringWalkRemainTime);

                    }

                });
            }
        });

        t.start();
    }
}
