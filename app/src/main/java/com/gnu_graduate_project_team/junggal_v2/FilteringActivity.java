package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class FilteringActivity extends Activity {

    /** XML 관련 변수 **/
    private ImageView filter_back_btn;
    private CheckBox checkBoxArr[];
    private CheckBox koreanFood_checkbox;
    private CheckBox chineseFood_checkbox;
    private CheckBox japaneseFood_checkbox;
    private CheckBox westernFood_checkbox;
    private CheckBox meat_checkbox;
    private CheckBox seaFood_checkbox;
    private CheckBox healthyFood_checkbox;
    private CheckBox snack_checkbox;
    private CheckBox foodIngredients_checkbox;
    private Button filter_select_btn;

    /** check box flag **/
    private MarkerFlagVO markerFlagVO;
    private boolean[] markerflag;

    /** Thread 사용 **/
    Handler mHandler = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.filter_activity);

        /** Thread 관련 **/
        mHandler = new Handler();

        /** XML 초기화 **/
        filter_back_btn = findViewById(R.id.filter_back_btn);
        koreanFood_checkbox = findViewById(R.id.koreanFood_checkbox);
        chineseFood_checkbox = findViewById(R.id.chineseFood_checkbox);
        japaneseFood_checkbox = findViewById(R.id.japaneseFood_checkbox);
        westernFood_checkbox = findViewById(R.id.westernFood_checkbox);
        meat_checkbox = findViewById(R.id.meat_checkbox);
        seaFood_checkbox = findViewById(R.id.seaFood_checkbox);
        healthyFood_checkbox = findViewById(R.id.healthyFood_checkbox);
        snack_checkbox = findViewById(R.id.snack_checkbox);
        foodIngredients_checkbox = findViewById(R.id.foodIngredients_checkbox);
        filter_select_btn = findViewById(R.id.filter_select_btn);

        /** checkbox arr 초기화 **/
        checkBoxArr = new CheckBox[]{koreanFood_checkbox,chineseFood_checkbox,japaneseFood_checkbox,
                                    westernFood_checkbox,meat_checkbox,seaFood_checkbox,
                                    healthyFood_checkbox,snack_checkbox,foodIngredients_checkbox};

        /** check box flag 초기화 **/
        Intent intent = getIntent();
        markerFlagVO = (MarkerFlagVO) intent.getSerializableExtra("checkFlagArr");
        markerflag = markerFlagVO.getFilteringFlag();

        /** ui setting **/
        uiSetting();
        
        /** 뒤로가기 버튼 클릭 이벤트 리스너 **/
        filter_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /** 체크 박스 클릭 이벤트 리스너 **/
        koreanFood_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    markerflag[0] = true;
                }
                else
                {
                    markerflag[0] = false;
                }
            }
        });

        chineseFood_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    markerflag[1] = true;
                }
                else
                {
                    markerflag[1] = false;
                }
            }
        });

        japaneseFood_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    markerflag[2] = true;
                }
                else
                {
                    markerflag[2] = false;
                }
            }
        });

        westernFood_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    markerflag[3] = true;
                }
                else
                {
                    markerflag[3] = false;
                }
            }
        });

        meat_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    markerflag[4] = true;
                }
                else
                {
                    markerflag[4] = false;
                }
            }
        });

        seaFood_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    markerflag[5] = true;
                }
                else
                {
                    markerflag[5] = false;
                }
            }
        });

        healthyFood_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    markerflag[6] = true;
                }
                else
                {
                    markerflag[6] = false;
                }
            }
        });

        snack_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    markerflag[7] = true;
                }
                else
                {
                    markerflag[7] = false;
                }
            }
        });

        foodIngredients_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    markerflag[8] = true;
                }
                else
                {
                    markerflag[8] = false;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        /** 선택완료 버튼 클릭 이벤트 리스너 **/
        filter_select_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markerFlagVO = new MarkerFlagVO(markerflag);

                Intent intent = new Intent();
                intent.putExtra("checkFlagArr",markerFlagVO);
                setResult(RESULT_OK,intent);
                finish();
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

                        int count = 0;
                        for(boolean b : markerflag)
                        {
                            if(b==true)
                            {
                                checkBoxArr[count].setChecked(true);
                            }
                            count++;
                        }
                    }
                });
            }
        });

        t.start();
    }
}
