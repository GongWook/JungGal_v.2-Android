package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

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

    /** Chat Room List 내역 **/
    private ArrayList<ChatRoomVO> chatRoomList;
    private List<ChatRoomItem> items = new ArrayList<>();
    ChatRoomAdapter chatRoomAdapter;

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiChatInterface chatInterface = retrofit.create(ApiChatInterface.class);

    /** 사용자 계정 **/
    private String user_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chat_room_list_activity);

        /** 사용자 계정 초기화 **/
        user_ID = PreferenceManager.getString(ChatRoomListActivity.this,"user_id");

        /** XML 관련 변수 초기화 **/
        chatRoomListBackBtn = (ImageView)findViewById(R.id.chatRoomListBackBtn);
        chat_food_share_btn = (ImageView)findViewById(R.id.chat_food_share_btn);
        chat_home_btn = (ImageView) findViewById(R.id.chat_home_btn);
        chatRoomRecyclerView = (RecyclerView) findViewById(R.id.chatRoomRecyclerView);

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
                if(user_ID.equals(chatRoomList.get(pos).getOwner_id()))
                {
                    intent.putExtra("sharePostWriter",chatRoomList.get(pos).getClient_id());
                    intent.putExtra("chatOpponent",chatRoomList.get(pos).getClient_name());
                }
                else
                {
                    intent.putExtra("sharePostWriter",chatRoomList.get(pos).getOwner_id());
                    intent.putExtra("chatOpponent",chatRoomList.get(pos).getOwner_name());
                }

                startActivity(intent);
            }
        });

        chatRoomRecyclerView.setAdapter(chatRoomAdapter);
        chatRoomRecyclerView.setLayoutManager(manager);

        /** 로직 흐름 **/
        selectChatRoom();

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
                    items.add(new ChatRoomItem(0,chatroom));
                }

                //데이터 적용
                ChatRoomAdapter chatRoomAdapter = new ChatRoomAdapter(items);
                chatRoomRecyclerView.setAdapter(chatRoomAdapter);
            }

            @Override
            public void onFailure(Call<List<ChatRoomVO>> call, Throwable t) {

            }
        });
    }
}
