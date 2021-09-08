package com.gammaray.eth.service;


import com.gammaray.eth.entity.LoginBean;
import com.gammaray.eth.entity.MsgCode;

import io.reactivex.Observable;
import okhttp3.RequestBody;

public interface RequestService {


//    Observable<RegiseResponse> registerPushQ(RegisterPush_Q wallet);
//
//    Observable<RegiseResponse> registerPushE(RegisterPush_E wallet);


    Observable<MsgCode> getCode(String type, String action, String value);

    Observable<LoginBean> login(String loginby, String code);

    Observable<LoginBean> bind(String header,String type, String code);

    Observable<LoginBean> getMemberInfo(String header);

    Observable<LoginBean> logOut(String header);

    Observable<LoginBean> upLoadImg(String header, RequestBody requestBody);
}
