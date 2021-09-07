package com.gammaray.ui.activity.video;


import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.gammaray.R;
import com.gammaray.base.VideoPlayerBaseActivity;
import com.gammaray.widget.MediaController;

public class VideoPlayerActivity extends VideoPlayerBaseActivity {
    private PLVideoView mVideoView;
    private int mDisplayAspectRatio = PLVideoView.ASPECT_RATIO_FIT_PARENT;
    private TextView mStatInfoTextView;
    private MediaController mMediaController;
    String videoPath;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        if(getIntent() != null){
            videoPath = getIntent().getStringExtra("art_video_path");
        }
        mVideoView = findViewById(R.id.VideoView);
        button = findViewById(R.id.close_video);
        button.setOnClickListener(v -> finish());
        mVideoView.setVideoPath(videoPath);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mMediaController = new MediaController(this, false, false);
        mMediaController.setOnClickSpeedAdjustListener(mOnClickSpeedAdjustListener);
        mVideoView.setMediaController(mMediaController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("onResume");
        mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("onPause");
        mMediaController.getWindow().dismiss();
        mVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }

    private PLOnErrorListener mOnErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            Log.e("TAG", "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                    /**
                     * SDK will do reconnecting automatically
                     */
                    return false;
                case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                    ToastUtils.showShort("failed to open player !");
                    break;
                case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                    ToastUtils.showShort("failed to seek !");
                    return true;
                case PLOnErrorListener.ERROR_CODE_CACHE_FAILED:
                    ToastUtils.showShort("failed to cache url !");
                    break;
                default:
                    ToastUtils.showShort("unknown error !");
                    break;
            }
            finish();
            return true;
        }
    };
    private MediaController.OnClickSpeedAdjustListener mOnClickSpeedAdjustListener = new MediaController.OnClickSpeedAdjustListener() {
        @Override
        public void onClickNormal() {
            // 0x0001/0x0001 = 2
            mVideoView.setPlaySpeed(0X00010001);
        }

        @Override
        public void onClickFaster() {
            // 0x0002/0x0001 = 2
            mVideoView.setPlaySpeed(0X00020001);
        }

        @Override
        public void onClickSlower() {
            // 0x0001/0x0002 = 0.5
            mVideoView.setPlaySpeed(0X00010002);
        }
    };
}