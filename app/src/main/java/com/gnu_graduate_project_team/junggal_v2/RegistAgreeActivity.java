package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class RegistAgreeActivity extends Activity {

    private CheckBox agree_check;
    private RadioGroup radioGroup;
    private RadioButton yes_radio;
    private RadioButton no_radio;
    private Button next_btn;

    Boolean all_checked = false;
    Boolean age_checked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.regist_agree_activity);

        agree_check = (CheckBox) findViewById(R.id.all_agree_check);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        yes_radio = (RadioButton) findViewById(R.id.yes_radio);
        no_radio = (RadioButton) findViewById(R.id.no_radio);
        next_btn = (Button) findViewById(R.id.next_btn);

        agree_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(agree_check.isChecked())
                    all_checked = true;
                else
                    all_checked = false;
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId)
                {
                    case R.id.yes_radio:
                        age_checked = true;
                        break;
                    case R.id.no_radio:
                        age_checked = false;
                        break;
                }

            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RegistAgreeActivity.this, RegistPhoneAuthActivity.class);
                
                if (all_checked == false)
                {
                    Toast.makeText(getApplicationContext(),"약관에 동의가 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(age_checked == false)
                {
                    Toast.makeText(getApplicationContext(),"14세 미만은 사용할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    startActivity(intent);
                }
            }
        });


    }
}
