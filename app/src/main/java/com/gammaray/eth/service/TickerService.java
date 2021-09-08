package com.gammaray.eth.service;


import com.gammaray.eth.entity.AddTokenBean;
import com.gammaray.eth.entity.MsgCode;
import com.gammaray.eth.entity.RegiseResponse;
import com.gammaray.eth.entity.RegistPushBean;
import com.gammaray.eth.entity.Ticker;

import java.util.List;

import io.reactivex.Observable;

public interface TickerService {

    Observable<List<Ticker.TickersBean>> fetchTickerPrice(String symbols, String currency);

//    Observable<RegiseResponse> registerPushQ(RegisterPush_Q wallet);
//
//    Observable<RegiseResponse> registerPushE(RegisterPush_E wallet);

    Observable<RegiseResponse> registerPush(RegistPushBean wallet);

    Observable<MsgCode> getCode(String type, String value);

    Observable<List<AddTokenBean>> getTokens(String keyword, String type);
}
