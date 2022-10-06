package com.gnu_graduate_project_team.junggal_v2;

import okhttp3.OkHttpClient;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class StompWebSocket {

    public static String WEB_SOCKET_URL = "ws://203.255.3.237/endpoint/websocket";
    public static Long intervalMills = 1000L;
    public OkHttpClient okHttpClient = new OkHttpClient();


}
