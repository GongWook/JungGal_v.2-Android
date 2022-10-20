package com.gnu_graduate_project_team.junggal_v2;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onDeletedMessages() {

        Log.d("메시지 Delete",":deleted");
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        //Token을 서버에 전송
        //서버에 전송 로직 필요

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //수신한 메시지 처리 ( 주석 처리 -> BroadCast 메세지 )
        if(remoteMessage.getNotification() != null){
            if(remoteMessage.getNotification().getTitle().equals("< 정갈 >")) {
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                Intent intent = new Intent("com.gnu.alarm");
                intent.setPackage("com.gnu_graduate_project_team.junggal_v2");
                intent.putExtra("alarm","alarm");
                sendBroadcast(intent);
            }
            else
            {
                showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                Intent intent = new Intent("com.gnu.chatnoti");
                intent.setPackage("com.gnu_graduate_project_team.junggal_v2");
                intent.putExtra("chat","chat");
                sendBroadcast(intent);
            }
        }
        else
        {
            Log.d("FCM error", "getNotification NULL");
        }

    }

    public void showNotification(String title, String message)
    {
        if(title.equals("< 정갈 >"))
        {
            //팝업 터치시 이동할 액티비티를 지정한다.
            Intent intent = new Intent(this, AlarmActivity.class);

            //알림 채널 아이디
            String channel_id ="JungGal";
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            //기본 사운드로 알림음 설정, 커스텀하면 소리 파일의 URL 설정
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                    .setSmallIcon(R.drawable.junggar_logo)
                    .setSound(uri)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000,500,1000})  //알림시 진동 설정 : 1초 진동, 0.5초 쉬고, 1초 진동
                    .setContentIntent(pendingIntent);

            //기본 레이아웃 호출
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_junggalfcm);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(channel_id, "JunggalFCM", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(uri, null);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            //알림 표시
            notificationManager.notify(0, builder.build());
        }
        else
        {
            //팝업 터치시 이동할 액티비티를 지정한다.
            Intent intent = new Intent(this, ChatActivity.class);

            //알림 채널 아이디
            String channel_id ="JungGal";
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            //기본 사운드로 알림음 설정, 커스텀하면 소리 파일의 URL 설정
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                    .setSmallIcon(R.drawable.junggar_logo)
                    .setSound(uri)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{1000,500,1000})  //알림시 진동 설정 : 1초 진동, 0.5초 쉬고, 1초 진동
                    .setContentIntent(pendingIntent);

            //기본 레이아웃 호출
            builder = builder.setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_junggalfcm);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(channel_id, "JunggalFCM", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.setSound(uri, null);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            //알림 표시
            notificationManager.notify(0, builder.build());
        }



    }

}
