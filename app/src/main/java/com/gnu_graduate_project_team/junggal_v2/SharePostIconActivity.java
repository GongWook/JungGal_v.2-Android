package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SharePostIconActivity extends Activity {

    private Button iconSelectBtn;
    private RadioGroup share_icon_radio_group;
    private Integer share_icon_number = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.share_post_share_icon_activity);


        iconSelectBtn = (Button) findViewById(R.id.select_btn);
        share_icon_radio_group = (RadioGroup) findViewById(R.id.share_icon_radio_group);


        /** 라디오 그룹 클릭 **/
        share_icon_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId)
                {
                    case R.id.koreanFood:
                        share_icon_number = 1;
                        break;
                    case R.id.chineseFood:
                        share_icon_number = 2;
                        break;
                    case R.id.japaneseFood:
                        share_icon_number = 3;
                        break;
                    case R.id.westernFood:
                        share_icon_number = 4;
                        break;
                    case R.id.meat:
                        share_icon_number = 5;
                        break;
                    case R.id.seaFood:
                        share_icon_number = 6;
                        break;
                    case R.id.healthyFood:
                        share_icon_number = 7;
                        break;
                    case R.id.snack:
                        share_icon_number = 8;
                        break;
                    case R.id.foodIngredients:
                        share_icon_number = 9;
                        break;
                }
            }
        });

        /** 나눔 아이콘 선택 완료 버튼 **/
        iconSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(share_icon_number == null) {
                    Toast.makeText(SharePostIconActivity.this, "나눔할 아이콘을 더블클릭 해주세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent data = new Intent();
                    data.putExtra("sharePostIconNumber", share_icon_number);
                    setResult(3, data);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("backPressed",9999);
        setResult(9999, data);
        finish();
    }
}
