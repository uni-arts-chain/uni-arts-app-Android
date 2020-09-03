package com.yunhualian.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blankj.utilcode.util.SPUtils;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.utils.DownLoadManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class DownLoadService extends Service {

    private String mPath;
    private String mVersion;

    private LocalBroadcastManager manager;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        manager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPath = intent.getStringExtra("Path");
        mVersion = intent.getStringExtra("VerTxt");

        new Thread(new DownloadThread()).start();

        return START_NOT_STICKY;
    }

    private class DownloadThread implements Runnable {
        @Override
        public void run() {
            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    AppConstant.DownLoadValues.IsDownLoadFinish = false;

                    URL url = new URL(mPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);

                    SPUtils.getInstance().put(ExtraConstant.KEY_DOWNLOAD_SIZE, conn.getContentLength());

                    InputStream is = conn.getInputStream();
                    File file = new File(getApplicationContext().getExternalCacheDir(), DownLoadManager.mFileName + mVersion);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);

                    byte[] buffer = new byte[1024];
                    int len;
                    int percent = 0;

                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);

                        percent += len;

                        sendProgressIntent(conn.getContentLength(), percent);
                    }

                    fos.close();
                    bis.close();
                    is.close();

                    AppConstant.DownLoadValues.IsDownLoadFinish = true;

                    sendSuccessIntent(file.toString());

                    stopSelf();
                }
            } catch (MalformedURLException e) {
                doFail();

                e.printStackTrace();
            } catch (IOException e) {
                doFail();

                e.printStackTrace();
            }
        }
    }

    private void doFail() {
        AppConstant.DownLoadValues.IsDownLoadFinish = true;

        SPUtils.getInstance().put(ExtraConstant.KEY_DOWNLOAD_SIZE, 0);

        manager.sendBroadcast(new Intent(AppConstant.DownLoadValues.DownLoadError));

        stopSelf();
    }

    private void sendProgressIntent(int all, int percent) {
        Intent intent = new Intent(AppConstant.DownLoadValues.DownLoading);
        intent.putExtra("All", all);
        intent.putExtra("Total", percent);

        manager.sendBroadcast(intent);
    }

    private void sendSuccessIntent(String file) {
        Intent intent = new Intent(AppConstant.DownLoadValues.DownLoadSuccess);
        intent.putExtra("Path", file);
        manager.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
