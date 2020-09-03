package com.yunhualian.base;


import androidx.appcompat.widget.Toolbar;


import com.yunhualian.R;

import java.io.Serializable;

/**
 * Synopsis     ToolBarOptions
 * Author		Mosr
 * Version		${VERSION}
 * Create 	    2020-06-19 20:55:49
 * Email  		intimatestranger@sina.cn
 */
public class ToolBarOptions implements Serializable {
    public int titleTxtColor = YunApplication.getInstance().getResources().getColor(R.color._333333);
    /**
     * toolbar的title资源id
     */
    public int titleId = 0;
    /**
     * toolbar的title
     */
    public String titleString;
    /**
     * toolbar的logo资源id
     */
    public int logoId = 0;

    /**
     * toolbar的右侧文字资源id
     */
    public int rightTextString  = 0;

    /**
     * toolbar的右侧图片资源id
     */
    public int rightPicRes = 0;

    /**
     * toolbar的右侧文字点击事件
     */
    public Toolbar.OnMenuItemClickListener onRightTextClick;

    /**
     * toolbar的右侧图片点击事件
     */
    public Toolbar.OnMenuItemClickListener onRightPicClick;



    /**
     * toolbar的返回按钮资源id，默认开启的资源nim_actionbar_dark_back_icon
     */
//    public int navigateId = R.drawable.nim_actionbar_dark_back_icon;
//    public int     navigateId     = R.mipmap.app_btn_back;
//    public int     closeId        = R.mipmap.icon_back_red_packet;
//    public int     backId        = R.mipmap.icon_redpacket_back;

    public int navigateId = R.mipmap.icon_back_dark;
    public int closeId = 0;
    public int backId = 0;
    /**
     * .
     * toolbar的返回按钮，默认开启
     */
    public boolean isNeedNavigate = true;
    public boolean isNeedClose = false;
    public boolean isNeedBack = false;
}
