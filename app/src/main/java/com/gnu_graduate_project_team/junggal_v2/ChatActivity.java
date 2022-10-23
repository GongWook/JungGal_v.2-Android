package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatActivity extends Activity {

    /** XML 관련 변수 **/
    private ImageView eggong_chat_backBtn;
    private Button egg_send_text;
    private EditText egg_edit_text;
    private RecyclerView recyclerView;
    private TextView chatOpponentXML;

    /** ChatRoom 관련 변수 **/
    private String userId;
    private String chatOpponent;
    private String chatOpponentName;
    private Integer sharePostId;
    private String sharePostName;
    private String sharePostWriter;
    private String sharePostWriterName;
    private String client;
    private String clientName;
    private ChatRoomVO chatRoom = new ChatRoomVO();
    public Boolean chatRoomSelectFlag = false;

    /** Chat 내역 **/
    private ArrayList<ChatVO> chatList;
    private List<ChatItem> items = new ArrayList<>();
    ChatAdapter chatAdapter;

    /** 알람 BroadCastRecevier **/
    private BroadcastReceiver receiver;

    /** Thread 사용 **/
    Handler mHandler = null;

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiChatInterface chatInterface = retrofit.create(ApiChatInterface.class);

    /** 사용자 계정 **/
    private String user_ID;
    private String user_Name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_eggong_chat);

        /** 사용자 계정 초기화 **/
        user_ID = PreferenceManager.getString(ChatActivity.this,"user_id");
        user_Name = PreferenceManager.getString(ChatActivity.this,"user_name");
        
        /** XML 초기화 **/
        eggong_chat_backBtn = (ImageView)findViewById(R.id.eggong_chat_backBtn);
        egg_send_text = (Button) findViewById(R.id.egg_send_text);
        egg_edit_text = (EditText) findViewById(R.id.egg_edit_text);
        recyclerView = (RecyclerView)findViewById(R.id.eggong_chat_recycler_view);
        chatOpponentXML = (TextView)findViewById(R.id.chatOpponent);

        /** Intent 값 가져오기 **/
        Intent intent = getIntent();
        userId = PreferenceManager.getString(ChatActivity.this,"user_id");
        sharePostId = intent.getIntExtra("sharePostId",0);
        sharePostName = intent.getStringExtra("sharePostName");
        sharePostWriter = intent.getStringExtra("sharePostWriter");
        sharePostWriterName = intent.getStringExtra("sharePostWriterName");
        client = intent.getStringExtra("client");
        clientName = intent.getStringExtra("clientName");

        /** chatting 상대 설정 하기 **/
        if(user_ID.equals(sharePostWriter))
        {
            chatOpponent = client;
            chatOpponentName = clientName;
        }
        else
        {
            chatOpponent = sharePostWriter;
            chatOpponentName = sharePostWriterName;
        }

        /** Chat Room 설정 **/
        chatRoom.setShare_post_id(sharePostId);
        chatRoom.setShare_post_name(sharePostName);
        chatRoom.setOwner_id(sharePostWriter);
        chatRoom.setOwner_name(sharePostWriterName);
        chatRoom.setClient_id(userId);
        chatRoom.setClient_name(user_Name);

        /** 스레드 과련 **/
        mHandler = new Handler();

        /** 알람 BroadCastRecevier **/
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("com.gnu.chatnoti"))
                {
                    selectChat(chatRoom);
                    onResume();
                }
            }
        };

        /** 로직 흐름
         *  0. UI setting
         *  1. ChatRoom이 존재하는지 확인하기
         * **/
        uiSetting();
        selectChatRoom();

        /** 뒤로 가기 버튼 **/
        eggong_chat_backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        /** BroadCast 받기 **/
        IntentFilter intentFilter = new IntentFilter("com.gnu.chatnoti");
        this.registerReceiver(receiver, intentFilter);

        /** recyclerView 등록 **/
        chatAdapter = new ChatAdapter(items);
        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        manager.setStackFromEnd(true);

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chatAdapter);

        /** 리사이클러 뷰 사이즈 변경 이벤트 리스너
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(bottom<oldBottom)
                {

                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(chatList.size()>0)
                            {
                                recyclerView.scrollToPosition(chatList.size()-1);
                            }
                        }
                    },100);
                }
            }
        });
         **/

        egg_send_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String chat = egg_edit_text.getText().toString().trim();
                ChatVO chatVO = new ChatVO();
                chatVO.setChat_room_id(chatRoom.getChatRoom_id());
                chatVO.setReader(chatOpponent);
                chatVO.setSender(userId);
                chatVO.setContent(chat);

                Call<ChatVO> call = chatInterface.insertChat(chatVO);
                call.enqueue(new Callback<ChatVO>() {
                    @Override
                    public void onResponse(Call<ChatVO> call, Response<ChatVO> response) {
                        chatVO.setChat_time(nowTime());
                        items.add(new ChatItem(0,chatVO));
                        ChatAdapter chatAdapter = new ChatAdapter(items);
                        recyclerView.setAdapter(chatAdapter);

                        egg_edit_text.setText("");

                        Log.d("response",response.body().toString());
                    }

                    @Override
                    public void onFailure(Call<ChatVO> call, Throwable t) {
                        Log.d("response",t.toString());
                    }
                });



            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();

        if(receiver !=null)
        {
            unregisterReceiver(receiver);
        }
    }

    /** 채팅방 존재 여부 확인 통신 메소드 **/
    public Boolean selectChatRoom()
    {
        Call<ChatRoomVO> call = chatInterface.selectChatRoom(chatRoom);
        Log.d("chatRoom state check", chatRoom.toString());
        call.enqueue(new Callback<ChatRoomVO>() {
            @Override
            public void onResponse(Call<ChatRoomVO> call, Response<ChatRoomVO> response) {
                ChatRoomVO tmp = response.body();
                Log.d("tmp check",tmp.toString());
                Log.d("chatRoom_id",tmp.getChatRoom_id()+"");

                if(tmp.getChatRoom_id()==null)
                {
                    /** 채팅 룸이 존재 하지 않을 때 채팅 전송 버튼 리스너  **/
                    egg_send_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //채팅방이 존재하지 않을시
                            //1. 채팅방 생성 호출
                            //2. 채팅 등록
                            Call<ChatRoomVO> call = chatInterface.insertChatRoom(chatRoom);
                            call.enqueue(new Callback<ChatRoomVO>() {
                                @Override
                                public void onResponse(Call<ChatRoomVO> call, Response<ChatRoomVO> response) {
                                    chatRoomSelectFlag=true;
                                    chatRoom = response.body();
                                    ChatVO chatVO = new ChatVO();
                                    chatVO.setChat_room_id(chatRoom.getChatRoom_id());
                                    chatVO.setReader(chatOpponent);
                                    chatVO.setSender(userId);
                                    chatVO.setContent(egg_edit_text.getText().toString().trim());
                                    Call<ChatVO> call2 = chatInterface.insertChat(chatVO);
                                    call2.enqueue(new Callback<ChatVO>() {
                                        @Override
                                        public void onResponse(Call<ChatVO> call, Response<ChatVO> response) {
                                            // 채팅 보내기 완료
                                            chatVO.setChat_time(nowTime());
                                            items.add(new ChatItem(0,chatVO));
                                            egg_edit_text.setText("");
                                            onResume();
                                        }

                                        @Override
                                        public void onFailure(Call<ChatVO> call, Throwable t) {

                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<ChatRoomVO> call, Throwable t) {
                                    Toast.makeText(ChatActivity.this, "네트워크를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                            });

                            onResume();

                        }
                    });
                }
                else
                {
                    /** chatRoom이 존재하는 로직  **/
                    //채팅 내역 가져오기
                    selectChat(tmp);
                    onResume();
                }
            }

            @Override
            public void onFailure(Call<ChatRoomVO> call, Throwable t) {
                Log.d("chatRoom select","failed");
            }
        });

        return chatRoomSelectFlag;
    }

    public void uiSetting()
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        chatOpponentXML.setText(chatOpponentName);
                    }
                });
            }
        });

        t.start();

    }

    /** 채팅 내역 Select **/
    public void selectChat(ChatRoomVO tmp)
    {
        chatRoom.setChatRoom_id(tmp.getChatRoom_id());
        Call<List<ChatVO>> getChatCall = chatInterface.selectChatList(tmp);
        getChatCall.enqueue(new Callback<List<ChatVO>>() {
            @Override
            public void onResponse(Call<List<ChatVO>> call, Response<List<ChatVO>> response) {
                chatList = (ArrayList)response.body();
                items = new ArrayList<>();

                for(ChatVO chat : chatList)
                {
                    if(chat.getSender().equals(user_ID))
                    {
                        items.add(new ChatItem(0,chat));
                    }
                    else
                    {
                        chat.setSender(chatOpponentName);
                        items.add(new ChatItem(1,chat));
                    }
                }

                //데이터 적용
                ChatAdapter chatAdapter = new ChatAdapter(items);
                recyclerView.setAdapter(chatAdapter);

                //read cnt 초기화 시키기
                initReadCnt(chatRoom);
            }

            @Override
            public void onFailure(Call<List<ChatVO>> call, Throwable t) {

            }
        });
    }

    /** 현재 시간 설정 함수 **/
    public String nowTime()
    {
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd'T'HH:mm");
        String Time = simpleDateFormat.format(mDate);

        return Time;
    }

    /** 자신 read cnt 초기화 시키기 **/
    public void initReadCnt(ChatRoomVO chatRoom)
    {
        Call<ChatRoomVO> call;
        Log.d("init chatRoom test",chatRoom.toString());
        if(user_ID.equals(chatRoom.getOwner_id()))
        {
            call = chatInterface.updateOwnerCntZero(chatRoom);
            call.enqueue(new Callback<ChatRoomVO>() {
                @Override
                public void onResponse(Call<ChatRoomVO> call, Response<ChatRoomVO> response) {
                    Log.d("owner cnt reset","success");
                }

                @Override
                public void onFailure(Call<ChatRoomVO> call, Throwable t) {
                    Log.d("owner cnt reset","failed");
                }
            });
        }
        else
        {
            call = chatInterface.updateClientCntZero(chatRoom);
            call.enqueue(new Callback<ChatRoomVO>() {
                @Override
                public void onResponse(Call<ChatRoomVO> call, Response<ChatRoomVO> response) {
                    Log.d("client cnt reset","success");
                }

                @Override
                public void onFailure(Call<ChatRoomVO> call, Throwable t) {
                    Log.d("client cnt reset","failed");
                }
            });
        }
    }
}
