package com.gammaray.eth.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.gammaray.eth.base.C;
import com.gammaray.eth.entity.GasSettings;
import com.gammaray.eth.util.WalletDaoUtils;

import java.math.BigInteger;

public class SharedPreferenceRepository {

    private static final String CURRENT_ACCOUNT_ADDRESS_KEY = "current_account_address";
    private static final String DEFAULT_NETWORK_NAME_KEY = "default_network_name";
    private static final String GAS_PRICE_KEY = "gas_price";
    private static final String GAS_LIMIT_KEY = "gas_limit";
    private static final String GAS_LIMIT_FOR_TOKENS_KEY = "gas_limit_for_tokens";
    private static final String CURRENCY_UNIT = "currencyUnit";
    private static final String PSW = "psw";
    private static final String PUSH = "push";
    private static final String ADDR = "addr";
    private final SharedPreferences pref;

    private static SharedPreferenceRepository sSelf;


    public static SharedPreferenceRepository init(Context context) {
        if (sSelf == null) {
            sSelf = new SharedPreferenceRepository(context);
        }
        return sSelf;
    }

    private SharedPreferenceRepository(Context context) {
        pref = context.getSharedPreferences(context.getPackageName() + "_preference", Context.MODE_MULTI_PROCESS);
    }

    public static SharedPreferenceRepository instance(Context context) {
        return init(context);
    }

    ;

    public String getCurrentWalletAddress() {

        return WalletDaoUtils.getCurrent().getAddress();
//		return pref.getString(CURRENT_ACCOUNT_ADDRESS_KEY, null);
    }

    public void setCurrentWalletAddress(String address) {
        pref.edit().putString(CURRENT_ACCOUNT_ADDRESS_KEY, address).apply();
    }

    public void setLockPsw(String psw) {
        pref.edit().putString(PSW, psw).apply();
    }

    public void setWalletRegist(String addr, int value) {
        pref.edit().putInt(addr, value).apply();
    }

    public int getWalletRegist(String addr) {
        return pref.getInt(addr, 0);
    }

    public void setPushSwitch(int on) {
        pref.edit().putInt(PUSH, on).apply();
    }

    public Integer getPushSwitch() {
        return pref.getInt(PUSH, 1);
    }

    public String getLockPsw() {
        return pref.getString(PSW, null);
    }

    public int getCurrencyUnit() {
        return pref.getInt(CURRENCY_UNIT, 0);
    }

    public void setCurrencyUnit(int currencyUnit) {
        pref.edit().putInt(CURRENCY_UNIT, currencyUnit).apply();
    }

    public String getDefaultNetwork() {
        return pref.getString(DEFAULT_NETWORK_NAME_KEY, null);
    }

    public void setDefaultNetwork(String netName) {
        pref.edit().putString(DEFAULT_NETWORK_NAME_KEY, netName).apply();
    }

    public GasSettings getGasSettings(boolean forTokenTransfer) {
        BigInteger gasPrice = new BigInteger(pref.getString(GAS_PRICE_KEY, C.DEFAULT_GAS_PRICE));
        BigInteger gasLimit = new BigInteger(pref.getString(GAS_LIMIT_KEY, C.DEFAULT_GAS_LIMIT));
        if (forTokenTransfer) {
            gasLimit = new BigInteger(pref.getString(GAS_LIMIT_FOR_TOKENS_KEY, C.DEFAULT_GAS_LIMIT_FOR_TOKENS));
        }

        return new GasSettings(gasPrice, gasLimit);
    }

    public void setGasSettings(GasSettings gasSettings) {
        pref.edit().putString(GAS_PRICE_KEY, gasSettings.gasPrice.toString()).apply();
        pref.edit().putString(GAS_PRICE_KEY, gasSettings.gasLimit.toString()).apply();
    }
}
