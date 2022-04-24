package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatActivity extends Activity {

    private ArrayList<Item> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eggong_chat);

        this.initializeData();

        RecyclerView recyclerView = findViewById(R.id.eggong_chat_recycler_view);

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        manager.setStackFromEnd(true);//키보드 올라왔을 때 아래꺼 보이게
        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new MyAdapter(dataList));  // Adapter 등록

    }

    public void initializeData()
    {
        dataList = new ArrayList<>();

        dataList.add(new Item("팀노바님이 입장하셨습니다.", null, null, ViewType.CENTER_JOIN));
        dataList.add(new Item("스틱코드님이 입장하셨습니다.", null, null, ViewType.CENTER_JOIN));
        dataList.add(new Item("안녕하세요", "스틱코드",  "오후 2:00", ViewType.LEFT_CHAT));
        dataList.add(new Item("안녕하세요", null, "오후 2:01", ViewType.RIGHT_CHAT));

    }
}