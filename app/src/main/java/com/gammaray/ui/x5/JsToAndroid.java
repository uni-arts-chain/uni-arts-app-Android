package com.gammaray.ui.x5;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.gammaray.ui.activity.art.ArtDetailActivity;
import com.gammaray.ui.activity.blindbox.BlindBoxDetailActivity;

/**
 * @Date:2021/6/16
 * @Author:Created by peter_ben
 */
public class JsToAndroid extends Object{

    private Context mContext;

    public JsToAndroid(Context context){
        this.mContext = context;
    }

    @JavascriptInterface
    public void NftDetail(int id){
        Intent intent = new Intent(mContext, ArtDetailActivity.class);
        intent.putExtra(ArtDetailActivity.ART_ID,id);
        mContext.startActivity(intent);
    }
    @JavascriptInterface
    public void MysteryBoxDetail(int id){
        Intent intent = new Intent(mContext, BlindBoxDetailActivity.class);
        intent.putExtra("id",String.valueOf(id));
        mContext.startActivity(intent);
    }
}
