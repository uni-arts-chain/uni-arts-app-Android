package com.gammaray.utils;

import androidx.lifecycle.LifecycleOwner;


/**
 * @author Jcking
 * @time 2019/10/28 15:03
 */
public interface IView extends LifecycleOwner {

    /**
     * 显示loading
     */
    void showLoading();

    /**
     * 显示loading
     * 带信息
     *
     * @param msg 提示信息
     */
    void showLoading(String msg);

    /**
     * 隐藏loading
     */
    void dismissLoading();


}
