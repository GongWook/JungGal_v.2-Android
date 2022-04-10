package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class HelloChooseActivity extends Activity {

    private Button yes_btn;
    private Button no_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hello_choose);

        yes_btn = (Button) findViewById(R.id.yes_button);
        no_btn = (Button) findViewById(R.id.no_button);

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelloChooseActivity.this, RegistAgreeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelloChooseActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
