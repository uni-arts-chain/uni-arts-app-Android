package com.yunhualian.service;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by gengda on 2018/3/16.
 */

public interface MarketApi {

    /**
     * 委托挂单
     *
     * @return
     */
    @GET("/api/v1/arts/search")
    Call<DTResponse> marketDepth(@QueryMap HashMap<String, String> map);

    /**
     * 成交回报
     *
     * @return
     */
    @GET("/api/v2/trades")
    Call<DTResponse> marketTrades(@QueryMap HashMap<String, String> map);

    /**
     * ticker
     */
    @GET("/api/v2/tickers")
    Call<DTResponse> tickers();

    /**
     * 单币种ticker
     */
    @GET("/api/v2/tickers/{market}")
    Call<DTResponse> singleTickers(@Path("market") String market);

    /**
     * 获取显示Tab
     */
    @GET("/api/v2/markets/tabs")
    Call<DTResponse> getTabs();

    /**
     * 获取首页市场推荐
     *
     * @return
     */
    @GET("/api/v2/home_markets")
    Call<DTResponse> getHomeMarkets();

    @GET("/api/v2/currency/information")
    Call<DTResponse> getInformation(@QueryMap HashMap<String, String> map);

}
