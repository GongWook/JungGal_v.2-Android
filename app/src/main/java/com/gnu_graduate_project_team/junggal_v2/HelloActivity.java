package com.gnu_graduate_project_team.junggal_v2;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class HelloActivity extends Activity {

    private TextView tx1;
    private TextView tx2;
    private ImageView egg;
    private Button button;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hello);

        tx1 = (TextView) findViewById(R.id.hello_junggal);
        tx2 = (TextView) findViewById(R.id.introduce_ai);
        egg = (ImageView) findViewById(R.id.ai_egg);
        button = (Button) findViewById(R.id.hell_next_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelloActivity.this, HelloChooseActivity.class);
                startActivity(intent);
            }
        });



    }
}
