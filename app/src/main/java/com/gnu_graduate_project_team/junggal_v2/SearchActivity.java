package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
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

public class SearchActivity extends Activity {

    /** XML 관련 변수 **/
    private ImageView search_list_back_btn;
    private ImageView search_select_btn;
    private EditText search_edit_box;
    private RecyclerView search_result_recyclerview;

    /** 사용자 위치 관련 변수 **/
    private Point point;
    private double latitude;
    private double longitude;

    /** 검색 Keyword **/
    private String keyword;

    /** Chat 내역 **/
    private ArrayList<SharePostVO> resultList;
    private List<SearchItem> items = new ArrayList<>();
    SearchAdapter searchAdapter;

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiPostInterface postInterface = retrofit.create(ApiPostInterface.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_list_activity);

        /** XML 변수 초기화 **/
        search_list_back_btn = findViewById(R.id.search_list_back_btn);
        search_select_btn = findViewById(R.id.search_select_btn);
        search_edit_box = findViewById(R.id.search_edit_box);
        search_result_recyclerview = findViewById(R.id.search_result_recyclerview);

        /** 사용자 위치 좌표 받아오기 **/
        Intent intent = getIntent();
        point = new Point();
        latitude = intent.getDoubleExtra("Latitude",0.0);
        longitude = intent.getDoubleExtra("Longitude",0.0);
        point.setLatitude(latitude+"");
        point.setLongitude(longitude+"");

        /** recyclerView 등록 **/
        searchAdapter = new SearchAdapter(items);
        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        searchAdapter.setOnItemClickListener(new AlarmAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(SearchActivity.this, SharePostActivity.class);
                intent.putExtra("sharePostID", resultList.get(pos).getShare_post_id());
                startActivity(intent);
            }
        });

        search_result_recyclerview.setLayoutManager(manager);
        search_result_recyclerview.setAdapter(searchAdapter);

        

        /** 뒤로가기 버튼 클릭 리스너 **/
        search_list_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        /** 검색 버튼 클릭 이벤트 리스너 **/
        search_select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = search_edit_box.getText().toString().trim();
                selectResult(keyword);
            }
        });

        /** 검색 엔터 이벤트 리스너 **/
        search_edit_box.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode)
                {
                    case KeyEvent.KEYCODE_ENTER:
                        keyword = search_edit_box.getText().toString().trim();
                        selectResult(keyword);

                        break;
                }
                return true;
            }
        });

    }

    public void selectResult(String keyword)
    {
        items = new ArrayList<>();

        SharePostVO tmp = new SharePostVO();
        tmp.setKeyword(keyword);
        tmp.setPoint(point);
        Call<List<SharePostVO>> call = postInterface.searchPost(tmp);
        call.enqueue(new Callback<List<SharePostVO>>() {
            @Override
            public void onResponse(Call<List<SharePostVO>> call, Response<List<SharePostVO>> response) {
                resultList = (ArrayList)response.body();

                if(resultList.size()==0)
                {
                    SharePostVO post = new SharePostVO();
                    items.add(new SearchItem(0,post));
                }
                else
                {
                    for(SharePostVO post : resultList)
                    {
                        items.add(new SearchItem(1,post));
                        Log.d("post test", post.toString());
                    }
                }

                //데이터 적용
                SearchAdapter searchAdapter = new SearchAdapter(items);
                search_result_recyclerview.setAdapter(searchAdapter);

            }

            @Override
            public void onFailure(Call<List<SharePostVO>> call, Throwable t) {

            }
        });
    }
}
