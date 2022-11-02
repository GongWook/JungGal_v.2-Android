package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SellPostWriteActivity extends Activity {

    private ArrayList<SellPostVO> dataList;
    private Button addSellPostItemBtn;
    private Button sellPostSubmitBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_post_write_activity);

        addSellPostItemBtn = (Button) findViewById(R.id.addSellPostItemBtn);
        sellPostSubmitBtn = (Button) findViewById(R.id.sellPostSubmitBtn);

        this.initializeData();

        RecyclerView recyclerView = findViewById(R.id.sellPostWriteRcyView);

        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(new SellPostAdapter(dataList));  // Adapter 등록

        addSellPostItemBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SellPostVO sell = new SellPostVO();
                SellPostAdapter adapter = new SellPostAdapter(dataList);
                RecyclerView ryv = findViewById(R.id.sellPostWriteRcyView);
                ryv.setAdapter(adapter);

            }
        });

        sellPostSubmitBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                SellPostVO sell = new SellPostVO();


            }
        });

    }

    private void initializeData() {
    }

}
