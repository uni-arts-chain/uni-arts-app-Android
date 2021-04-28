package com.yunhualian.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by XuJun on 2017/09/04.
 */

public class StringUtils {

    public static String formatPhoneNumber(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = "+" + phoneNumber;

            int length = phoneNumber.length();

            if (length < 10)
                return phoneNumber;
            else
                return phoneNumber.substring(0, 6) + "****" + phoneNumber.substring(length - 4, length);
        }

        return "";
    }

    // 身份证中间替换为*
    public static String cardIDInvisibleMiddle(String cardID) {
        int length = cardID.length();

        if (!TextUtils.isEmpty(cardID))
            return cardID.substring(0, 1) + "****************" + cardID.substring(length - 1, length);

        return "";
    }

    // 只显示名字的第一个字
    public static String formatName(String name) {
        if (TextUtils.isEmpty(name))
            return "";
        else
            return name.substring(0, 1) + "**";
    }

    // 判断密码6-18位字母和数字的组合
    public static boolean isPasswordRight(String password) {
        String REGEX_PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,18}$";

        return Pattern.matches(REGEX_PASSWORD, password);
//        if (password.length() < 6 || password.length() > 24)
//            return false;
//        else
//            return true;
    }

    /**
     * 校验银行卡卡号
     * 校验过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    public static boolean checkBankCard(String bankCard) {
        int length = bankCard.length();

        if (length < 16 || length > 19)
            return false;

        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));

        if (bit == 'N')
            return false;

        return bankCard.charAt(length - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeBankCard
     * @return
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (null == nonCheckCodeBankCard || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+"))
            return 'N';

        char[] chs = nonCheckCodeBankCard.trim().toCharArray();

        int luhmSum = 0;

        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';

            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }

            luhmSum += k;
        }

        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static boolean isPhoneNumber(String mobile) {
        String REGEX_MOBILE = "^[1][3-9][0-9]{9}$";

        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    public static String formatUpperCase(String value) {
        if (TextUtils.isEmpty(value))
            return "";

        return value.toUpperCase();
    }

    public static String formatLowerCase(String value) {
        if (TextUtils.isEmpty(value))
            return "";

        return value.toLowerCase();
    }

    public static String getArrayValue(String[] arrayValue) {
        if (null == arrayValue)
            return "";

        StringBuffer stringBuffer = new StringBuffer();
        int size = arrayValue.length;

        for (int i = 0; i < size; i++) {
            if (i < size - 1)
                stringBuffer.append("" + (i + 1) + ". " + arrayValue[i] + "\n");
            else
                stringBuffer.append("" + (i + 1) + ". " + arrayValue[i]);
        }

        return stringBuffer.toString();
    }

    //邮箱验证
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    public static String getStringValue(String[] value) {
        if (null == value || 0 == value.length)
            return "";

        StringBuffer stringBuffer = new StringBuffer();
        int size = value.length;

        for (int i = 0; i < size; i++) {
            if (i < size - 1)
                stringBuffer.append(value[i] + ",");
            else
                stringBuffer.append(value[i]);
        }

        return stringBuffer.toString();
    }

    public static String[] getStringArray(String value) {
        if (TextUtils.isEmpty(value))
            return null;

        return value.split(",");
    }

    public static String getValueStr(String value) {
        String valueStr;

        if (TextUtils.isEmpty(value)) {
            valueStr = "";
        } else {
            if (value.startsWith("."))
                valueStr = "";
            else if (value.endsWith("."))
                valueStr = value.substring(0, value.length() - 1);
            else
                valueStr = value;
        }

        return valueStr;
    }

    public static String formatEmail(String value) {
        String email;

        if (TextUtils.isEmpty(value)) {
            email = "";
        } else {
            String emailHead;

            if (value.contains("@")) {
                int index = value.indexOf("@");

                emailHead = value.substring(0, index);
                String emailEnd = value.substring(index);

                if (TextUtils.isEmpty(emailHead)) {
                    email = "";
                } else {
                    int length = emailHead.length();

                    if (length > 1)
                        email = emailHead.substring(0, 2) + "***" + emailEnd;
                    else
                        email = emailHead + "***" + emailEnd;
                }
            } else {
                emailHead = value;

                int length = emailHead.length();

                if (length > 1)
                    email = emailHead.substring(0, 2) + "***";
                else
                    email = emailHead + "***";
            }
        }

        return email;
    }

    public static int getSize(String value, int bigSize, int smallSize) {
        return value.length() < 10 ? bigSize : smallSize;
    }

    public static int getSize(String value, int maxLength, int bigSize, int smallSize) {
        return value.length() < maxLength ? bigSize : smallSize;
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /*  * Java文件操作 获取不带扩展名的文件名  */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return "";
    }
}
