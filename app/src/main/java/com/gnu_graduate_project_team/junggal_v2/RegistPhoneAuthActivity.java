package com.gnu_graduate_project_team.junggal_v2;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class RegistPhoneAuthActivity extends Activity {

    private EditText phoneNum;
    private Button auth_btn;
    private Button next_btn;

    static final int SMS_SEND_PERMISSION =1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.regist_phone_auth_activity);

        phoneNum = (EditText) findViewById(R.id.phone_num);
        auth_btn = (Button) findViewById(R.id.auth_button);
        next_btn = (Button) findViewById(R.id.auth_next_button);

        // 인증 번호 받는 버튼
        auth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                sendSMS(phoneNum.getText().toString(),"메시지 전송 테스트");
                
            }
        });


        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistPhoneAuthActivity.this, RegistActivity.class);
                startActivity(intent);
            }
        });



        /***
         * 문자 보내기 권환 확인
         */

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if(permissionCheck != PackageManager.PERMISSION_GRANTED)
        {
            //문자 보내기 권환 거부시
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS))
            {
                Toast.makeText(getApplicationContext(), "SMS 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_SEND_PERMISSION);
        }



    }

    /***
     *  SMS 발송 기능
     *
     * @param phoneNumber
     * @param message
     *
     */



    private void sendSMS(String phoneNumber, String message)
    {
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, RegistPhoneAuthActivity.class), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);

        Toast.makeText(getBaseContext(), "메시지가 전송 되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
