package com.gammaray.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.BuildConfig;
import com.gammaray.MainActivity;
import com.gammaray.R;
import com.gammaray.base.ActivityManager;
import com.gammaray.base.YunApplication;
import com.gammaray.constant.AppConstant;
import com.gammaray.constant.ExtraConstant;
import com.gammaray.entity.AppUpdateVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.service.DownLoadService;
import com.gammaray.widget.UpdateDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

import static com.blankj.utilcode.util.StringUtils.getString;

/**
 * Synopsis     com.miner.client.utils.DownLoadManager
 * Author		Mosr
 * Version		1.0.0
 * Create 	    2020/7/14 21:48
 * Email  		intimatestranger@sina.cn
 */
public class DownLoadManager {
    private static boolean mUpdateFlag = false;
    public static final String mFileName = "UniArtClient";
    private static final int MSG_DOWNLOAD_ERROR = 0x1001;
    private static volatile DownLoadManager mDownLoadManager;
    private final static int REQUEST_CODE_ASK_WRITE_EXTERNAL_STORAGE = 0x1002;


    private LocalBroadcastManager mLocalBroadcastManager;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;
    private ProgressDialog mProgressDialog;
    private MainHandler mHandler;

    private List<String> mLackedPermission = new ArrayList<>();
    private static String mUrl;
    private static String mVersion;
    private static String mVersionName;
    private static String mDesc;


    public static DownLoadManager with() {
        if (null == mDownLoadManager) {
            synchronized (PopupwindowUtil.class) {
                if (null == mDownLoadManager) {
                    mDownLoadManager = new DownLoadManager();
                }
            }
        }
        return mDownLoadManager;
    }

    public void init(MainActivity mMainActivity) {
        mHandler = new MainHandler(mMainActivity);
        mProgressDialog = new ProgressDialog(mMainActivity/*, android.R.style.Theme_Material_Light_Dialog*/);
//        if (null != mProgressDialog)
//            mProgressDialog.setProgressDrawable(new ClipDrawable(drawSystemBar(Color.WHITE, YunApplication.getInstance().getResources().getColor(R.color.colorAccent)), ClipDrawable.VERTICAL, ClipDrawable.HORIZONTAL));

        checkUpdate();
    }

    private Drawable drawSystemBar(@NonNull int StartColor, @NonNull int EndColor) {
        GradientDrawable mGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{StartColor,
                        EndColor});
        mGradientDrawable.setGradientCenter(0, 0);
        mGradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        mGradientDrawable.setShape(GradientDrawable.RECTANGLE);
        mGradientDrawable.setGradientRadius(mProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getWidth() / 2);
        return mGradientDrawable;
    }

    private MainActivity getActivity() {
        return null != mHandler && null != mHandler.mActivityRef && null != mHandler.mActivityRef.get() && !mHandler.mActivityRef.get().isFinishing() ? mHandler.mActivityRef.get() : null;
    }

    private void checkStatus() {
        final MainActivity mMainActivity = getActivity();
        if (FileUtils.isFileExists(YunApplication.getInstance().getExternalCacheDir() + File.separator + mFileName + mVersion)) {
            long size = FileUtils.getFileLength(YunApplication.getInstance().getExternalCacheDir() + File.separator + mFileName + mVersion);
            long saveSize = SPUtils.getInstance().getInt(ExtraConstant.KEY_DOWNLOAD_SIZE, 0);
            if (size != 0 && saveSize != 0 && size == saveSize) {
                if (mUpdateFlag) {

                    if (null != mMainActivity && !mMainActivity.isFinishing()) {
                        if (null == mMainActivity.mForceDialog)
                            mMainActivity.mForceDialog = new UpdateDialog(mMainActivity, mVersionName, mDesc, YunApplication.getInstance().getResources().getString(R.string.version_install),
                                    mUpdateFlag, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMainActivity.mForceDialog.dismiss();
                                    installApk();
                                }
                            });

                        mMainActivity.mForceDialog.setUpdateButton(getString(R.string.version_install), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMainActivity.mForceDialog.dismiss();
                                installApk();
                            }
                        });

                        if (null != mMainActivity.mForceDialog && !mMainActivity.mForceDialog.isShowing())
                            mMainActivity.mForceDialog.show();
                    }
                } else {
                    if (null != mMainActivity && !mMainActivity.isFinishing() && AppConstant.DownLoadValues.IsDownLoadDialogShow) {
                        mMainActivity.mDialog = new UpdateDialog(mMainActivity, mVersionName, mDesc, YunApplication.getInstance().getResources().getString(R.string.version_install),
                                mUpdateFlag, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mMainActivity.mDialog.dismiss();

                                DownLoadManager.this.installApk();
                            }
                        });

                        if (null != mMainActivity.mDialog && !mMainActivity.mDialog.isShowing())
                            mMainActivity.mDialog.show();
                    }
                }
            } else {
                deleteFile();

                if (mUpdateFlag) {
                    if (null != mMainActivity && !mMainActivity.isFinishing()) {
                        if (null == mMainActivity.mForceDialog)
                            mMainActivity.mForceDialog = new UpdateDialog(mMainActivity, mVersionName, mDesc, YunApplication.getInstance().getResources().getString(R.string.version_update),
                                    mUpdateFlag, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mMainActivity.mForceDialog.dismiss();

                                    if (NetworkUtils.isConnected())
                                        DownLoadManager.this.checkPermission();
                                    else
                                        ToastUtils.showShort(R.string.net_word_error);
                                }
                            });

                        if (null != mMainActivity.mForceDialog && !mMainActivity.mForceDialog.isShowing())
                            mMainActivity.mForceDialog.show();
                    }
                } else {
                    if (null != mMainActivity && !mMainActivity.isFinishing() && AppConstant.DownLoadValues.IsDownLoadDialogShow) {
                        mMainActivity.mDialog = new UpdateDialog(mMainActivity, mVersionName, mDesc, YunApplication.getInstance().getResources().getString(R.string.version_update),
                                mUpdateFlag, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mMainActivity.mDialog.dismiss();

                                if (NetworkUtils.isConnected())
                                    DownLoadManager.with().checkPermission();
                                else
                                    ToastUtils.showShort(R.string.net_word_error);
                            }
                        });

                        if (null != mMainActivity.mDialog && !mMainActivity.mDialog.isShowing())
                            mMainActivity.mDialog.show();
                    }
                }
            }
        } else {
            if (mUpdateFlag) {
                if (null != mMainActivity && !mMainActivity.isFinishing()) {
                    if (null == mMainActivity.mForceDialog)
                        mMainActivity.mForceDialog = new UpdateDialog(mMainActivity, mVersionName, mDesc, YunApplication.getInstance().getResources().getString(R.string.version_update),
                                mUpdateFlag, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mMainActivity.mForceDialog.dismiss();

                                if (NetworkUtils.isConnected())
                                    DownLoadManager.with().checkPermission();
                                else
                                    ToastUtils.showShort(R.string.net_word_error);
                            }
                        });

                    if (null != mMainActivity.mForceDialog && !mMainActivity.mForceDialog.isShowing())
                        mMainActivity.mForceDialog.show();
                }
            } else {
                if (null != mMainActivity && !mMainActivity.isFinishing() && AppConstant.DownLoadValues.IsDownLoadDialogShow) {
                    mMainActivity.mDialog = new UpdateDialog(mMainActivity, mVersionName, mDesc, YunApplication.getInstance().getResources().getString(R.string.version_update),
                            mUpdateFlag, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mMainActivity.mDialog.dismiss();

                            if (NetworkUtils.isConnected())
                                DownLoadManager.with().checkPermission();
                            else
                                ToastUtils.showShort(R.string.net_word_error);
                        }
                    });

                    if (null != mMainActivity.mDialog && !mMainActivity.mDialog.isShowing())
                        mMainActivity.mDialog.show();
                }
            }
        }

        if (!mUpdateFlag)
            AppConstant.DownLoadValues.IsDownLoadDialogShow = false;
    }

    public void onDestory() {
        if (null != mLocalBroadcastManager)
            mLocalBroadcastManager.unregisterReceiver(mReceiver);
    }

    private void initBroadcast() {
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(YunApplication.getInstance());

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstant.DownLoadValues.DownLoading);
        filter.addAction(AppConstant.DownLoadValues.DownLoadError);
        filter.addAction(AppConstant.DownLoadValues.DownLoadSuccess);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null == mProgressDialog)
                    return;

                if (!mProgressDialog.isShowing())
                    return;

                if (TextUtils.equals(intent.getAction(), (AppConstant.DownLoadValues.DownLoadSuccess))) {
                    mProgressDialog.dismiss();
                    installApk();
                } else if (TextUtils.equals(intent.getAction(), (AppConstant.DownLoadValues.DownLoadError))) {
                    mProgressDialog.dismiss();

                    if (null != mHandler)
                        mHandler.sendEmptyMessage(MSG_DOWNLOAD_ERROR);
                } else {
                    final int max = intent.getIntExtra("All", 0);
                    final int total = intent.getIntExtra("Total", 0);

                    mProgressDialog.setMax(max);
                    mProgressDialog.setProgress(total);
                    mProgressDialog.setProgressNumberFormat(String.format(Locale.getDefault(), "%.2fM/%.2fM", (float) (total / (1024.00 * 1024.00)), (float) (max / (1024.00 * 1024.00))));
                }
            }
        };

        mLocalBroadcastManager.registerReceiver(mReceiver, filter);
    }

    private void downLoadApk() {
        if (null == mProgressDialog || mProgressDialog.isShowing())
            return;

        /*DisplayMetrics metric = new DisplayMetrics();
        if (null != mProgressDialog.getWindow() && null != mProgressDialog.getWindow().getWindowManager()) {
            mProgressDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
            WindowManager.LayoutParams p = mProgressDialog.getWindow().getAttributes();
            p.width = (int) (metric.widthPixels * 0.8);
            mProgressDialog.getWindow().setAttributes(p);
        }*/
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(YunApplication.getInstance().getResources().getString(R.string.version_download_progress));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        setWinWidth(mProgressDialog);

        initBroadcast();

        Intent intent = new Intent(YunApplication.getInstance(), DownLoadService.class);
        intent.putExtra("Path", mUrl);
        intent.putExtra("VerTxt", mVersion);

        YunApplication.getInstance().startService(intent);
    }

    private void setWinWidth(Dialog mDialog) {
        if (null == mDialog) return;
        DisplayMetrics metric = new DisplayMetrics();
        if (null == mDialog.getWindow()) return;
        if (null == mDialog.getWindow().getWindowManager()) return;
        mDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
        WindowManager.LayoutParams p = mDialog.getWindow().getAttributes();
        p.width = (int) (metric.widthPixels * 0.95);
        mDialog.getWindow().setAttributes(p);
    }

    private void installApk() {
        File file;

        try {
            file = new File(YunApplication.getInstance().getExternalCacheDir(), mFileName + AppConstant.DownLoadValues.DownLoadVersion);
        } catch (Exception e) {
            file = null;

            e.printStackTrace();
        }

        long size = FileUtils.getLength(file);
        long saveSize = SPUtils.getInstance().getInt(ExtraConstant.KEY_DOWNLOAD_SIZE, 0);

        if (file != null && size != 0 && saveSize != 0 && size == saveSize) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                String mAuthority;
                if (!TextUtils.isEmpty(mAuthority = getFileProviderAuthority()))
                    intent.setDataAndType(FileProvider.getUriForFile(YunApplication.getInstance(), mAuthority, file), "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }

            if (YunApplication.getInstance().getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                YunApplication.getInstance().startActivity(intent);
                if (mUpdateFlag)
                    ActivityManager.getAppManager().finishAllActivity();
            }
        } else {
            deleteFile();

            if (null != mHandler && null != mHandler.mActivityRef && null != mHandler.mActivityRef.get() && !mHandler.mActivityRef.get().isFinishing())
                DialogManager.showConfirmDialog(mHandler.mActivityRef.get(),
                        YunApplication.getInstance().getResources().getString(R.string.dialog_title),
                        YunApplication.getInstance().getResources().getString(R.string.version_error),
                        YunApplication.getInstance().getResources().getString(R.string.canel),
                        YunApplication.getInstance().getResources().getString(R.string.determine), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mUpdateFlag)
                                    ActivityManager.getAppManager().finishActivity(MainActivity.class);
                                else
                                    dialog.dismiss();

                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (NetworkUtils.isConnected())
                                    checkPermission();
                                else
                                    ToastUtils.showShort(R.string.net_word_error);

                            }
                        });
        }
    }

    private void deleteFile() {
        try {
            FileUtils.delete(YunApplication.getInstance().getExternalCacheDir() + File.separator + mFileName + AppConstant.DownLoadValues.DownLoadVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFileProviderAuthority() {
        try {
            for (ProviderInfo provider : YunApplication.getInstance().getPackageManager().getPackageInfo(YunApplication.getInstance().getPackageName(), PackageManager.GET_PROVIDERS).providers) {
                if (FileProvider.class.getName().equals(provider.name) && provider.authority.endsWith(BuildConfig.APPLICATION_ID + ".fileprovider")) {
                    return provider.authority;
                }
            }
        } catch (PackageManager.NameNotFoundException ignore) {

        }
        return null;
    }

    private void checkPermission() {
        if (NetworkUtils.isConnected()) {
            if (Build.VERSION.SDK_INT >= 23) {

                if (!PermissionUtils.isGranted(PermissionConstants.getPermissions(PermissionConstants.STORAGE))) {
                    mLackedPermission.add(PermissionConstants.STORAGE);
                }
                if (null != mLackedPermission && !mLackedPermission.isEmpty()) {
                    PermissionUtils.permission(mLackedPermission.toArray(new String[0])).callback(new PermissionUtils.FullCallback() {
                        @Override
                        public void onGranted(List<String> permissionsGranted) {
                            if (permissionsGranted.containsAll(Arrays.asList(PermissionConstants.getPermissions(PermissionConstants.STORAGE)))) {
                                downLoadApk();
                            }
                        }

                        @Override
                        public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        }
                    }).request();
                    return;
                }

                downLoadApk();

            } else {
                downLoadApk();
            }
        }
    }

    private static class MainHandler extends Handler {

        private WeakReference<MainActivity> mActivityRef;

        public MainHandler(MainActivity activity) {
            mActivityRef = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (null == mActivityRef.get())
                return;

            final MainActivity activity = mActivityRef.get();

            switch (msg.what) {
                case MSG_DOWNLOAD_ERROR:
                    ToastUtils.showShort(R.string.version_download_error);

                    if (mUpdateFlag) {
                        if (!activity.isFinishing() && AppConstant.DownLoadValues.IsDownLoadDialogShow) {
                            if (null == activity.mDialog)
                                activity.mDialog = new UpdateDialog(activity, mVersionName, mDesc,
                                        YunApplication.getInstance().getResources().getString(R.string.version_update),
                                        mUpdateFlag, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        activity.mDialog.dismiss();

                                        if (NetworkUtils.isConnected()) {
                                            DownLoadManager.with().deleteFile();
                                            DownLoadManager.with().checkPermission();
                                        } else {
                                            ToastUtils.showShort(R.string.net_word_error);
                                        }

                                    }
                                });

                            if (null != activity.mDialog && !activity.mDialog.isShowing())
                                activity.mDialog.show();
                        }
                    } else {
                        if (!activity.isFinishing()) {
                            if (null == activity.mForceDialog)
                                activity.mForceDialog = new UpdateDialog(activity, mVersionName, mDesc,
                                        YunApplication.getInstance().getResources().getString(R.string.version_update),
                                        mUpdateFlag, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        activity.mForceDialog.dismiss();

                                        if (NetworkUtils.isConnected()) {
                                            DownLoadManager.with().deleteFile();
                                            DownLoadManager.with().checkPermission();
                                        } else {
                                            ToastUtils.showShort(R.string.net_word_error);
                                        }

                                    }
                                });

                            if (null != activity.mForceDialog && !activity.mForceDialog.isShowing())
                                activity.mForceDialog.show();
                        }
                    }

                    break;
            }
        }
    }


    private void checkUpdate() {
        RequestManager.instance().checkUpdate("andr", BuildConfig.VERSION_CODE, new MinerCallback<BaseResponseVo<AppUpdateVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<AppUpdateVo>> call, Response<BaseResponseVo<AppUpdateVo>> response) {
                if (null != response.body() && null != response.body().getBody()) {
                    AppUpdateVo mAppUpdateVo = response.body().getBody();

                    if (null == mAppUpdateVo || null == mAppUpdateVo.getDownload_url() || TextUtils.isEmpty(mAppUpdateVo.getVersion_code())) {
                        AppConstant.DownLoadValues.IsNeedDownLoad = false;
                    } else {
                        mUpdateFlag = mAppUpdateVo.isForce_updated();
                        mVersion = String.valueOf(mAppUpdateVo.getVersion_code());
                        mVersionName = mAppUpdateVo.getVersion_name();
                        mDesc = mAppUpdateVo.getDesc();
                        String url = null != mAppUpdateVo.getDownload_url() ? mAppUpdateVo.getDownload_url().getUrl() : "";
                        if (TextUtils.isEmpty(url)) {
                            AppConstant.DownLoadValues.IsNeedDownLoad = false;
                        } else {
                            mUrl = url;
                            AppConstant.DownLoadValues.IsNeedDownLoad = true;
                            AppConstant.DownLoadValues.DownLoadUrl = mUrl;
                            AppConstant.DownLoadValues.DownLoadVersion = mVersion;
                            checkStatus();
                            SPUtils.getInstance().put(ExtraConstant.KEY_CLOSE_TIME, System.currentTimeMillis());
                        }

                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<AppUpdateVo>> call, Response<BaseResponseVo<AppUpdateVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }
}
