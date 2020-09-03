package com.yunhualian.net;

import com.google.gson.JsonObject;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.AppUpdateVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.UserVo;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class RequestManager {
    private static volatile RequestManager mRequestManager;

    public static RequestManager instance() {
        if (null == mRequestManager) {
            synchronized (RequestManager.class) {
                if (null == mRequestManager) {
                    mRequestManager = new RequestManager();
                }
            }
        }
        return mRequestManager;
    }

    private <T> boolean checkNull(JsonObject mRequestJson, MinerCallback<T> mCallBack) {
        if (null == mRequestJson) {
            if (null != mCallBack) {
                mCallBack.onFailure(null, new Throwable(YunApplication.getInstance().getResources().getString(R.string.parameter_error)));
            }
            return true;
        }
        return false;
    }

    public void sendInfo(JsonObject mRequestJson, MinerCallback<JsonObject> mCallBack) {
        if (checkNull(mRequestJson, mCallBack)) {
            return;
        }
        Call<JsonObject> mCall = NetworkManager.instance().getmRemoteService().sendInfo(new RemoteRequestBody(mRequestJson.toString()));
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void downloadFile(String mUrl, MinerCallback<ResponseBody> mCallBack) {
        Call<ResponseBody> mCall = NetworkManager.instance().getmRemoteService().downloadFile(mUrl);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 商城列表（算力列表）
     *
     * @param mCallBack mCallBack
     */
//    public void powers(MinerCallback<BaseResponseVo<List<PowersVo>>> mCallBack) {
//        Call<BaseResponseVo<List<PowersVo>>> mCall = NetworkManager.instance().getmRemoteService().powers();
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 算力详情
     *
     * @param powersId  算力id
     * @param mCallBack mCallBack
     */
//    public void powersDetails(String powersId, MinerCallback<BaseResponseVo<PowersVo>> mCallBack) {
//        Call<BaseResponseVo<PowersVo>> mCall = NetworkManager.instance().getmRemoteService().powers(powersId);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 算力类型
     *
     * @param mCallBack mCallBack
     */
//    public void powersOuts(MinerCallback<BaseResponseVo<List<PowersOutsVo>>> mCallBack) {
//        Call<BaseResponseVo<List<PowersOutsVo>>> mCall = NetworkManager.instance().getmRemoteService().powersOuts();
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 已购矿机（订单历史）
     *
     * @param currency     币种
     * @param state        状态
     * @param mining_state waitting, running, cancelled, suspect, suspend, ended, admin_suspend
     *                     待付款： state = paying
     *                     准备中: mining_state = waitting
     *                     待运行: mining_state = suspect, suspend
     *                     运行中: mining_state =  running
     * @param page         页数
     * @param mCallBack    mCallBack
     */
//    public void orders(String currency, String state, String mining_state, int page, MinerCallback<BaseResponseVo<List<OrdersBean>>> mCallBack) {
//        Call<BaseResponseVo<List<OrdersBean>>> mCall = NetworkManager.instance().getmRemoteService().orders(currency, state, mining_state, page);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 订单详情
     *
     * @param sn        后缀url
     * @param mCallBack mCallBack
     */
//    public void ordersDetails(String sn, MinerCallback<BaseResponseVo<OrdersBean>> mCallBack) {
//        Call<BaseResponseVo<OrdersBean>> mCall = NetworkManager.instance().getmRemoteService().ordersDetails(sn);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 续费电费包
     *
     * @param currency       币种eth、btc
     * @param mining_sn      mining_sn
     * @param electricity_id 电费包ID
     * @param otp            otp码
     * @param mCallBack      mCallBack
     */
    public void addElectricity(String currency, int mining_sn, int electricity_id, String otp, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().addElectricity(currency, mining_sn, electricity_id, otp);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 质押
     *
     * @param sn        订单sn
     * @param mCallBack mCallBack
     */
    public void pledge(String sn, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().pledge(sn);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 解押
     *
     * @param sn        订单sn
     * @param mCallBack mCallBack
     */
    public void releasePledge(String sn, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().releasePledge(sn);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }


    /**
     * 订单创建
     *
     * @param currency       币种eth、btc
     * @param power_id
     * @param power_amount   数量
     * @param electricity_id 电费包ID
     * @param otp            otp码
     * @param mCallBack      mCallBack
     */
//    public void ordersCreate(String currency, int power_id, String power_amount, int electricity_id, int deduct_flag, String otp, MinerCallback<BaseResponseVo<OrderCreateVo>> mCallBack) {
//        Call<BaseResponseVo<OrderCreateVo>> mCall = NetworkManager.instance().getmRemoteService().ordersCreate(currency, power_id, power_amount, electricity_id, deduct_flag, otp);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 订单提交
     *
     * @param power_id
     * @param power_amount   数量
     * @param electricity_id 电费包ID
     * @param mCallBack      mCallBack
     */
//    public void ordersBook(int power_id, String power_amount, int electricity_id, int deduct_flag, int discount_flag, String deduct_amount, MinerCallback<BaseResponseVo<OrderCreateVo>> mCallBack) {
//        Call<BaseResponseVo<OrderCreateVo>> mCall = NetworkManager.instance().getmRemoteService().ordersBook(power_id, power_amount, electricity_id, deduct_flag,discount_flag,deduct_amount);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * @param currency  币种
     * @param order_sn  订单sn
     * @param mCallBack mCallBack
     */
    public void ordersPay(String currency, String order_sn, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().ordersPay(currency, order_sn);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 订单取消
     *
     * @param order_sn  订单号
     * @param mCallBack mCallBack
     */
    public void ordersCanceled(String order_sn, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().ordersCanceled(order_sn);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 发送手机短信验证(注册/忘记密码)
     *
     * @param phone_number 手机号：国家码+手机号
     * @param send_type    短信类型   signup, sigin, change_pwd, forget_pwd, bind_email, change_phone, set_pay_pwd
     * @param mCallBack    mCallBack
     */
    public void sendSmsCode(String phone_number, String send_type, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().sendSmsCode(phone_number, send_type);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 发送邮箱验证(注册/忘记密码)
     *
     * @param email     邮箱
     * @param send_type 邮箱类型    signup, sigin, change_pwd, forget_pwd, bind_phone, change_email, set_pay_pwd, bind_otp, forget_pay_pwd
     * @param mCallBack mCallBack
     */
    public void sendEmailCode(String email, String send_type, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().sendEmailCode(email, send_type);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 发送手机短信验证(注册/忘记密码)
     *
     * @param phone_number 手机号：国家码+手机号
     * @param token        验证码
     * @param send_type    短信类型   signup, sigin, change_pwd, forget_pwd, bind_email, change_phone, set_pay_pwd
     * @param mCallBack    mCallBack
     */
    public void verifySmsCode(String phone_number, String token, String send_type, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().verifySmsCode(phone_number, token, send_type);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 发送邮箱验证(注册/忘记密码)
     *
     * @param email     邮箱
     * @param token     验证码
     * @param send_type 邮箱类型    signup, sigin, change_pwd, forget_pwd, bind_phone, change_email, set_pay_pwd, bind_otp, forget_pay_pwd
     * @param mCallBack mCallBack
     */
    public void verifyEmailCode(String email, String token, String send_type, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().verifyEmailCode(email, token, send_type);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 手机号注册
     *
     * @param phone_number          手机号：国家码+手机号
     * @param token                 验证码
     * @param password              密码
     * @param password_confirmation 确认密码
     * @param ref_code              邀请码
     * @param referer               注册来源
     * @param mCallBack
     */
    public void phoneRegister(String phone_number,
                              String token,
                              String password,
                              String password_confirmation,
                              String ref_code,
                              String referer, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().phoneRegister(phone_number, token, password, password_confirmation, ref_code, referer);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 邮箱注册
     *
     * @param email                 邮箱
     * @param token                 验证码
     * @param password              密码
     * @param password_confirmation 确认密码
     * @param ref_code              邀请码
     * @param referer               注册来源
     * @param mCallBack
     */
    public void emailRegister(String email,
                              String token,
                              String password,
                              String password_confirmation,
                              String ref_code,
                              String referer, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().emailRegister(email, token, password, password_confirmation, ref_code, referer);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 手机号登录
     *
     * @param phone_number 手机号：国家码+手机号
     * @param password     密码
     * @param mCallBack
     */
    public void phoneLogin(String phone_number,
                           String password, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().phoneLogin(phone_number, password);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 邮箱登录
     *
     * @param email     邮箱
     * @param password  密码
     * @param mCallBack
     */
    public void emailLogin(String email,
                           String password, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().emailLogin(email, password);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 邮箱登录
     */
    public void queryUser(MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().queryUser();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 手机号找回
     *
     * @param phone_number          手机号：国家码+手机号
     * @param token                 验证码
     * @param password              密码
     * @param password_confirmation 确认密码
     * @param mCallBack
     */
    public void phoneFindBack(String phone_number,
                              String token,
                              String password,
                              String password_confirmation, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().phoneFindBack(phone_number, token, password, password_confirmation);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 邮箱找回
     *
     * @param email                 邮箱
     * @param token                 验证码
     * @param password              密码
     * @param password_confirmation 确认密码
     * @param mCallBack
     */
    public void emailFindBack(String email,
                              String token,
                              String password,
                              String password_confirmation, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().emailFindBack(email, token, password, password_confirmation);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * Banners
     *
     * @param mCallBack
     */
//    public void queryBanner(MinerCallback<BaseResponseVo<List<BannersVo>>> mCallBack) {
//        Call<BaseResponseVo<List<BannersVo>>> mCall = NetworkManager.instance().getmRemoteService().queryBanner(1);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 通知
//     *
//     * @param mCallBack
//     */
//    public void queryNotices(int page, MinerCallback<BaseResponseVo<List<NoticeVo>>> mCallBack) {
//        Call<BaseResponseVo<List<NoticeVo>>> mCall = NetworkManager.instance().getmRemoteService().queryNotices(page);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 资产列表
//     *
//     * @param mCallBack
//     */
//    public void queryAssets(MinerCallback<BaseResponseVo<List<AssetsVo>>> mCallBack) {
//        Call<BaseResponseVo<List<AssetsVo>>> mCall = NetworkManager.instance().getmRemoteService().queryAssets();
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 资产变动历史
//     *
//     * @param page      页码
//     * @param currency  币种code
//     * @param reason    类型: {:deposit=>1000, :withdraw=>2000, :award=>3000, :order_pay=>4000, :sub_order_pay=>4010, :power_daily_income=>5000}
//     *                  <p>
//     *                  <p>
//     *                  Available values : 1000, 2000, 3000, 4000, 4010, 5000
//     * @param mCallBack
//     */
//    public void queryCurrencyTradeHistory(int page, String currency, String reason, MinerCallback<BaseResponseVo<List<CurrencyTradeHistoryVo>>> mCallBack) {
//        Call<BaseResponseVo<List<CurrencyTradeHistoryVo>>> mCall = NetworkManager.instance().getmRemoteService().queryCurrencyTradeHistory(page, currency, reason);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 用户信息持久化
     */
    public void userInfo(MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().userInfo();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 用户信息持久化
     */
//    public void accounts(MinerCallback<BaseResponseVo<List<AccountVo>>> mCallBack) {
//        Call<BaseResponseVo<List<AccountVo>>> mCall = NetworkManager.instance().getmRemoteService().accounts();
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 电费包列表
//     *
//     * @param powerId   算力id
//     * @param mCallBack mCallBack
//     */
//    public void electricities(String powerId, MinerCallback<BaseResponseVo<List<ElectricitiesVo>>> mCallBack) {
//        Call<BaseResponseVo<List<ElectricitiesVo>>> mCall = NetworkManager.instance().getmRemoteService().electricities(powerId);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }


    public void changePwd(String orgin_password,
                          String password,
                          String password_confirmation,
                          MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().changePwd(orgin_password, password, password_confirmation);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }


    /**
     * 通知列表
     *
     * @param mCallBack mCallBack
     */
//    public void queryNews(int page, String type, MinerCallback<BaseResponseVo<List<AnnouncementVo>>> mCallBack) {
//        Call<BaseResponseVo<List<AnnouncementVo>>> mCall = NetworkManager.instance().getmRemoteService().queryNews(page, type);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 获取充值地址
//     *
//     * @param account_id 账户ID
//     * @param mCallBack
//     */
//    public void queryDepositAddress(String account_id, MinerCallback<BaseResponseVo<DepositVo>> mCallBack) {
//        Call<BaseResponseVo<DepositVo>> mCall = NetworkManager.instance().getmRemoteService().queryDepositAddress(account_id);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 获取币种的法币价格
     *
     * @param codes     逗号隔开， eth,btc,ltc,eos
     * @param symbol    法币 {@link com.miner.client.constant.AppConstant.FiatCurrencyType}
     *                  <p>
     *                  <p>
     *                  Available values : cny, usd
     *                  <p>
     *                  <p>
     *                  Default value : cny
     * @param mCallBack
     */
//    public void queryFiatCurrencyPrice(String codes, String symbol, MinerCallback<BaseResponseVo<CurrencyPriceVo>> mCallBack) {
//        Call<BaseResponseVo<CurrencyPriceVo>> mCall = NetworkManager.instance().getmRemoteService().queryFiatCurrencyPrice(codes, symbol);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * otps
//     *
//     * @param mCallBack
//     */
//    public void queryOtps(MinerCallback<BaseResponseVo<OtpsVo>> mCallBack) {
//        Call<BaseResponseVo<OtpsVo>> mCall = NetworkManager.instance().getmRemoteService().queryOtps();
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 绑定2步验证码
//     *
//     * @param otp        两步验证码
//     * @param token_type 验证码类型
//     * @param token      验证码,type: "bind_otp"
//     * @param mCallBack
//     */
//    public void bindOtp(String otp, String token_type, String token, MinerCallback<BaseResponseVo<OtpsVo>> mCallBack) {
//        Call<BaseResponseVo<OtpsVo>> mCall = NetworkManager.instance().getmRemoteService().bindOtp(otp, token_type, token);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 验证otp码
     *
     * @param otp       两步验证码
     * @param mCallBack
     */
    public void verifyOtp(String otp, String mToken, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().verifyOtp(otp, mToken);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 绑定邮箱
     *
     * @param email       邮箱
     * @param phone_token 用户输入手机号
     * @param email_token 用户输入邮箱
     * @param mCallBack   mCallBack
     */
    public void bindEmail(String email,
                          String phone_token,
                          String email_token, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().bindEmail(email, phone_token, email_token);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 绑定手机号
     *
     * @param email       邮箱
     * @param phone_token 用户输入手机号
     * @param email_token 用户输入邮箱
     * @param mCallBack   mCallBack
     */
    public void bindPhone(String email,
                          String phone_token,
                          String email_token, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().bindPhone(email, phone_token, email_token);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 提现地址列表
     *
     * @param currency  币种 btc、eth
     * @param mCallBack mCallBack
     */
//    public void queryFundSources(String currency, MinerCallback<BaseResponseVo<List<CurrencyAddressVo>>> mCallBack) {
//        Call<BaseResponseVo<List<CurrencyAddressVo>>> mCall = NetworkManager.instance().getmRemoteService().queryFundSources(currency);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 提现地址创建
//     *
//     * @param currency  币种 btc、eth
//     * @param mCallBack mCallBack
//     */
//    public void createFundSources(String currency, String uid, String extra, String chain_name, MinerCallback<BaseResponseVo<CurrencyAddressVo>> mCallBack) {
//        Call<BaseResponseVo<CurrencyAddressVo>> mCall = NetworkManager.instance().getmRemoteService().createFundSources(currency, uid, extra, chain_name);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 解绑2步验证码
     *
     * @param otp       两步验证码
     * @param mCallBack
     */
    public void unbindOtp(String otp, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().unbindOtp(otp);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 提现地址删除
     *
     * @param id        两步验证码
     * @param mCallBack
     */
    public void deleteWithdrawAddress(int id, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().deleteWithdrawAddress(id);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 显示合同
     *
     * @param power_id  算力id
     * @param mCallBack mCallBack
     */
//    public void useContract(Integer power_id, MinerCallback<BaseResponseVo<UserContractVo>> mCallBack) {
//        Call<BaseResponseVo<UserContractVo>> mCall = NetworkManager.instance().getmRemoteService().useContract(power_id);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 收益历史
//     *
//     * @param mining_sn 算力订单
//     * @param page      page
//     * @param mCallBack mCallBack
//     */
//    public void incomes(String mining_sn, int page, MinerCallback<BaseResponseVo<List<IncomesVo>>> mCallBack) {
//        Call<BaseResponseVo<List<IncomesVo>>> mCall = NetworkManager.instance().getmRemoteService().incomes(mining_sn, page);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 电费包提交
//     *
//     * @param mining_sn      算力sn
//     * @param electricity_id 电费包id
//     * @param mCallBack      mCallBack
//     */
//    public void subOrdersBook(String mining_sn,
//                              int electricity_id, MinerCallback<BaseResponseVo<SubOrderBookVo>> mCallBack) {
//        Call<BaseResponseVo<SubOrderBookVo>> mCall = NetworkManager.instance().getmRemoteService().subOrdersBook(mining_sn, electricity_id);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 电费包支付
//     *
//     * @param currency     支付币种
//     * @param sub_order_sn 电费包sn
//     * @param mCallBack    mCallBack
//     */
//    public void subOrdersPay(String currency,
//                             String sub_order_sn, MinerCallback<BaseResponseVo<SubOrderBookVo>> mCallBack) {
//        Call<BaseResponseVo<SubOrderBookVo>> mCall = NetworkManager.instance().getmRemoteService().subOrdersPay(currency, sub_order_sn);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 身份核验:活体检测与姓名、身份证号核验
//     *
//     * @param mCallBack
//     */
//    public void livenessCheck(RequestBody mRequestBody, MinerCallback<BaseResponseVo<LivenessVerifyVo>> mCallBack) {
//        Call<BaseResponseVo<LivenessVerifyVo>> mCall = NetworkManager.instance().getmRemoteService().livenessCheck(mRequestBody);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 身份证姓名和号码一致性核验
     *
     * @param name      姓名
     * @param id_number 身份证号
     * @param mCallBack mCallBack
     */
    public void idCardsCheck(String name, String id_number, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().idCardsCheck(name, id_number);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 上传身份图片
     *
     * @param mCallBack
     */
//    public void uploadIdImages(RequestBody mRequestBody, MinerCallback<BaseResponseVo<LivenessVerifyVo>> mCallBack) {
//        Call<BaseResponseVo<LivenessVerifyVo>> mCall = NetworkManager.instance().getmRemoteService().uploadIdImages(mRequestBody);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 上传身份图片
//     *
//     * @param mCallBack
//     */
//    public void miningPools(MinerCallback<BaseResponseVo<MiningPoolsVo>> mCallBack) {
//        Call<BaseResponseVo<MiningPoolsVo>> mCall = NetworkManager.instance().getmRemoteService().miningPools();
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//
//    /**
//     * 提现
//     *
//     * @param currency  币种
//     * @param amount    提现数量
//     * @param uid       提现地址
//     * @param otp       两步验证码
//     * @param mCallBack
//     */
//    public void withDraw(String currency, String amount, String uid, String otp, MinerCallback<BaseResponseVo<WithDrawVo>> mCallBack) {
//        Call<BaseResponseVo<WithDrawVo>> mCall = NetworkManager.instance().getmRemoteService().withDraw(currency, amount, uid, otp);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 充币历史
//     *
//     * @param currency  币种
//     * @param state     状态 Available values : submitting, cancelled, submitted, rejected, accepted, checked, warning
//     * @param mCallBack
//     */
//    public void depositHistory(String currency, String state, int page, MinerCallback<BaseResponseVo<List<DepositHistoryVo>>> mCallBack) {
//        Call<BaseResponseVo<List<DepositHistoryVo>>> mCall = NetworkManager.instance().getmRemoteService().depositHistory(currency, state, page);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 提现历史
//     *
//     * @param currency  币种
//     * @param state     状态 Available values : submitting, submitted, rejected, accepted, suspect, processing, done, canceled, failed
//     * @param mCallBack
//     */
//    public void withDrawHistory(String currency, String state, MinerCallback<BaseResponseVo<List<WithDrawHistoryVo>>> mCallBack) {
//        Call<BaseResponseVo<List<WithDrawHistoryVo>>> mCall = NetworkManager.instance().getmRemoteService().withDrawHistory(currency, state);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//
//    /**
//     * 用户划转历史
//     *
//     * @param currency  币种
//     * @param state     状态 Available values : submitting, submitted, done, canceled, failed
//     * @param mCallBack
//     */
//    public void transferHistory(String currency, String state, MinerCallback<BaseResponseVo<List<TransferHistoryVo>>> mCallBack) {
//        Call<BaseResponseVo<List<TransferHistoryVo>>> mCall = NetworkManager.instance().getmRemoteService().transferHistory(currency, state);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 提现
//     *
//     * @param currency      币种
//     * @param to_member_key 对方账号(email/手机号)
//     * @param amount        提现数量
//     * @param otp           两步验证码
//     * @param mCallBack
//     */
//    public void transfer(String currency, String to_member_key, String amount, String otp, MinerCallback<BaseResponseVo<TransferVo>> mCallBack) {
//        Call<BaseResponseVo<TransferVo>> mCall = NetworkManager.instance().getmRemoteService().transfer(currency, to_member_key, amount, otp);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 我的邀请奖励
//     *
//     * @param page      page
//     * @param page      award_id
//     * @param mCallBack mCallBack
//     */
//    public void awardOrders(int page, Integer award_id, MinerCallback<BaseResponseVo<List<AawardOrdersVo>>> mCallBack) {
//        Call<BaseResponseVo<List<AawardOrdersVo>>> mCall = NetworkManager.instance().getmRemoteService().awardOrders(page, award_id);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 系统信息
//     *
//     * @param mCallBack
//     */
//    public void systemInfo(MinerCallback<BaseResponseVo<SystemInfoVo>> mCallBack) {
//        Call<BaseResponseVo<SystemInfoVo>> mCall = NetworkManager.instance().getmRemoteService().systemInfo();
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//

    /**
     * 检测更新
     *
     * @param phone_type   客户端类型
     * @param version_code 版本号
     * @param mCallBack
     */
    public void checkUpdate(String phone_type, int version_code, MinerCallback<BaseResponseVo<AppUpdateVo>> mCallBack) {
        Call<BaseResponseVo<AppUpdateVo>> mCall = NetworkManager.instance().getmRemoteService().checkUpdate(phone_type, version_code);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 单个矿机历史总收益
     *
     * @param mining_sn 订单sn
     * @param mCallBack mCallBack
     */
//    public void miningTotalIncomes(String mining_sn, MinerCallback<BaseResponseVo<MiningTotalIncomesVo>> mCallBack) {
//        Call<BaseResponseVo<MiningTotalIncomesVo>> mCall = NetworkManager.instance().getmRemoteService().miningTotalIncomes(mining_sn);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 我的邀请等级完成情况
//     *
//     * @param week_count 月份号(4位年份+2位本年周数,202004)
//     * @param mCallBack  mCallBack
//     */
//    public void memberLevel(String week_count, MinerCallback<BaseResponseVo<MimberLevelVo>> mCallBack) {
//        Call<BaseResponseVo<MimberLevelVo>> mCall = NetworkManager.instance().getmRemoteService().memberLevel(week_count);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 邀请等级
//     *
//     * @param mCallBack mCallBack
//     */
//    public void awards(MinerCallback<BaseResponseVo<List<Awards>>> mCallBack) {
//        Call<BaseResponseVo<List<Awards>>> mCall = NetworkManager.instance().getmRemoteService().awards();
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 奖金划转
     *
     * @param mCallBack mCallBack
     */
    public void awardsTransfer(String currency,
                               String amount, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().awardsTransfer(currency, amount);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }


    /**
     * 奖金赠送
     *
     * @param currency      币种
     * @param to_member_key 对方账号(email/手机号)
     * @param amount        数量
     * @param otp           otp码
     * @param mCallBack
     */
    public void bonusTransfer(String currency, String to_member_key, String amount, String otp, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().bonusTransfer(currency, to_member_key, amount, otp);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 奖金赠送历史
     *
     * @param page      页码
     * @param currency  币种
     * @param reason    类型: {:award_transfer=>6000, :award_give=>6100, :award=>1000, :order_pay=>2000}
     *                  <p>
     *                  <p>
     *                  Available values : 6000, 6100, 1000, 2000
     * @param mCallBack
     */
//    public void awardsTransferHistory(int page, String currency, String reason, MinerCallback<BaseResponseVo<List<AwardsTransferHistoryVo>>> mCallBack) {
//        Call<BaseResponseVo<List<AwardsTransferHistoryVo>>> mCall = NetworkManager.instance().getmRemoteService().awardsTransferHistory(page, currency, reason);
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }
//
//    /**
//     * 分享下载地址
//     *
//     * @param mCallBack
//     */
//    public void awardsTransferHistory(MinerCallback<BaseResponseVo<ShareDownloadInfoVo>> mCallBack) {
//        Call<BaseResponseVo<ShareDownloadInfoVo>> mCall = NetworkManager.instance().getmRemoteService().shareDownloadInfo();
//        NetworkManager.instance().postReq(mCallBack, mCall);
//    }

    /**
     * 绑定邀请关系
     *
     * @param ref_code  推荐码
     * @param mCallBack
     */
    public void bindingInvitation(String ref_code, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().bindingInvitation(ref_code);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

}
