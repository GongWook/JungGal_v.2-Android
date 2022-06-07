package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatActivity extends Activity {

    private ArrayList<Item> dataList;
    private EditText egg_edit_text;
    private Button egg_send_text;

    /** 사용자 좌표 **/
    private LatLng userLocation;



    /** Thread 사용 **/
    Handler mHandler = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eggong_chat);

        Retrofit retrofit = EggApiClient.getApiClient();
        EggInterface eggInterface = retrofit.create(EggInterface.class);

        /** 문자 입력칸과 전송 버튼**/
        egg_edit_text = (EditText) findViewById(R.id.egg_edit_text);
        egg_send_text = (Button) findViewById(R.id.egg_send_text);

        this.initializeData();

        /** 사용자 위치 좌표 받아오기 **/
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("userLatitude",0.0);
        double longitude = intent.getDoubleExtra("userLongitude",0.0);
        Log.d("userLatitude",latitude+"");
        Log.d("userLongitude",longitude+"");


        /** Thread 사용 위한 Handler **/
        mHandler = new Handler();

        RecyclerView recyclerView = findViewById(R.id.eggong_chat_recycler_view);

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        manager.setStackFromEnd(true);//키보드 올라왔을 때 아래꺼 보이게
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new MyAdapter(dataList));  // Adapter 등록

        egg_send_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_content = egg_edit_text.getText().toString().trim();
                String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());




                EggchatVO chat = new EggchatVO();
                chat.setInput_text(input_content);
                chat.setX_coordinate((float) longitude);
                chat.setY_coordinate((float) latitude);
                chat.setUid("this_is_uid");
                Item item = new Item(input_content, "tester", timeStamp, ViewType.RIGHT_CHAT);
                dataList.add(item);

                egg_edit_text.setText("");

                /** 전송 시 recyclerview 새로고침 **/
                MyAdapter adapter = new MyAdapter(dataList);
                RecyclerView ryv = findViewById(R.id.eggong_chat_recycler_view);
                ryv.setAdapter(adapter);

                if (item.getContent().contains("안녕"))
                {
                    Item egg_answer = new Item("안녕하세요, 저는 정갈의 마스코트 달걀이에요", "달걀이", timeStamp, ViewType.LEFT_CHAT);
                    dataList.add(egg_answer);

                    /** 전송 시 recyclerview 새로고침 **/
                    adapter = new MyAdapter(dataList);
                    ryv.setAdapter(adapter);
                }
                else if (item.getContent().contains("고마워"))
                {
                    Item egg_answer = new Item("천만에요. 언제든 필요한 것 있으시면 말씀해주세요. \n더 물어보실 건 없나요?", "달걀이", timeStamp, ViewType.LEFT_CHAT);
                    dataList.add(egg_answer);

                    /** 전송 시 recyclerview 새로고침 **/
                    adapter = new MyAdapter(dataList);
                    ryv.setAdapter(adapter);
                }

                else if (item.getContent().contains("없어"))
                {
                    Item egg_answer = new Item("그렇군요. 알겠습니다!", "달걀이", timeStamp, ViewType.LEFT_CHAT);
                    dataList.add(egg_answer);

                    /** 전송 시 recyclerview 새로고침 **/
                    adapter = new MyAdapter(dataList);
                    ryv.setAdapter(adapter);
                }
                else {
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    /** Kochat server와 통신 **/
                                    Call<EggAnswerVO> call = eggInterface.egg_chat(chat);
                                    Log.v("here", chat.getInput_text() + chat.getUid());
                                    call.enqueue(new Callback<EggAnswerVO>() {
                                        @Override
                                        public void onResponse(Call<EggAnswerVO> call, Response<EggAnswerVO> response) {
                                            Log.v("OnResponse는 ", "들어왔음");
                                            EggAnswerVO egg_answer_content = response.body();
                                            Log.d("egg_answer_content", "body도 받아왔음");

                                            if (egg_answer_content.getIntent().equals("FALLBACK"))
                                            {
                                                Log.v("android to server","실패 \n 참고: EggApiClient URL 체크");
                                                Item egg_answer = new Item("그 정보는 알 수 없습니다..", "달걀이", timeStamp, ViewType.LEFT_CHAT);
                                                dataList.add(egg_answer);

                                                /** 전송 시 recyclerview 새로고침 **/
                                                MyAdapter adapter = new MyAdapter(dataList);
                                                RecyclerView ryv = findViewById(R.id.eggong_chat_recycler_view);
                                                ryv.setAdapter(adapter);
                                            }
                                            else {
                                                Log.v("답 받아오기",egg_answer_content.getAnswer());
                                                Item egg_answer = new Item(egg_answer_content.getAnswer(), "달걀이", timeStamp, ViewType.LEFT_CHAT);
                                                dataList.add(egg_answer);

                                                /** 전송 시 recyclerview 새로고침 **/
                                                MyAdapter adapter = new MyAdapter(dataList);
                                                RecyclerView ryv = findViewById(R.id.eggong_chat_recycler_view);
                                                ryv.setAdapter(adapter);

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<EggAnswerVO> call, Throwable t) {
                                            Toast.makeText(ChatActivity.this, "네트워크를 확인해 주세요",Toast.LENGTH_LONG).show();
                                            Log.v("연결 실패","fail");
                                        }

                                    });
                                }
                            });
                        }
                    });

                    t.start();

                }


            }
        });



    }

    public void initializeData()
    {
        dataList = new ArrayList<>();

        dataList.add(new Item("달걀이님이 입장하셨습니다.", null, null, ViewType.CENTER_JOIN));
        dataList.add(new Item("정갈님이 입장하셨습니다.", null, null, ViewType.CENTER_JOIN));
        dataList.add(new Item("안녕하세요!", "달걀이",  "오후 2:00", ViewType.LEFT_CHAT));
        dataList.add(new Item("저는 정갈의 마스코트 달걀이라고 해요 :)", "달걀이",  "오후 2:00", ViewType.LEFT_CHAT));
        dataList.add(new Item("저는 다음과 같은 것들을 알려드릴 수 있어요~\n" +
                "-오늘 기분은 어때요?\n" +
                " 그에 맞는 음식을 추천해 드릴게요\n" +
                "       ex) 발표가 있어서 긴장되네\n" +
                "\n" +
                "-드시고 싶은 게 있나요?\n" +
                " 관련 게시글을 찾아 드릴게요\n" +
                "       ex) 떡볶이 게시글 추천해줘\n" +
                "\n" +
                "-날씨가 궁금하신 장소와 날짜를 알려주시면\n" +
                " 날씨를 알려드릴 수 있어요\n" +
                "       ex) 내일 진주 날씨 어때?\n" +
                "\n" +
                "-미세먼지 정보도 가능해요\n" +
                "       ex) 진주 오늘 미세먼지 어때?\n" +
                "\n" +
                "-주제에 따라 해당 지역의 장소를 추천해드려요\n" +
                "       ex) 진주에 놀 만 한 관광지 알려줄래", "달걀이",  "오후 2:00", ViewType.LEFT_CHAT));
        dataList.add(new Item("고마워", null, "오후 2:01", ViewType.RIGHT_CHAT));

    }
}