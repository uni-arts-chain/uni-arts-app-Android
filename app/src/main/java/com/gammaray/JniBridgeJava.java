/**
 * Copyright(c) Live2D Inc. All rights reserved.
 * <p>
 * Use of this source code is governed by the Live2D Open Software license
 * that can be found at https://www.live2d.com/eula/live2d-open-software-license-agreement_en.html.
 */

package com.gammaray;

import android.app.Activity;
import android.content.Context;

import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;

import org.greenrobot.eventbus.EventBus;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JniBridgeJava {

    private static final String LIBRARY_NAME = "Demo";
    private static Activity _activityInstance;
    private static Context _context;

    static {
        System.loadLibrary(LIBRARY_NAME);
    }

    // Native -----------------------------------------------------------------
    public static native void loadModelFile(String modelPath, String jsonModelName);

    public static native void nativeOnStart();

    public static native void nativeOnPause();

    public static native void nativeOnStop();

    public static native void nativeOnDestroy();

    public static native void nativeOnSurfaceCreated();

    public static native void nativeOnSurfaceChanged(int width, int height);

    public static native void nativeOnDrawFrame();

    public static native void nativeOnTouchesBegan(float pointX, float pointY);

    public static native void nativeOnTouchesEnded(float pointX, float pointY);

    public static native void nativeOnTouchesMoved(float pointX, float pointY);

    // Java -----------------------------------------------------------------

    public static void SetContext(Context context) {
        _context = context;
    }

    public static void SetActivityInstance(Activity activity) {
        _activityInstance = activity;
    }

    public static byte[] LoadFile(String filePath) {
        InputStream fileData = null;
        try {
//            String dir = Environment.getExternalStorageDirectory().getAbsolutePath()
//                    + File.separator.concat("Yunhualian/live2d/97");
            fileData = new FileInputStream(filePath);
//            fileData = _context.getAssets().open(filePath);
            int fileSize = fileData.available();
            byte[] fileBuffer = new byte[fileSize];
            fileData.read(fileBuffer, 0, fileSize);
            return fileBuffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileData != null) {
                    fileData.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] LoadFileFromDisk(String filePath) {
        InputStream fileData = null;
        try {
            if (filePath.contains("back_class") || filePath.contains("close") || filePath.contains("icon_gear") || filePath.contains("back_white")) {
                fileData = _context.getAssets().open(filePath);
            } else
                fileData = new FileInputStream(filePath);
//            fileData = _context.getAssets().open(filePath);
            int fileSize = fileData.available();
            byte[] fileBuffer = new byte[fileSize];
            fileData.read(fileBuffer, 0, fileSize);
            return fileBuffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileData != null) {
                    fileData.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void MoveTaskToBack() {
        EventBus.getDefault().post(new EventBusMessageEvent(EventEntity.EVENT_LIVE_CLOSE, null));
//        _activityInstance.finish();
//        _activityInstance.moveTaskToBack(true);
    }

}
