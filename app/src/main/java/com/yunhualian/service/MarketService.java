package com.yunhualian.service;


import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.User;
import com.yunhualian.net.CommonConstants;
import com.yunhualian.net.IService;
import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.MD5Encrypt;
import com.yunhualian.utils.ParamsBuilder;
import com.yunhualian.utils.StringUtils;

import java.util.HashMap;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

/**
 * Created by gengda on 2018/3/16.
 */
public class MarketService extends IService<MarketApi> {

    public MarketService() {
        super(AppConstant.ADDRESS, true);
    }

    public static MarketService getInstance() {
        return (MarketService) getInstance(MarketService.class);
    }

    protected Interceptor addHeader() {
        Interceptor interceptor = chain -> {
            Request.Builder builder = chain.request().newBuilder();
            Request request = mRequest = builder.build();

            if (YunApplication.isLogin()) {
                mParamsWithToken.put(StringUtils.formatLowerCase(CommonConstants.HttpHeadConfig.TONCE), DateUtil.getCurrentSecondTime());

                mParamsWithToken = MD5Encrypt.orderByASC(mParamsWithToken);

                request = request.newBuilder()
                        .addHeader(CommonConstants.HttpHeadConfig.AUTHORIZATION, YunApplication.getToken())
                        .addHeader(CommonConstants.HttpHeadConfig.SIGN,
                                MD5Encrypt.HMACSHA256(ParamsBuilder.getSign(request, mParamsWithToken), YunApplication.getmUserVo().getExpire_at()))
                        .build();
            } else {
                request = request.newBuilder()
                        .removeHeader(CommonConstants.HttpHeadConfig.AUTHORIZATION)
                        .build();
            }

            Response response = chain.proceed(request);

            return response;
        };

        return interceptor;
    }


    public API MarketDepth = new API<List<SellingArtVo>>("MarketDepth", AppConstant.ADDRESS) {
        @Override
        public Call<DTResponse> request(HashMap<String, String> params) {
            return mApi.marketDepth(params);
        }
    };

//    public API MarketTrades = new API<List<TradeVo>>("MarketTrades", AppConstant.ADDRESS) {
//        @Override
//        public Call<DTResponse> request(HashMap<String, String> params) {
//            return mApi.marketTrades(params);
//        }
//    };
//
//    public API Tickers = new API<List<TickerVo>>("Tickers", AppConstant.ADDRESS) {
//        @Override
//        public Call<DTResponse> request(HashMap<String, String> params) {
//            return mApi.tickers();
//        }
//    };
//
//    public API SingleTickers = new API<TickerVo>("SingleTickers", AppConstant.ADDRESS) {
//        @Override
//        public Call<DTResponse> request(HashMap<String, String> params) {
//            String market = params.get("market");
//
//            return mApi.singleTickers(market);
//        }
//    };
//
//    public API GetTabs = new API<String[]>("GetTabs", AppConstant.ADDRESS) {
//        @Override
//        public Call<DTResponse> request(HashMap<String, String> params) {
//            return mApi.getTabs();
//        }
//    };
//
//    public API GetHomeMarkets = new API<List<HomeMarketVo>>("GetHomeMarkets", AppConstant.ADDRESS) {
//        @Override
//        public Call<DTResponse> request(HashMap<String, String> params) {
//            return mApi.getHomeMarkets();
//        }
//    };
//
//    public API getInformation = new API<MarketInformationVo>("GetInformation", AppConstant.ADDRESS) {
//        @Override
//        public Call<DTResponse> request(HashMap<String, String> params) {
//            return mApi.getInformation(params);
//        }
//    };

}
