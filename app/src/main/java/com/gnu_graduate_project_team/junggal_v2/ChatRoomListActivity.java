package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChatRoomListActivity extends Activity {

    /** XML 관련 변수 **/
    private ImageView chatRoomListBackBtn;
    private ImageView chat_food_share_btn;
    private ImageView chat_home_btn;
    private RecyclerView chatRoomRecyclerView;
    private CircleImageView chat_room_profile_image;

    /** Chat Room List 내역 **/
    private ArrayList<ChatRoomVO> chatRoomList;
    private List<ChatRoomItem> items = new ArrayList<>();
    ChatRoomAdapter chatRoomAdapter;

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiChatInterface chatInterface = retrofit.create(ApiChatInterface.class);
    ApiPostInterface apiPostInterface = retrofit.create(ApiPostInterface.class);

    /** Thread 사용 **/
    Handler mHandler = null;

    /** 사용자 계정 **/
    private String user_ID;
    private Bitmap bitmap;

    /** 알람 BroadCastRecevier **/
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_room_list_activity);

        /** 스레드 과련 **/
        mHandler = new Handler();

        /** 사용자 계정 초기화 **/
        user_ID = PreferenceManager.getString(ChatRoomListActivity.this,"user_id");

        /** XML 관련 변수 초기화 **/
        chatRoomListBackBtn = (ImageView)findViewById(R.id.chatRoomListBackBtn);
        chat_food_share_btn = (ImageView)findViewById(R.id.chat_food_share_btn);
        chat_home_btn = (ImageView) findViewById(R.id.chat_home_btn);
        chatRoomRecyclerView = (RecyclerView) findViewById(R.id.chatRoomRecyclerView);

        /** 알람 BroadCastRecevier **/
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("com.gnu.chatnoti"))
                {
                    onResume();
                }
            }
        };

        /** recyclerView 등록 **/
        chatRoomAdapter = new ChatRoomAdapter(items);
        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        chatRoomAdapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(ChatRoomListActivity.this, ChatActivity.class);
                intent.putExtra("sharePostId",chatRoomList.get(pos).getShare_post_id());
                intent.putExtra("sharePostName",chatRoomList.get(pos).getShare_post_name());
                intent.putExtra("sharePostWriter",chatRoomList.get(pos).getOwner_id());
                intent.putExtra("sharePostWriterName",chatRoomList.get(pos).getOwner_name());
                intent.putExtra("client",chatRoomList.get(pos).getClient_id());
                intent.putExtra("clientName",chatRoomList.get(pos).getClient_name());
                if(user_ID.equals(chatRoomList.get(pos).getOwner_id()))
                {
                    //내자신이 owner일 경우 상대방은 client

                    Call<ChatRoomVO> call = chatInterface.updateOwnerCntZero(chatRoomList.get(pos));
                    call.enqueue(new Callback<ChatRoomVO>() {
                        @Override
                        public void onResponse(Call<ChatRoomVO> call, Response<ChatRoomVO> response) {
                            Log.d("owner cnt clear","test");
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<ChatRoomVO> call, Throwable t) {
                            Log.d("owner cnt clear","failed");
                        }
                    });
                }
                else
                {
                    //내 자신이 client일 경우 상대방은 owner
                    Call<ChatRoomVO> call = chatInterface.updateClientCntZero(chatRoomList.get(pos));
                    call.enqueue(new Callback<ChatRoomVO>() {
                        @Override
                        public void onResponse(Call<ChatRoomVO> call, Response<ChatRoomVO> response) {
                            Log.d("client cnt clear","test");
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<ChatRoomVO> call, Throwable t) {
                            Log.d("client cnt clear","failed");
                        }
                    });
                }

                onResume();
            }
        });

        chatRoomRecyclerView.setAdapter(chatRoomAdapter);
        chatRoomRecyclerView.setLayoutManager(manager);


        /** 뒤로가기 버튼 **/
        chatRoomListBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /** 음식 나눔 버튼 **/
        chat_food_share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatRoomListActivity.this, SharePostWriteActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /** 홈 버튼 **/
        chat_home_btn.setOnClickListener(new View.OnClickListener() {
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

        /** 로직 흐름 **/
        selectChatRoom();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(receiver !=null)
        {
            unregisterReceiver(receiver);
        }
    }

    public void selectChatRoom()
    {
        UserVO tmp = new UserVO();
        tmp.setId(user_ID);
        Call<List<ChatRoomVO>> call = chatInterface.chatRoomListSelect(tmp);
        call.enqueue(new Callback<List<ChatRoomVO>>() {
            @Override
            public void onResponse(Call<List<ChatRoomVO>> call, Response<List<ChatRoomVO>> response) {
                chatRoomList = (ArrayList)response.body();
                items = new ArrayList<>();
                for(ChatRoomVO chatroom : chatRoomList)
                {
                    chatroom.setUser_id(user_ID);
                    if(user_ID.equals(chatroom.getOwner_id()))
                    {
                        UserVOGet(chatroom.getClient_id(),chatroom);
                    }
                    else
                    {
                        UserVOGet(chatroom.getOwner_id(),chatroom);
                    }

                }

            }

            @Override
            public void onFailure(Call<List<ChatRoomVO>> call, Throwable t) {

            }
        });
    }

    /** 게시글 작성자 서버에서 Select **/
    public void UserVOGet(String userId, ChatRoomVO chatRoomVO)
    {
        UserVO tmp = new UserVO();
        tmp.setId(userId);

        Call<UserVO> call = apiPostInterface.selectWriter(tmp);
        call.enqueue(new Callback<UserVO>() {
            @Override
            public void onResponse(Call<UserVO> call, Response<UserVO> response) {
                UserVO userVO = response.body();

                if(userVO.getProfile_flag()==true)
                {
                    byte[] tmp = Base64.decode(userVO.getImagedata(),Base64.DEFAULT);
                    bitmap = BitmapFactory.decodeByteArray(tmp,0,tmp.length);
                    chatRoomVO.setBitmap(bitmap);
                    chatRoomVO.setBitmapFlag(true);
                }
                else
                {
                    bitmap =null;
                    chatRoomVO.setBitmapFlag(false);
                }

                items.add(new ChatRoomItem(0,chatRoomVO));

                //데이터 적용
                ChatRoomAdapter chatRoomAdapter = new ChatRoomAdapter(items);
                chatRoomRecyclerView.setAdapter(chatRoomAdapter);

            }

            @Override
            public void onFailure(Call<UserVO> call, Throwable t) {
                Toast.makeText(ChatRoomListActivity.this, "작성자 정보 로드 실패\n네트워크 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
