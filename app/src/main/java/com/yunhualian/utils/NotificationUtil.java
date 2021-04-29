package com.yunhualian.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.yunhualian.MainActivity;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;

public class NotificationUtil extends ContextWrapper {
    private NotificationManager mManager;
    public static final String sID = "channel_1";
    public static final String sName = "channel_name_1";

    public NotificationUtil(Context context) {
        super(context);
    }

    public void sendNotification(String title, String content) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getNotification_26(title, content).build();
            getmManager().notify(1, notification);
        } else {
            Notification notification = getNotification_25(title, content).build();
            getmManager().notify(1, notification);
        }
    }


    private NotificationManager getmManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(sID, sName, NotificationManager.IMPORTANCE_HIGH);
        getmManager().createNotificationChannel(channel);
    }

    public NotificationCompat.Builder getNotification_25(String title, String content) {

        // 以下是展示大图的通知
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.setBigContentTitle("BigContentTitle");
        style.setSummaryText("SummaryText");
        Intent intent = new Intent(YunApplication.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("key", true);
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 以下是展示多文本通知
        NotificationCompat.BigTextStyle style1 = new NotificationCompat.BigTextStyle();
        style1.setBigContentTitle(title);
        style1.bigText(content);

        return new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.icon_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo)).setAutoCancel(true)
                .setAutoCancel(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification_26(String title, String content, int total, int progress) {
        return new Notification.Builder(getApplicationContext(), sID)
                .setContentTitle(title)
                .setContentText(progress + "%")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setProgress(total, progress, false)
                .setAutoCancel(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification_26(String title, String content) {
        Intent intent = new Intent(YunApplication.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("key", true);
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new Notification.Builder(getApplicationContext(), sID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setStyle(new Notification.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background)))
                .setNumber(1)
                .setAutoCancel(true);
    }


}
