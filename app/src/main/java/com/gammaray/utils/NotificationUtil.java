package com.gammaray.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.gammaray.MainActivity;
import com.gammaray.R;
import com.gammaray.base.YunApplication;
import com.gammaray.entity.ReceiverPushBean;
import com.gammaray.ui.activity.AuctionRecordsActivity;
import com.gammaray.ui.activity.blindbox.BlindBoxDetailActivity;
import com.gammaray.ui.activity.order.SellAndBuyActivity;
import com.gammaray.ui.activity.user.MyHomePageActivity;

public class NotificationUtil extends ContextWrapper {
    private NotificationManager mManager;
    public static final String sID = "channel_1";
    public static final String sName = "channel_name_1";
    public static final String KEY = "message";
    public static final String DATA = "data";
    ReceiverPushBean receiverPushBean;

    public static final String ART = "art";
    public static final String TRADE = "trade";
    public static final String BLINDBOX = "blind_box";
    public static final String AUCTIONS = "auction";

    public NotificationUtil(Context context) {
        super(context);
    }

    public void sendNotification(ReceiverPushBean receiverPushBean) {
        this.receiverPushBean = receiverPushBean;
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            Notification notification = getNotification_26(receiverPushBean.getTitle(), receiverPushBean.getBody()).build();
            getmManager().notify(1, notification);
        } else {
            Notification notification = getNotification_25(receiverPushBean.getTitle(), receiverPushBean.getBody()).build();
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

    private Intent goPage() {
        Intent intent = null;
        if (receiverPushBean != null) {
            String[] parms = receiverPushBean.getPayload().split("#");
            if (parms.length > 2) {
                String resource = parms[2];
                switch (resource) {
                    case ART:
                        intent = new Intent(YunApplication.getInstance(), MyHomePageActivity.class);
//                        startActivity(intent);
                        break;
                    case TRADE:
                        intent = new Intent(YunApplication.getInstance(), SellAndBuyActivity.class);
                        Bundle sell = new Bundle();
                        sell.putString("from", SellAndBuyActivity.SELL);
                        intent.putExtras(sell);
//                        startActivity(intent);
                        break;
                    case BLINDBOX:
                        intent = new Intent(YunApplication.getInstance(), BlindBoxDetailActivity.class);
                        String blindBoxId = parms[3];
                        Bundle bundle = new Bundle();
                        bundle.putString("id", blindBoxId);
                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;

                    case AUCTIONS:
                        intent = new Intent(YunApplication.getInstance(), AuctionRecordsActivity.class);
                        intent.putExtra("page_index", 2);
                        break;
                    default:
                        intent = new Intent(YunApplication.getInstance(), MainActivity.class);
                }

                return intent;
            }
        } else new Intent(YunApplication.getInstance(), MainActivity.class);
        return intent;
    }

    public NotificationCompat.Builder getNotification_25(String title, String content) {

        // 以下是展示大图的通知
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.setBigContentTitle("BigContentTitle");
        style.setSummaryText("SummaryText");
        Intent intent = new Intent(YunApplication.getInstance(), MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY, true);
        bundle.putSerializable(DATA, receiverPushBean);
        intent.putExtras(bundle);
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, goPage(), PendingIntent.FLAG_UPDATE_CURRENT);

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
                .setSmallIcon(R.mipmap.icon_logo)
                .setProgress(total, progress, false)
                .setAutoCancel(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getNotification_26(String title, String content) {
        Intent intent = new Intent(YunApplication.getInstance(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("key", true);
        PendingIntent contentIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, goPage(), PendingIntent.FLAG_UPDATE_CURRENT);

        return new Notification.Builder(getApplicationContext(), sID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.mipmap.icon_logo)
                .setContentIntent(contentIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo))
                .setStyle(new Notification.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo)))
                .setNumber(1)
                .setAutoCancel(true);
    }


}
