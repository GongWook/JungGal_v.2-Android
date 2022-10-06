package com.gnu_graduate_project_team.junggal_v2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class StompWebSocketActivity extends Activity {

    public static String WEB_SOCKET_URL = "ws://203.255.3.237/endpoint/websocket";
    private StompClient mStompClient;
    private Button StompTestBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stompwebsocket);

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP,WEB_SOCKET_URL);
        StompTestBtn = (Button)findViewById(R.id.stompweb_socket_test_btn);


    }
}
