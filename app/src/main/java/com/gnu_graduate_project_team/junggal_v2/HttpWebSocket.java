package com.gnu_graduate_project_team.junggal_v2;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class HttpWebSocket {

    public static String WEB_SOCKET_URL = "ws://203.255.3.237/chat/Gong123/websocket";

    public static WebSocketListener listener = new WebSocketListener() {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.d("TLOG", "전송 데이터 확인 : " + webSocket + " : " + response);
            webSocket.close(1000, null);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.d("TLOG", "text 데이터 확인 : " + text.toString());
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Log.d("TLOG", "ByteString 데이터 확인 : " + bytes.toString());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Log.d("TLOG", "소켓 onClosing");
            webSocket.close(1000, null);
            webSocket.cancel();

        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);

        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.d("TLOG", "소켓 onFailure : " + t.toString());
        }
    };

    public void sendWebSocket() {

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(WEB_SOCKET_URL).build();

            client.newWebSocket(request, HttpWebSocket.listener).send("테스트 메시지!");
            //client.newWebSocket(request, HttpWebSocket.listener);

            client.dispatcher().executorService().shutdown();

        }catch(Exception e) {
            Log.e("TLOG", "MAIN 소켓 통신 오류 : " + e.toString());

        }

    }
}
