package com.gammaray.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SharedPreferances工具类
 * Created by zhangzhongqiang on 2015/10/15.
 */
public class SharedPreUtils {

    public static final String Key_UUID = "Uuid";
    public static final String key_Market_Tab = "KeyMarketTab";
    public static final String Key_Login_Type = "KeyLoginType";
    public static final String Key_Token = "token";
    public static final String Key_Phone = "KeyPhone";
    public static final String Key_Email = "KeyEmail";
    public static final String Key_DownLoad_Size = "DownLoadSize";
    public static final String Key_Close_Time = "CloseTime";
    public static final String Key_market_optional = "Optional_local_v3.0.0";
    public static final String Key_market_optional_edit = "Optional_edit";
    public static final String Key_search_history = "Search_history_new";
    public static final String Key_HomePage_Rise_List = "HomePageRiseLists";
    public static final String Key_HomePage_Fall_List = "HomePageFallLists";
    public static final String Key_HomePage_Deal_List = "HomePageDealLists";
    public static final String Key_HomePage_Block_List = "HomePageBlockLists";
    public static final String Key_HomePage_Banner_List = "HomePageBannerLists";
    public static final String Key_QuoteSearchVo = "QuoteSearchVoData";
    public static final String Key_TickerVo_List = "TickerVoListData";
    public static final String Key_TimeInterval = "TimeInterval";
    public static final String Key_CDNS = "Cdns";
    public static final String Key_CDNS_CURRENT = "CurrentCdns";
    public static final String Key_GUIDE_DATE = "GuideDate";
    public static final String KEY_PATTERN = "Pattern";
    public static final String KEY_LANGUAGE = "Language";
    public static final String KEY_GUIDE_FLAG = "GuideFlag";
    public static final String FIRST = "first";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_HEAD_URL = "head_url";
    public static final String KEY_SEED = "seed";
    public static final String KEY_NONCE = "nonce";
    public static final String KEY_PRIVATE = "private_key";
    public static final String KEY_PUBLICKEY = "public_key";
    public static final String KEY_PIN = "pin";
    public static final String KEY_MNENONIC = "mnemonic";

    /**
     * 序列化对象
     *
     * @param object
     * @return
     * @throws IOException
     */
    public static String serialize(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();

        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object deSerialization(String str) throws IOException, ClassNotFoundException {
        String redStr = "";
        if (str != null) {
            redStr = java.net.URLDecoder.decode(str, "UTF-8");
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();

        return object;
    }

    public static void saveObject(String strObject, Context context, String name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(name, strObject);
        edit.commit();
    }

    public static String getObject(Context context, String name) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(name, null);
    }

    public static void setInteger(Context context, String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInteger(Context context, String key, int defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defaultValue);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, "");
    }

    public static void setLong(Context context, String key, long value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defaultValue);
    }

    public static boolean setStringSet(Context context, String key, Set<String> values) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
        editor.putStringSet(key, values);
        return editor.commit();
    }

    public static Set<String> getStringSet(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getStringSet(key, new HashSet<String>());
    }

    /**
     * 清空SharePreference
     */
    public static void clearData(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }


    private final static String PREFERENCE_NAME = "superservice_ly";
    private final static String SEARCH_HISTORY = "linya_history";

    // 保存搜索记录
    public static void saveSearchHistory(Context context, String inputText) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(inputText)) {
            return;
        }
        String longHistory = sp.getString(SEARCH_HISTORY, "");  //获取之前保存的历史记录
        String[] tmpHistory = longHistory.split(","); //逗号截取 保存在数组中
        List<String> historyList = new ArrayList<String>(Arrays.asList(tmpHistory)); //将改数组转换成ArrayList
        SharedPreferences.Editor editor = sp.edit();
        if (historyList.size() > 0) {
            //1.移除之前重复添加的元素
            for (int i = 0; i < historyList.size(); i++) {
                if (inputText.equals(historyList.get(i))) {
                    historyList.remove(i);
                    break;
                }
            }
            historyList.add(0, inputText); //将新输入的文字添加集合的第0位也就是最前面(2.倒序)
            if (historyList.size() > 8) {
                historyList.remove(historyList.size() - 1); //3.最多保存8条搜索记录 删除最早搜索的那一项
            }
            //逗号拼接
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < historyList.size(); i++) {
                sb.append(historyList.get(i) + ",");
            }
            //保存到sp
            editor.putString(SEARCH_HISTORY, sb.toString());
            editor.commit();
        } else {
            //之前未添加过
            editor.putString(SEARCH_HISTORY, inputText + ",");
            editor.commit();
        }
    }


    public static void deleteSearchHistory(Context context, String inputText) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(inputText)) {
            return;
        }
        String longHistory = sp.getString(SEARCH_HISTORY, "");  //获取之前保存的历史记录
        String[] tmpHistory = longHistory.split(","); //逗号截取 保存在数组中
        List<String> historyList = new ArrayList<String>(Arrays.asList(tmpHistory)); //将改数组转换成ArrayList
        SharedPreferences.Editor editor = sp.edit();
        if (historyList.size() > 0) {
            //1.移除之前重复添加的元素
            for (int i = 0; i < historyList.size(); i++) {
                if (inputText.equals(historyList.get(i))) {
                    historyList.remove(i);
                    break;
                }
            }
            if (historyList.size() > 8) {
                historyList.remove(historyList.size() - 1); //3.最多保存8条搜索记录 删除最早搜索的那一项
            }
            //逗号拼接
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < historyList.size(); i++) {
                sb.append(historyList.get(i) + ",");
            }
            //保存到sp
            editor.putString(SEARCH_HISTORY, sb.toString());
            editor.commit();
        } else {
            //之前未添加过
            editor.putString(SEARCH_HISTORY, inputText + ",");
            editor.commit();
        }
    }

    //获取搜索记录
    public static List<String> getSearchHistory(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String longHistory = sp.getString(SEARCH_HISTORY, "");
        String[] tmpHistory = longHistory.split(","); //split后长度为1有一个空串对象
        List<String> historyList = new ArrayList<String>(Arrays.asList(tmpHistory));
        if (historyList.size() == 1 && historyList.get(0).equals("")) { //如果没有搜索记录，split之后第0位是个空串的情况下
            historyList.clear();  //清空集合，这个很关键
        }
        return historyList;
    }

    //清空记录
    public static void clearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String longHistory = sp.getString(SEARCH_HISTORY, "");  //获取之前保存的历史记录
        SharedPreferences.Editor editor = sp.edit();
        //保存到sp
        editor.putString(SEARCH_HISTORY, "");
        editor.commit();
    }
}
