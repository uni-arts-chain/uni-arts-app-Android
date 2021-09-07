
package com.gammaray.base;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * Synopsis     activity管理
 * Author		Mosr
 * Version		${VERSION}
 * Create 	    2020-06-19 20:55:49
 * Email  		intimatestranger@sina.cn
 */
public class ActivityManager {
    private static Stack<Activity> activityStack;
    private volatile static ActivityManager instance;

    private ActivityManager() {

    }

    /**
     * 单一实例
     */
    public static ActivityManager getAppManager() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                    instance.activityStack = new Stack();
                }
            }

        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        try {
            Activity activity = activityStack.lastElement();
            return activity;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前Activity的前一个Activity
     */
    public Activity preActivity() {
        int index = activityStack.size() - 2;
        if (index < 0) {
            return null;
        }
        Activity activity = activityStack.get(index);
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (null != activityStack && !activityStack.isEmpty() && activityStack.contains(activity))
                activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束除Activity外其他Activity
     */
    public void finishExcludeActivity(Class<?> cls) {
        if (cls != null) {
            if (null != activityStack && !activityStack.isEmpty()) {
                Iterator<Activity> iterator = activityStack.iterator();
                if (null != iterator)
                    while (iterator.hasNext()) {
                        final Activity mActivity = iterator.next();

                        if (null != mActivity && !mActivity.getClass().equals(cls)) {
                            mActivity.finish();
                            iterator.remove();
                        }
                    }


            }

        }
    }

    /**
     * 结束除集合中的Activity外其他Activity
     */
    public void finishExcludeActivity(Class<?>... cls) {
        List<Class<?>> mClasss = Arrays.asList(cls);
        if (cls != null) {
            if (null != activityStack && !activityStack.isEmpty()) {
                Iterator<Activity> iterator = activityStack.iterator();
                if (null != iterator)
                    while (iterator.hasNext()) {
                        final Activity mActivity = iterator.next();

                        if (null != mActivity && !mClasss.contains(mActivity.getClass())) {
                            mActivity.finish();
                            iterator.remove();
                        }
                    }


            }

        }
    }

    /**
     * 结束除Activity外其他Activity
     */
    public Activity findActivity(String mClassName) {
        Activity rActivity = null;
        if (!TextUtils.isEmpty(mClassName)) {
            if (null != activityStack && !activityStack.isEmpty()) {

                for (Activity activity : activityStack) {
                    if (null != activity && mClassName.contains(activity.getClass().getSimpleName())) {
                        rActivity = activity;
                        break;
                    }
                }
            }
        }
        return rActivity;
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        try {
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    finishActivity(activity);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 返回到指定的activity
     *
     * @param cls
     */
    public void returnToActivity(Class<?> cls) {
        while (activityStack.size() != 0)
            if (activityStack.peek().getClass() == cls) {
                break;
            } else {
                finishActivity(activityStack.peek());
            }
    }


    /**
     * 是否已经打开指定的activity
     *
     * @param cls
     * @return
     */
    public boolean isOpenActivity(Class<?> cls) {
        if (activityStack != null) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (cls == activityStack.get(i).getClass()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否当前打开的activity
     *
     * @param cls
     * @return
     */
    public boolean isCurrentActivity(Class<?> cls) {
        if (activityStack != null) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (cls == activityStack.peek().getClass()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     *
     * @param context      上下文
     * @param isBackground 是否开开启后台运行
     */
    public void AppExit(Context context, Boolean isBackground) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
        } catch (Exception e) {

        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子
            if (!isBackground) {
                System.exit(0);
            }
        }
    }
}
