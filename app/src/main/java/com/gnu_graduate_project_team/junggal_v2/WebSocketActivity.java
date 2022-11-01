package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class WebSocketActivity extends Activity {

    private Button webSocket_Test;
    private HttpWebSocket httpWebSocket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_websocket_test);

        webSocket_Test = (Button)findViewById(R.id.web_socket_test);
        httpWebSocket = new HttpWebSocket();

        webSocket_Test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpWebSocket.sendWebSocket();

            }
        });

    }
}
