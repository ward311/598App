package com.example.homerenting_prototype_one.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.homerenting_prototype_one.R;
import com.example.homerenting_prototype_one.calendar.Calendar;
import com.example.homerenting_prototype_one.system.System;
import com.example.homerenting_prototype_one.system.System_Data;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FCMService extends FirebaseMessagingService {

    public static final String TAG = "FCM";
    private String CHANNEL_ID = "Coder";
    SharedPreferences fcm_SP;
    public FCMService() { }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        fcm_SP = getSharedPreferences("fcmToken", Context.MODE_PRIVATE);
        fcm_SP.edit().putString("fcmToken", s).apply();
        Log.d(TAG, "裝置Token: "+s);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            Log.i("MyFirebaseService","title "+remoteMessage.getNotification().getTitle());
            Log.i("MyFirebaseService","body "+remoteMessage.getNotification().getBody());
            Log.d(TAG, "onMessageReceived: "+remoteMessage.getData());
            /**檢查手機版本是否支援通知；若支援則新增"頻道"*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID, "DemoCode", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                assert manager != null;
                manager.createNotificationChannel(channel);

            }
            Map<String,String> s = remoteMessage.getData();
            if(remoteMessage.getNotification().getBody().contains("已收到款項")){
                Intent intent = new Intent(this, Calendar.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }




            /**建置通知欄位的內容*/
            NotificationCompat.Builder builder
                    = new NotificationCompat.Builder(FCMService.this,CHANNEL_ID)
                    .setSmallIcon(R.drawable.home_logo)
                    .setContentTitle(s.get("title"))
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE);
            /**發出通知*/
            NotificationManagerCompat notificationManagerCompat
                    = NotificationManagerCompat.from(FCMService.this);
            notificationManagerCompat.notify(1,builder.build());
            //到這邊
        }

    }
}