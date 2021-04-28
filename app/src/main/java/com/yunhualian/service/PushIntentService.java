package com.yunhualian.service;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;


public class PushIntentService extends GTIntentService {
    public PushIntentService() {
    }

    /**
     * 为了观察透传数据变化.
     */
    private static int cnt;

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        LogUtils.e("onReceiveServicePid -> " + "pid = " + pid);
    }

    // 处理透传消息
    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        // 透传消息的处理，详看 SDK demo
        String appid = msg.getAppid();
        String taskid = msg.getTaskId();
        String messageid = msg.getMessageId();
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        String cid = msg.getClientId();

        // 第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
        boolean result = PushManager.getInstance().sendFeedbackMessage(context, taskid, messageid, 90001);
        Log.d(TAG, "call sendFeedbackMessage = " + (result ? "success" : "failed"));

        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + appid + "\ntaskid = " + taskid + "\nmessageid = " + messageid + "\npkg = " + pkg
                + "\ncid = " + cid);

        if (payload == null) {
            Log.e(TAG, "receiver payload = null");
        } else {
            String data = new String(payload);
            Log.d(TAG, "receiver payload = " + data);
//            try {
//                JSONObject jsonObject = new JSONObject(data);
//                String name = jsonObject.getString("class");
//                LogUtils.e("name == " + name);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

            // 测试消息为了观察数据变化
            if (data.equals(getResources().getString(R.string.push_transmission_data))) {
                data = data + "-" + cnt;
                cnt++;
            }
            sendMessage(data, YunApplication.DemoHandler.RECEIVE_MESSAGE_DATA);
        }

        Log.d(TAG, "----------------------------------------------------------------------------------------------");

    }

    // 接收 cid
    @Override
    public void onReceiveClientId(Context context, String clientid) {
        LogUtils.e("onReceiveClientId -> " + "clientid = " + clientid);
    }

    // cid 离线上线通知
    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        LogUtils.e("onReceiveOnlineState -> " + "online " + online);
    }

    // 各种事件处理回执
    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
        LogUtils.e("onReceiveCommandResult -> " + "clientid = " + cmdMessage.getClientId());
    }

    // 通知到达，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage msg) {
        LogUtils.e("onNotificationMessageArrived -> " + "clientid = " + msg.getContent());

    }

    // 通知点击，只有个推通道下发的通知会回调此方法
    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage msg) {
        LogUtils.e("onNotificationMessageClicked -> " + "clientid = " + msg.getContent());
    }

    private void sendMessage(String data, int what) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = data;
        YunApplication.sendMessage(msg);
    }
}
