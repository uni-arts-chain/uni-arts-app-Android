package com.yunhualian.net;

import com.google.gson.JsonObject;
import com.yunhualian.entity.AppUpdateVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.UserVo;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RemoteService {
    @POST("njhzf/sendInfo")
    Call<JsonObject> sendInfo(@Body RemoteRequestBody mRemoteRequestBody);

    @POST()
    Call<JsonObject> netLock(@Url String url, @Body RemoteRequestBody mRemoteRequestBody);

    @Streaming //添加这个注解用来下载大文件
    @GET()
    Call<ResponseBody> downloadFile(@Url String url);
/*

    //商城列表（算力列表）
    @GET("/api/v1/powers")
    Call<BaseResponseVo<List<PowersVo>>> powers();

    //算力详情
    @GET("/api/v1/powers/{powersId}")
    Call<BaseResponseVo<PowersVo>> powers(@Path("powersId") String powersId);

    //算力类型
    @GET("/api/v1/powers/outs")
    Call<BaseResponseVo<List<PowersOutsVo>>> powersOuts();

    //已购矿机（订单历史）
    @GET("/api/v1/orders")
    Call<BaseResponseVo<List<OrdersBean>>> orders(@Query("currency") String currency,
                                                  @Query("state") String state,
                                                  @Query("mining_state") String mining_state,
                                                  @Query("page") int page);
*/

    //已购矿机 质押
    @POST("/api/v1/orders/pledge")
    @FormUrlEncoded
    Call<BaseResponseVo> pledge(@Field("order_sn") String order_sn);

    //已购矿机  解押
    @POST("/api/v1/orders/release_pledge")
    @FormUrlEncoded
    Call<BaseResponseVo> releasePledge(@Field("order_sn") String order_sn);

    //订单详情
//    @GET("/api/v1/orders/{sn}")
//    Call<BaseResponseVo<OrdersBean>> ordersDetails(@Path("sn") String sn);

    //续费电费包（补缴电费包）
    @POST("/api/v1/ /orders/add_electricity")
    @FormUrlEncoded
    Call<BaseResponseVo> addElectricity(@Field("currency") String currency,
                                        @Field("mining_sn") int mining_sn,
                                        @Field("electricity_id") int electricity_id,
                                        @Field("otp") String otp);

  /*  //订单创建
    @POST("/api/v1/orders")
    @FormUrlEncoded
    Call<BaseResponseVo<OrderCreateVo>> ordersCreate(@Field("currency") String currency,
                                                     @Field("power_id") int power_id,
                                                     @Field("power_amount") String power_amount,
                                                     @Field("electricity_id") int electricity_id,
                                                     @Field("deduct_flag") int deduct_flag,
                                                     @Field("otp") String otp);


    //订单提交
    @POST("/api/v1/orders/book")
    @FormUrlEncoded
    Call<BaseResponseVo<OrderCreateVo>> ordersBook(@Field("power_id") int power_id,
                                                   @Field("power_amount") String power_amount,
                                                   @Field("electricity_id") int electricity_id,
                                                   @Field("deduct_flag") int deduct_flag,
                                                   @Field("discount_flag") int discount_flag,
                                                   @Field("deduct_amount") String deduct_amount);
*/
    //订单支付
    @POST("/api/v1/orders/pay")
    @FormUrlEncoded
    Call<BaseResponseVo> ordersPay(@Field("currency") String currency,
                                   @Field("order_sn") String order_sn);

    //订单取消
    @POST("/api/v1/orders/canceled")
    @FormUrlEncoded
    Call<BaseResponseVo> ordersCanceled(@Field("order_sn") String order_sn);


    //发送手机短信验证(注册/忘记密码)
    @POST("/api/v1/members/send_sms")
    @FormUrlEncoded
    Call<BaseResponseVo> sendSmsCode(@Field("phone_number") String phone_number,
                                     @Field("send_type") String send_type);

    //发送邮箱验证(注册/忘记密码)
    @POST("/api/v1/members/send_email_code_verify")
    @FormUrlEncoded
    Call<BaseResponseVo> sendEmailCode(@Field("email") String email,
                                       @Field("send_type") String send_type);

    //验证短信码
    @POST("/api/v1/members/validate_sms_token")
    @FormUrlEncoded
    Call<BaseResponseVo> verifySmsCode(@Field("phone_number") String phone_number,
                                       @Field("token") String token,
                                       @Field("send_type") String send_type);

    //验证邮箱码
    @POST("/api/v1/members/validate_email_token")
    @FormUrlEncoded
    Call<BaseResponseVo> verifyEmailCode(@Field("email") String email,
                                         @Field("token") String token,
                                         @Field("send_type") String send_type);

    //手机号注册
    @POST("/api/v1/members/user_create")
    @FormUrlEncoded
    Call<BaseResponseVo<UserVo>> phoneRegister(@Field("phone_number") String phone_number,
                                               @Field("token") String token,
                                               @Field("password") String password,
                                               @Field("password_confirmation") String password_confirmation,
                                               @Field("ref_code") String ref_code,
                                               @Field("referer") String referer);

    //邮箱注册
    @POST("/api/v1/members/email_user_create")
    @FormUrlEncoded
    Call<BaseResponseVo<UserVo>> emailRegister(@Field("email") String email,
                                               @Field("token") String token,
                                               @Field("password") String password,
                                               @Field("password_confirmation") String password_confirmation,
                                               @Field("ref_code") String ref_code,
                                               @Field("referer") String referer);

    //手机号登录
    @POST("/api/v1/members/user_login")
    @FormUrlEncoded
    Call<BaseResponseVo<UserVo>> phoneLogin(@Field("phone_number") String phone_number,
                                            @Field("password") String password);

    //邮箱登录
    @POST("/api/v1/members/user_email_login")
    @FormUrlEncoded
    Call<BaseResponseVo<UserVo>> emailLogin(@Field("email") String email,
                                            @Field("password") String password);

    //用户信息(持久化登录)
    @GET("/api/v1/members/user_info")
    Call<BaseResponseVo<UserVo>> queryUser();

    //账户列表
//    @GET("/api/v1/accounts")
//    Call<BaseResponseVo<List<AccountVo>>> accounts();

    //手机用户忘记密码
    @POST("/api/v1/members/forget_pwd")
    @FormUrlEncoded
    Call<BaseResponseVo<UserVo>> phoneFindBack(@Field("phone_number") String phone_number,
                                               @Field("token") String token,
                                               @Field("password") String password,
                                               @Field("password_confirmation") String password_confirmation);

    //邮箱用户忘记密码
    @POST("/api/v1/members/forget_email_user_pwd")
    @FormUrlEncoded
    Call<BaseResponseVo<UserVo>> emailFindBack(@Field("email") String email,
                                               @Field("token") String token,
                                               @Field("password") String password,
                                               @Field("password_confirmation") String password_confirmation);

    //Banner列表
//    @GET("/api/v1/banners")
//    Call<BaseResponseVo<List<BannersVo>>> queryBanner(@Query("platform") int platform);

    //用户信息持久化
    @GET("/api/v1/members/user_info")
    Call<BaseResponseVo<UserVo>> userInfo();

    //电费包列表
//    @GET("/api/v1/electricities")
//    Call<BaseResponseVo<List<ElectricitiesVo>>> electricities(@Query("power_id") String power_id);

//    // 资产
//    @GET("/api/v1/accounts")
//    Call<BaseResponseVo<List<AssetsVo>>> queryAssets();
//
//    // 资金变动历史
//    @GET("/api/v1/funds")
//    Call<BaseResponseVo<List<CurrencyTradeHistoryVo>>> queryCurrencyTradeHistory(@Query("page") int page, @Query("currency") String currency, @Query("reason") String reason);

    //修改密码
    @POST("/api/v1/members/change_pwd")
    @FormUrlEncoded
    Call<BaseResponseVo<UserVo>> changePwd(@Field("orgin_password") String orgin_password,
                                           @Field("password") String password,
                                           @Field("password_confirmation") String password_confirmation
    );

    // 新闻列表
//    @GET("/api/v1/news")
//    Call<BaseResponseVo<List<AnnouncementVo>>> queryNews(@Query("page") int page, @Query("type") String type);
//
//    // 获取充值地址
//    @GET("/api/v1/payment_addresses/address")
//    Call<BaseResponseVo<DepositVo>> queryDepositAddress(@Query("account_id") String account_id);
//
//    // 获取币种的法币价格
//    @GET("/api/v1/prices/exchange")
//    Call<BaseResponseVo<CurrencyPriceVo>> queryFiatCurrencyPrice(@Query("codes") String codes, @Query("symbol") String symbol);
//
//    // otps
//    @GET("/api/v1/members/otps")
//    Call<BaseResponseVo<OtpsVo>> queryOtps();
//
//    // 我的算力
//    @GET("/api/v1/minings")
//    Call<BaseResponseVo<List<NoticeVo>>> minings(@Query("currency") String currency,
//                                                 @Query("state") String state,
//                                                 @Query("page") String page);

    // 绑定邮箱
    @POST("/api/v1/members/bind_email")
    @FormUrlEncoded
    Call<BaseResponseVo> bindEmail(@Field("email") String email,
                                   @Field("phone_token") String phone_token,
                                   @Field("email_token") String email_token);

    // 绑定邮箱
    @POST("/api/v1/members/bind_phone")
    @FormUrlEncoded
    Call<BaseResponseVo> bindPhone(@Field("phone_number") String phone_number,
                                   @Field("phone_token") String phone_token,
                                   @Field("email_token") String email_token);

    // 绑定2步验证码
//    @POST("/api/v1/members/binding_otp")
//    @FormUrlEncoded
//    Call<BaseResponseVo<OtpsVo>> bindOtp(@Field("otp") String otp, @Field("token_type") String token_type, @Field("token") String token);

    // 验证otp码
    @POST("/api/v1/members/validate_otp")
    @FormUrlEncoded
    Call<BaseResponseVo> verifyOtp(@Field("otp") String otp, @Header("Authorization") String mToken);

    // 通知
//    @GET("/api/v1/notices")
//    Call<BaseResponseVo<List<NoticeVo>>> queryNotices(@Query("page") int page);
//
//    // 提现地址列表
//    @GET("/api/v1/fund_sources")
//    Call<BaseResponseVo<List<CurrencyAddressVo>>> queryFundSources(@Query("currency") String currency);

    //  提现地址创建
//    @POST("/api/v1/fund_sources")
//    @FormUrlEncoded
//    Call<BaseResponseVo<CurrencyAddressVo>> createFundSources(@Field("currency") String currency, @Field("uid") String uid, @Field("extra") String extra, @Field("chain_name") String chain_name);

    // 解绑2步验证码
    @POST("/api/v1/members/unbinding_otp")
    @FormUrlEncoded
    Call<BaseResponseVo> unbindOtp(@Field("otp") String otp);

    //提现地址删除
    @DELETE("/api/v1/fund_sources/{id}")
    Call<BaseResponseVo> deleteWithdrawAddress(@Path("id") int id);

    //显示合同
//    @GET("/api/v1/members/user_contract")
//    Call<BaseResponseVo<UserContractVo>> useContract(@Query("power_id") Integer power_id);
//
//    //历史收益
//    @GET("/api/v1/incomes/mining_incomes")
//    Call<BaseResponseVo<List<IncomesVo>>> incomes(@Query("mining_sn") String mining_sn, @Query("page") int page);
//
//    // 电费包提交
//    @POST("/api/v1/sub_orders/book")
//    @FormUrlEncoded
//    Call<BaseResponseVo<SubOrderBookVo>> subOrdersBook(@Field("mining_sn") String mining_sn,
//                                                       @Field("electricity_id") int electricity_id);

    // 身份核验:身份证姓名和号码一致性核验
    @POST("/api/v1/members/validity_idnumber_verification")
    @FormUrlEncoded
    Call<BaseResponseVo> idCardsCheck(@Field("name") String name, @Field("id_number") String id_number);

    // 电费包支付
//    @POST("/api/v1/sub_orders/pay")
//    @FormUrlEncoded
//    Call<BaseResponseVo<SubOrderBookVo>> subOrdersPay(@Field("currency") String currency,
//                                                      @Field("sub_order_sn") String sub_order_sn);

    // 身份核验:活体检测与姓名、身份证号核验
//    @POST("/api/v1/members/validity_liveness_stateless")
//    @FormUrlEncoded
//    Call<BaseResponseVo<LivenessVerifyVo>> livenessCheck(@Body RequestBody mRequestBody);

    // 上传身份图片
//    @POST("/api/v1/members/upload_image")
////    @FormUrlEncoded
//    Call<BaseResponseVo<LivenessVerifyVo>> uploadIdImages(@Body RequestBody mRequestBody);

    //矿池列表
//    @GET("/api/v1/mining_pools")
//    Call<BaseResponseVo<MiningPoolsVo>> miningPools();

    // 提现
//    @POST("/api/v1/withdraws")
//    @FormUrlEncoded
//    Call<BaseResponseVo<WithDrawVo>> withDraw(@Field("currency") String currency,
//                                              @Field("amount") String amount,
//                                              @Field("uid") String uid,
//                                              @Field("otp") String otp);
//
//    // 充币历史
//    @GET("/api/v1/deposits")
//    Call<BaseResponseVo<List<DepositHistoryVo>>> depositHistory(@Query("currency") String currency,
//                                                                @Query("state") String state,
//                                                                @Query("page") int page);
//
//    // 提现历史
//    @GET("/api/v1/withdraws")
//    Call<BaseResponseVo<List<WithDrawHistoryVo>>> withDrawHistory(@Query("currency") String currency,
//                                                                  @Query("state") String state);
//
//    // 用户划转历史
//    @GET("/api/v1/withdraws/transfer_history")
//    Call<BaseResponseVo<List<TransferHistoryVo>>> transferHistory(@Query("currency") String currency,
//                                                                  @Query("state") String state);
//
//    // 用户划转
//    @POST("/api/v1/withdraws/transfer")
//    @FormUrlEncoded
//    Call<BaseResponseVo<TransferVo>> transfer(@Field("currency") String currency,
//                                              @Field("to_member_key") String to_member_key,
//                                              @Field("amount") String amount,
//                                              @Field("otp") String otp);
//
//    //我的邀请奖励记录
//    @GET("/api/v1/awards/award_orders")
//    Call<BaseResponseVo<List<AawardOrdersVo>>> awardOrders(@Query("page") int page,
//                                                           @Query("award_id") Integer award_id);
//
//    //我的邀请奖励记录
//    @GET("/api/v1/mining_pools/system_info")
//    Call<BaseResponseVo<SystemInfoVo>> systemInfo();

    //检测更新
    @GET("/api/v1/mobile_versions/info")
    Call<BaseResponseVo<AppUpdateVo>> checkUpdate(@Query("phone_type") String phone_type, @Query("version_code") int version_code);

    //我的邀请奖励记录
//    @GET("/api/v1/incomes/mining_total_incomes")
//    Call<BaseResponseVo<MiningTotalIncomesVo>> miningTotalIncomes(@Query("mining_sn") String mining_sn);
//
//    //我的邀请等级完成情况
//    @GET("/api/v1/awards/member_level")
//    Call<BaseResponseVo<MimberLevelVo>> memberLevel(@Query("week_count") String week_count);
//
//
//    //我的邀请等级完成情况
//    @GET("/api/v1/awards")
//    Call<BaseResponseVo<List<Awards>>> awards();

    // 奖金划转
    @POST("/api/v1/withdraws/awards_transfer")
    @FormUrlEncoded
    Call<BaseResponseVo> awardsTransfer(@Field("currency") String currency,
                                        @Field("amount") String amount);

    // 奖金赠送
    @POST("/api/v1/withdraws/awards_give")
    @FormUrlEncoded
    Call<BaseResponseVo> bonusTransfer(@Field("currency") String currency,
                                       @Field("to_member_key") String to_member_key,
                                       @Field("amount") String amount,
                                       @Field("otp") String otp);


    //奖金划转历史
//    @GET("/api/v1/funds/awards")
//    Call<BaseResponseVo<List<AwardsTransferHistoryVo>>> awardsTransferHistory(@Query("page") int page, @Query("currency") String currency, @Query("reason") String reason);
//
//
//    //分享下载地址
//    @GET("/api/v1/mobile_versions/download")
//    Call<BaseResponseVo<ShareDownloadInfoVo>> shareDownloadInfo();


    //绑定邀请关系
    @POST("/api/v1/members/binding_invitation")
    @FormUrlEncoded
    Call<BaseResponseVo> bindingInvitation(@Field("ref_code") String ref_code);


}

