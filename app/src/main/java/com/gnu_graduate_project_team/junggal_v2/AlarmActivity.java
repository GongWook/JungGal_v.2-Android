package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AlarmActivity extends Activity {

    /** Thread 사용 **/
    Handler mHandler = new Handler();

    /** requestCode **/
    private static Integer review_check = 1;

    /** XML 관련 변수 **/
    private ImageView backBtn;
    private TextView requestAlarmCnt;
    private RecyclerView recyclerView;
    private List<AlarmItem> items = new ArrayList<>();
    private UserVO user= new UserVO();
    private ArrayList<AlarmVO> alarmList;
    private ToggleSwitch alarmToogleSwitch;

    private Boolean toogleFlag = false;

    /** request Alarm 갯수 **/
    public Integer requestCnt=0;

    /** Retrofit **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiFcmInterface apifCMInterface = retrofit.create(ApiFcmInterface.class);
    ApiInterface apiInterface = retrofit.create(ApiInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alarm);

        user.setId(PreferenceManager.getString(AlarmActivity.this,"user_id"));

        /** request 알람 갯수 받기 **/
        Intent intent = getIntent();
        requestCnt = intent.getIntExtra("requestCnt",0);

        /** request UI Setting **/
        AlarmUIsetting(requestCnt);

        /** xml 변수 초기화 **/
        backBtn = (ImageView) findViewById(R.id.AlarmBackBtn);
        recyclerView = (RecyclerView) findViewById(R.id.AlarmRecyclerView);
        alarmToogleSwitch = (ToggleSwitch) findViewById(R.id.alarmToogleSwitch);
        requestAlarmCnt = (TextView) findViewById(R.id.requestAlarmCnt);

        /** recyclerView 등록 **/
        AlarmAdapter alarmAdapter = new AlarmAdapter(items);
        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        /** alarmAdapter onitemclick Listener **/
        alarmAdapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                onResume();
            }
        });

        /** alarmAdapter onItemClickMove Listener **/
        alarmAdapter.setOnItemClickMoveListner(new AlarmAdapter.OnItemClickMoveListner() {
            @Override
            public void onItemClick(int pos) {

                Intent intent = new Intent(AlarmActivity.this, SharePostActivity.class);
                intent.putExtra("sharePostID", alarmList.get(pos).getSharePostId());
                startActivity(intent);

            }
        });

        /** alarmAdapater onItemClickMove to Reveiw Listener **/
        alarmAdapter.setOnItemClickMoveToReviewListner(new AlarmAdapter.OnItemClickMoveToReviewListner() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(AlarmActivity.this, ReviewRegistActivity.class);
                intent.putExtra("postWriterId",alarmList.get(pos).getPostWriter());
                intent.putExtra("applyUser",alarmList.get(pos).getApplyUser());
                intent.putExtra("alarmId",alarmList.get(pos).getAlarmId());
                startActivityForResult(intent,review_check);
            }
        });


        recyclerView.setAdapter(alarmAdapter);
        recyclerView.setLayoutManager(manager);

        /** backBtn Listener **/
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        /** ToogleSwitch Listener **/
        alarmToogleSwitch.setOnToggleSwitchChangeListener(new ToggleSwitch.OnToggleSwitchChangeListener(){

            @Override
            public void onToggleSwitchChangeListener(int position, boolean isChecked) {
                mHandler = new Handler();

                if(position==0)
                {
                    toogleFlag = false;
                    onResume();
                }
                else
                {
                    requestCnt=0;
                    toogleFlag = true;
                    onResume();
                }
            }
        });

        /** resopnse Alarm Init 함수 호출 **/
        responseAlarmInit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        /** request UI Setting **/
        AlarmUIsetting(requestCnt);

        if(!toogleFlag)
        {
            responseAlarmSelect();
            /** resopnse Alarm Init 함수 호출 **/
            responseAlarmInit();
        }
        else
        {
            /** request UI Setting **/
            requestAlarmSelect();
            /** request Alarm Init 함수 호출 **/
            postWriter_initAlarm();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==review_check)
        {
            if(requestCode==RESULT_OK)
            {
                onResume();
            }
        }

    }

    public void postWriter_initAlarm()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Call<UserVO> call = apiInterface.postWriterInitAlarm(user);
                        call.enqueue(new Callback<UserVO>() {
                            @Override
                            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                                Log.d("Alarm count init ", "Success");
                            }

                            @Override
                            public void onFailure(Call<UserVO> call, Throwable t) {
                                Log.d("Alarm count init ", "Failed");
                            }
                        });

                    }
                });
            }
        });

        t.start();
    }

    public void responseAlarmInit()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Call<UserVO> call = apiInterface.responseAlarmInit(user);
                        call.enqueue(new Callback<UserVO>() {
                            @Override
                            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                                Log.d("res Alarm count init ", "Success");
                            }

                            @Override
                            public void onFailure(Call<UserVO> call, Throwable t) {
                                Log.d("res Alarm count init ", "Failed");
                            }
                        });


                    }
                });
            }
        });

        t.start();
    }

    public void requestAlarmSelect()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Call<List<AlarmVO>> call = apifCMInterface.requestAlarmSelect(user);
                        call.enqueue(new Callback<List<AlarmVO>>() {
                            @Override
                            public void onResponse(Call<List<AlarmVO>> call, Response<List<AlarmVO>> response) {
                                alarmList = (ArrayList<AlarmVO>) response.body();
                                items = new ArrayList<>();
                                for(AlarmVO alarm : alarmList)
                                {
                                    if(alarm.getAcceptFlag() == null)
                                    {
                                        items.add(new AlarmItem(0,alarm));
                                    }
                                    else
                                    {
                                        items.add(new AlarmItem(1,alarm));
                                    }
                                }

                                //데이터 적용
                                AlarmAdapter alarmAdapter = new AlarmAdapter(items);
                                recyclerView.setAdapter(alarmAdapter);

                            }

                            @Override
                            public void onFailure(Call<List<AlarmVO>> call, Throwable t) {
                                Toast.makeText(AlarmActivity.this, "네트워크 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        t.start();
    }

    public void responseAlarmSelect()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Call<List<AlarmVO>> call = apifCMInterface.responseAlarmSelect(user);
                        call.enqueue(new Callback<List<AlarmVO>>() {
                            @Override
                            public void onResponse(Call<List<AlarmVO>> call, Response<List<AlarmVO>> response) {
                                alarmList = (ArrayList<AlarmVO>) response.body();
                                items = new ArrayList<>();
                                for(AlarmVO alarm : alarmList)
                                {
                                    if(alarm.getReviewFlag()==null)
                                    {
                                        items.add(new AlarmItem(2,alarm));
                                    }
                                    else
                                    {
                                        items.add(new AlarmItem(3,alarm));
                                    }
                                }

                                //데이터 적용
                                AlarmAdapter alarmAdapter = new AlarmAdapter(items);
                                recyclerView.setAdapter(alarmAdapter);
                            }

                            @Override
                            public void onFailure(Call<List<AlarmVO>> call, Throwable t) {
                                Toast.makeText(AlarmActivity.this, "네트워크 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        });

        t.start();
    }

    public void AlarmUIsetting(int count)
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(count==0)
                        {
                            requestCnt=0;
                            requestAlarmCnt.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            String alarmString = requestCnt+"개";
                            requestAlarmCnt.setVisibility(View.VISIBLE);
                            requestAlarmCnt.setText(alarmString);
                        }


                    }
                });
            }
        });

        t.start();
    }

}
