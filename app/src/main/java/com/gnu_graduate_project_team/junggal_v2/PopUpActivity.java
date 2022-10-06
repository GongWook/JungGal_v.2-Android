package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PopUpActivity extends Activity {

    TextView ment;
    Button cancleBtn;
    Button submitBtn;
    String applyUserName;

    /** 레트로 핏 **/
    Retrofit retrofit = ApiClient.getApiClient();
    ApiFcmInterface apiFcmInterface = retrofit.create(ApiFcmInterface.class);

    /** FcmApplyMessage **/
    FcmApplyMessage fcmApplyMessage = new FcmApplyMessage();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        ment = (TextView)findViewById(R.id.PopupTitle);
        cancleBtn = (Button)findViewById(R.id.CanclePopupButton);
        submitBtn = (Button)findViewById(R.id.SubmitPopupButton);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        ment.setText(title);

        //user_name
        applyUserName = PreferenceManager.getString(PopUpActivity.this,"user_name");

        fcmApplyMessage.setSharePostId(intent.getIntExtra("sharePostId",0));
        fcmApplyMessage.setPostName(intent.getStringExtra("sharePostName"));
        fcmApplyMessage.setPostWriter(intent.getStringExtra("sharePostWriter"));
        fcmApplyMessage.setShareTime(intent.getStringExtra("shareTime"));
        fcmApplyMessage.setApplyUser(intent.getStringExtra("applyUser"));
        fcmApplyMessage.setApplyUserName(applyUserName);


        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent();
                intent1.putExtra("result","ok");
                setResult(RESULT_OK, intent1);
                Log.d("applyUsername test", applyUserName);

                /**
                 * Call<FcmTokenVO> call = apiFcmInterface.tokenRegist(token);
                 *                     call.enqueue(new Callback<FcmTokenVO>() {
                 * **/

                Call<FcmApplyMessage> call = apiFcmInterface.sharePostApply(fcmApplyMessage);
                call.enqueue(new Callback<FcmApplyMessage>() {
                    @Override
                    public void onResponse(Call<FcmApplyMessage> call, Response<FcmApplyMessage> response) {
                        Toast.makeText(PopUpActivity.this, "나눔 신청 접수가 완료되었습니다.\n나눔자의 알람이 올 때까지 기다려주세요!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Call<FcmApplyMessage> call, Throwable t) {
                        Toast.makeText(PopUpActivity.this, "네트워크 상태를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction()==MotionEvent.ACTION_OUTSIDE)
        {
            return false;
        }
        return true;
    }
}
