package com.yunhualian.net;

import com.yunhualian.service.API;

import java.util.HashMap;

/**
 * Created by Yanmin on 2016/3/15.
 */
public class DTRequest {
    public API _api;
    public HashMap<String, String> _params;
    public boolean _silent;
    public boolean _showErrorMsg;

    public DTRequest(API api, HashMap<String, String> params, boolean silent, boolean showErrorMsg) {
        this._api = api;
        this._params = params;
        this._silent = silent;
        this._showErrorMsg = showErrorMsg;
    }

    public API getApi() {
        return _api;
    }

    public HashMap<String, String> getParams() {
        return _params;
    }

    public boolean isSilent() {
        return _silent;
    }

    public boolean isShowErrorMsg() {
        return _showErrorMsg;
    }
}
