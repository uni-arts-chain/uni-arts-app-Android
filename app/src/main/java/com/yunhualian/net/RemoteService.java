package com.yunhualian.net;

import com.yunhualian.entity.AccountIdVo;
import com.yunhualian.entity.AccountVo;
import com.yunhualian.entity.AnnouncementVo;
import com.yunhualian.entity.AppUpdateVo;
import com.yunhualian.entity.ArtAuctionVo;
import com.yunhualian.entity.ArtPriceVo;
import com.yunhualian.entity.ArtTopicVo;
import com.yunhualian.entity.ArtTypeVo;
import com.yunhualian.entity.ArtistListVo;
import com.yunhualian.entity.ArtistVo;
import com.yunhualian.entity.AuctionVo;
import com.yunhualian.entity.BannersVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.BlindBoxCheckVO;
import com.yunhualian.entity.BlindBoxOpenVo;
import com.yunhualian.entity.BlindBoxOrderCheck;
import com.yunhualian.entity.BlindBoxRecordVo;
import com.yunhualian.entity.BlindBoxVo;
import com.yunhualian.entity.BoughtArtVo;
import com.yunhualian.entity.CollectArtVo;
import com.yunhualian.entity.FollowerVO;
import com.yunhualian.entity.HistoriesBean;
import com.yunhualian.entity.MemberInfo;
import com.yunhualian.entity.MessagesVo;
import com.yunhualian.entity.NoRead;
import com.yunhualian.entity.OrderAmountVo;
import com.yunhualian.entity.PayResult;
import com.yunhualian.entity.PayResyltVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.UploadLive2dVo;
import com.yunhualian.entity.UserAggrementVo;
import com.yunhualian.entity.UserVo;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RemoteService {


    //发送手机短信验证(注册/忘记密码)
    @POST("/api/v2/members/send_sms")
    @FormUrlEncoded
    Call<BaseResponseVo> sendSmsCode(@Field("phone_number") String phone_number,
                                     @Field("send_type") String send_type);


    //用户信息(持久化登录)
    @GET("/api/v2/members/user_info")
    Call<BaseResponseVo<UserVo>> queryUser();

    //用户信息(持久化登录)
    @GET("/api/v2/messages/has_unread")
    Call<BaseResponseVo<NoRead>> queryHasUnReadMessage();

    /*兑换*/
    @POST("/api/v2/lotteries/get_reward")
    @FormUrlEncoded
    Call<BaseResponseVo<UserVo>> exchange(@FieldMap HashMap<String, String> map);

    @POST("/api/v1/feedbacks")
    @FormUrlEncoded
    Call<BaseResponseVo<UserVo>> feedBack(@FieldMap HashMap<String, String> map);

    //账户列表
    @GET("/api/v1/accounts")
    Call<BaseResponseVo<List<AccountVo>>> queryAccountInfo();

    /*出售*/
    @POST("/api/v2/art_orders")
    @FormUrlEncoded
    Call<BaseResponseVo<SellingArtVo>> sellArt(@FieldMap HashMap<String, String> map);


    //用户信息(持久化登录)
    @GET("/api/v2/art_orders/lock_account_id")
    Call<BaseResponseVo<AccountIdVo>> queryAccountId();


    @GET("/api/v2/auctions/lock_account_id")
    Call<BaseResponseVo<AccountIdVo>> queryAuctionAccountId();

    //手机用户忘记密码

    //Banner列表
    @GET("/api/v2/banners")
    Call<BaseResponseVo<List<BannersVo>>> queryBanner(@Query("platform") int platform);

    //拍卖列表
    @GET("/api/v2/auction_meetings")
    Call<BaseResponseVo<List<ArtAuctionVo>>> queryAuction(@Query("page") int platform);


    //购买盲盒
    @POST("/api/v2/blind_box_orders")
    @FormUrlEncoded
    Call<BaseResponseVo<PayResult>> blindBoxOrders(@FieldMap HashMap<String, String> map);

    //购买艺术品
    @POST("/api/v2/art_trades")
    @FormUrlEncoded
    Call<BaseResponseVo<PayResyltVo>> artOrders(@FieldMap HashMap<String, String> map);


    //check已购买盲盒
    @POST("/api/v2/blind_box_orders/check")
    @FormUrlEncoded
    Call<BaseResponseVo<List<BlindBoxCheckVO>>> blindBoxCheck(@FieldMap HashMap<String, String> map);

    //open已购买盲盒
    @POST("/api/v2/blind_box_orders/open")
    @FormUrlEncoded
    Call<BaseResponseVo<List<BlindBoxOpenVo>>> blindBoxOpen(@FieldMap HashMap<String, String> map);


    @POST("/api/v2/members/change_user_info")
    @FormUrlEncoded
    Call<BaseResponseVo<UserVo>> changeUserInfo(@FieldMap HashMap<String, String> map);

    /*是否可以下单检查*/
    @POST("/api/v2/blind_box_orders/check_order")
    @FormUrlEncoded
    Call<BaseResponseVo<BlindBoxOrderCheck>> orderCheck(@FieldMap HashMap<String, String> map);

    /*盲盒详情*/
    @GET("/api/v2/blind_boxes/{id}")
    Call<BaseResponseVo<BlindBoxVo>> getBlindBoxDetail(@Path("id") String id);


    /*置顶艺术家*/
    @GET("/api/v2/members/artist_topic")
    Call<BaseResponseVo<List<ArtistVo>>> getTopArtist();

    /*艺术家列表*/
    @GET("/api/v2/members/pre_artist_topic")
    Call<BaseResponseVo<List<ArtistListVo>>> getArtistList(@QueryMap HashMap<String, String> map);

    // 新闻列表
    @GET("/api/v2/news")
    Call<BaseResponseVo<List<AnnouncementVo>>> queryNews(@Query("page") int page, @Query("type") String type);

    // 新闻列表
    @GET("/api/v2/arts/popular")
    Call<BaseResponseVo<List<SellingArtVo>>> queryPopular(@Query("page") int page, @Query("per_page") int type);

    // 盲盒列表
    @GET("/api/v2/blind_boxes")
    Call<BaseResponseVo<List<BlindBoxVo>>> queryBlindBox();

    // 新闻列表
    @GET("/api/v2/members/favorate_arts")
    Call<BaseResponseVo<List<CollectArtVo>>> queryCollect(@Query("page") int page, @Query("limit") int type);

    // 新闻列表
    @GET("/api/v2/arts/mine")
    Call<BaseResponseVo<List<SellingArtVo>>> queryMine(@QueryMap HashMap<String, String> map);


    @GET("/api/v2/arts/selling")
    Call<BaseResponseVo<List<SellingArtVo>>> querySelling(@QueryMap HashMap<String, String> map);

    @GET("/api/v2/blind_box_orders/{id}/history")
    Call<BaseResponseVo<List<BlindBoxRecordVo>>> queryBox(@Path("id") String id, @QueryMap HashMap<String, String> map);


    @GET("/api/v2/members/followings")
    Call<BaseResponseVo<List<FollowerVO>>> queryFollowings(@QueryMap HashMap<String, String> map);

    @GET("/api/v2/members/followers")
    Call<BaseResponseVo<List<FollowerVO>>> queryFollowers(@QueryMap HashMap<String, String> map);

    /*买入订单*/
    @GET("/api/v2/arts/bought")
    Call<BaseResponseVo<List<BoughtArtVo>>> queryBought(@QueryMap HashMap<String, String> map);

    /*卖出订单*/
    @GET("/api/v2/arts/sold")
    Call<BaseResponseVo<List<BoughtArtVo>>> querySold(@QueryMap HashMap<String, String> map);

    /*地址注册*/
    @POST("/api/v2/members/user_address_login")
    Call<BaseResponseVo<UserVo>> addressLogin(@QueryMap HashMap<String, String> map);

    @GET("/api/v2/arts/search")
    Call<BaseResponseVo<List<SellingArtVo>>> searchArt(@QueryMap HashMap<String, String> map);

    @GET("/api/v2/members/{id}/arts")
    Call<BaseResponseVo<List<SellingArtVo>>> queryUserArts(@Path("id") String id);

    @GET("/api/v2/arts/{id}/orders")
    Call<BaseResponseVo<List<OrderAmountVo>>> queryOrderAmount(@Path("id") String id);

    @GET("/api/v2/members/{id}")
    Call<BaseResponseVo<MemberInfo>> queryMemberInfo(@Path("id") String id);


    // 主题列表
    @GET("/api/v2/arts/categories")
    Call<BaseResponseVo<List<ArtTypeVo>>> queryCategories();


    // 类型列表
    @GET("/api/v2/arts/art_types")
    Call<BaseResponseVo<List<ArtTypeVo>>> queryArtTypes();

    // 新闻列表
    @GET("/api/v2/arts/topic")
    Call<BaseResponseVo<List<ArtTopicVo>>> queryTheme();

    // 价格区间
    @GET("/api/v2/arts/prices")
    Call<BaseResponseVo<List<ArtPriceVo>>> queryPrize();


    // 绑定邮箱
    @POST("/api/v2/members/bind_phone")
    @FormUrlEncoded
    Call<BaseResponseVo> bindPhone(@Field("phone_number") String phone_number,
                                   @Field("phone_token") String phone_token);


    // 通知
    @GET("/api/v2/messages")
    Call<BaseResponseVo<List<MessagesVo>>> queryNotices(@Query("page") int page);

    // 通知
    @POST("/api/v2/messages/read")
    @FormUrlEncoded
    Call<BaseResponseVo> readMessage(@FieldMap HashMap<String, String> map);

    //
// 全部已读
    @POST("/api/v2/messages/read_all")
    Call<BaseResponseVo> readAllMessage();


    // 身份核验:身份证姓名和号码一致性核验
    @POST("/api/v1/members/validity_idnumber_verification")
    @FormUrlEncoded
    Call<BaseResponseVo> idCardsCheck(@Field("name") String name, @Field("id_number") String id_number);


    // 上传身份图片
    @POST("/api/v1/members/change_user_info")
    Call<BaseResponseVo<UserVo>> uploadIdImages(@Body RequestBody mRequestBody);

    @POST("/api/v2/arts")
    Call<BaseResponseVo<UserVo>> uploadArts(@Body RequestBody mRequestBody);

    @POST("/api/v2/arts/upload_live2d_file")
    Call<BaseResponseVo<UploadLive2dVo>> uploadLive2d(@Body RequestBody mRequestBody);


    //检测更新
    @GET("/api/v1/mobile_versions/info")
    Call<BaseResponseVo<AppUpdateVo>> checkUpdate(@Query("phone_type") String phone_type, @Query("version_code") int version_code);


    @POST("/api/v2/arts/{id}/like")
    @FormUrlEncoded
    Call<BaseResponseVo<SellingArtVo>> like(@Path("id") String id, @FieldMap HashMap<String, String> map);

    @POST("/api/v2/members/send_sms")
    @FormUrlEncoded
    Call<BaseResponseVo> sendCode(@FieldMap HashMap<String, String> map);


    @POST("/api/v2/arts/{id}/cancel_like")
    @FormUrlEncoded
    Call<BaseResponseVo<SellingArtVo>> cancleLike(@Path("id") String id, @FieldMap HashMap<String, String> map);

    @POST("/api/v2/members/{id}/follow")
    @FormUrlEncoded
    Call<BaseResponseVo<MemberInfo>> followAction(@Path("id") int id, @FieldMap HashMap<String, String> map);

    @POST("/api/v2/members/{id}/unfollow")
    @FormUrlEncoded
    Call<BaseResponseVo<MemberInfo>> unFollowAction(@Path("id") int id, @FieldMap HashMap<String, String> map);

    @POST("/api/v2/arts/{id}/dislike")
    @FormUrlEncoded
    Call<BaseResponseVo<SellingArtVo>> disLike(@Path("id") String id, @FieldMap HashMap<String, String> map);

    @POST("/api/v2/arts/{id}/cancel_dislike")
    @FormUrlEncoded
    Call<BaseResponseVo<SellingArtVo>> cancleDisLike(@Path("id") String id, @FieldMap HashMap<String, String> map);

    @POST("/api/v2/arts/{id}/favorite")
    @FormUrlEncoded
    Call<BaseResponseVo<SellingArtVo>> collect(@Path("id") String id, @FieldMap HashMap<String, String> map);

    @POST("/api/v2/arts/{id}/unfavorite")
    @FormUrlEncoded
    Call<BaseResponseVo<SellingArtVo>> disCollect(@Path("id") String id, @FieldMap HashMap<String, String> map);


    // 艺术品详情
    @GET("/api/v2/arts/{id}")
    Call<BaseResponseVo<SellingArtVo>> artInfo(@Path("id") String artId);

    @POST("/api/v2/art_orders/cancel")
    @FormUrlEncoded
    Call<BaseResponseVo<SellingArtVo>> undercarriageArt(@FieldMap HashMap<String, String> map);


    /*协议详情*/
    @GET("/api/v2/members/user_agreement")
    Call<BaseResponseVo<UserAggrementVo>> getAgreement();

    /*创建艺术品拍卖*/
    @POST("/api/v2/auctions")
    @FormUrlEncoded
    Call<BaseResponseVo<AuctionVo>> startAuction(@FieldMap HashMap<String, String> map);

    /*账单明细*/
    @GET("/api/v2/account_histories")
    Call<BaseResponseVo<List<HistoriesBean>>> queryAccountHistory(@Query("page") int page);
}

