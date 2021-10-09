package com.gammaray.net;

import com.gammaray.adapter.UploadCodeBean;
import com.gammaray.entity.AccountIdVo;
import com.gammaray.entity.AccountVo;
import com.gammaray.entity.AnnouncementVo;
import com.gammaray.entity.AppUpdateVo;
import com.gammaray.entity.ArtAuctionVo;
import com.gammaray.entity.ArtPriceVo;
import com.gammaray.entity.ArtTopicVo;
import com.gammaray.entity.ArtTypeVo;
import com.gammaray.entity.ArtistListVo;
import com.gammaray.entity.ArtistVo;
import com.gammaray.entity.AuctionArtVo;
import com.gammaray.entity.BannersVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.BlindBoxCheckVO;
import com.gammaray.entity.BlindBoxOpenVo;
import com.gammaray.entity.BlindBoxOrderCheck;
import com.gammaray.entity.BlindBoxRecordVo;
import com.gammaray.entity.BlindBoxVo;
import com.gammaray.entity.BoughtArtVo;
import com.gammaray.entity.ChainBean;
import com.gammaray.entity.CollectArtVo;
import com.gammaray.entity.DAppFavouriteBean;
import com.gammaray.entity.DAppGroupBean;
import com.gammaray.entity.DAppSearchBean;
import com.gammaray.entity.DAppItemBean;
import com.gammaray.entity.DAppRecentlyBean;
import com.gammaray.entity.FollowerVO;
import com.gammaray.entity.HistoriesBean;
import com.gammaray.entity.MemberInfo;
import com.gammaray.entity.MessagesVo;
import com.gammaray.entity.NoRead;
import com.gammaray.entity.OfferPriceBean;
import com.gammaray.entity.OrderAmountVo;
import com.gammaray.entity.PayResult;
import com.gammaray.entity.PayResyltVo;
import com.gammaray.entity.SellingArtVo;
import com.gammaray.entity.UploadLive2dVo;
import com.gammaray.entity.UserAggrementVo;
import com.gammaray.entity.UserAuctionsVo;
import com.gammaray.entity.UserVo;
import com.gammaray.entity.WithDrawsBean;

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

    @GET("/api/v2/auctions/search")
    Call<BaseResponseVo<List<AuctionArtVo>>> searchAuctions(@QueryMap HashMap<String, String> map);

    @GET("/api/v2/members/{id}/arts")
    Call<BaseResponseVo<List<SellingArtVo>>> queryUserArts(@Path("id") String id);

    @GET("/api/v2/members/{id}/auctions")
    Call<BaseResponseVo<List<AuctionArtVo>>> searchUserAuctionArts(@Path("id") String id);


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
    Call<BaseResponseVo<AuctionArtVo>> startAuction(@FieldMap HashMap<String, String> map);

    /*账单明细*/
    @GET("/api/v2/account_histories")
    Call<BaseResponseVo<List<HistoriesBean>>> queryAccountHistory(@Query("page") int page, @Query("per_page") int pageSize);

    // 上传wx和alipay收款二维码
    @POST("/api/v2/payment_methods")
    Call<BaseResponseVo<UploadCodeBean>> uploadQrCodeImg(@Body RequestBody mRequestBody);

    // 上传wx和alipay收款二维码
    @POST("/api/v2/payment_methods/update_img")
    Call<BaseResponseVo<UploadCodeBean>> updateQrCodeImg(@Body RequestBody mRequestBody);

    // 获取拍卖列表
    @GET("/api/v2/auctions")
    Call<BaseResponseVo<List<AuctionArtVo>>> queryAuctions(@QueryMap HashMap<String, String> map);

    // 获取拍卖列表
    @POST("/api/v2/withdraws")
    @FormUrlEncoded
    Call<BaseResponseVo<WithDrawsBean>> withdraws(@FieldMap HashMap<String, String> map);

    // 获取推荐拍卖列表
    @GET("/api/v2/auctions/popular")
    Call<BaseResponseVo<List<AuctionArtVo>>> queryHomePageAuctions(@Query("page") int page, @Query("per_page") int pageSize);

    // 拍卖艺术品详情
    @GET("/api/v2/auctions/{id}")
    Call<BaseResponseVo<AuctionArtVo>> auctionArtInfo(@Path("id") String artId);

    // 获取拍卖出价列表
    @GET("/api/v2/auctions/{id}/bid_histories")
    Call<BaseResponseVo<List<OfferPriceBean>>> queryOfferPriceList(@Path("id") String artId, @Query("page") int page, @Query("per_page") int pageSize);

    //创建拍卖保证金
    @POST("/api/v2/auction_deposits")
    @FormUrlEncoded
    Call<BaseResponseVo<PayResyltVo>> payForDeposit(@FieldMap HashMap<String, String> map);

    //创建拍卖保证金
    @POST("/api/v2/auctions/{id}/cancel")
    Call<BaseResponseVo<AuctionArtVo>> cancelAuction(@Path("id") String id);

    //拍卖出价
    @POST("/api/v2/auctions/{id}/bid")
    @FormUrlEncoded
    Call<BaseResponseVo<OfferPriceBean>> offerPrice(@Path("id") String id, @FieldMap HashMap<String, String> map);

    // 我的拍卖列表
    @GET("/api/v2/auctions/mine")
    Call<BaseResponseVo<List<AuctionArtVo>>> queryMyAuctions();

    // 我参与的拍卖列表
    @GET("/api/v2/auctions/attend")
    Call<BaseResponseVo<List<AuctionArtVo>>> queryAttendAuctions(@Query("page") int page, @Query("per_page") int pageSize);

    // 我出价的拍卖列表
    @GET("/api/v2/auctions/bid_auctions")
    Call<BaseResponseVo<List<AuctionArtVo>>> queryBidAuctions(@Query("page") int page, @Query("per_page") int pageSize);

    // 我拍到的拍卖列表
    @GET("/api/v2/auctions/wins")
    Call<BaseResponseVo<List<AuctionArtVo>>> queryWinAuctions(@Query("page") int page, @Query("per_page") int pageSize);

    // 我的已结束拍卖列表
    @GET("/api/v2/auctions/finish")
    Call<BaseResponseVo<List<AuctionArtVo>>> queryFinishAuctions(@Query("page") int page, @Query("per_page") int pageSize);

    // 竞拍须知
    @GET("/api/v2/auctions/notice")
    Call<BaseResponseVo<UserAuctionsVo>> queryAuctionRules();

    //链列表
    @GET("/api/v2/chains")
    Call<BaseResponseVo<List<ChainBean>>> queryChainList();

    //获取对应ID下的推荐Dapp列表
    @GET("/api/v2/chains/{id}/recommend_dapps")
    Call<BaseResponseVo<List<DAppItemBean>>> queryRecommendDApps(@Path("id") String id,@QueryMap HashMap<String, String> map);

    //获取对应ID下的Dapp列表
    @GET("/api/v2/chains/{id}/categories")
    Call<BaseResponseVo<List<DAppGroupBean>>> queryCategoryDApps(@Path("id") String id,@QueryMap HashMap<String, String> map);

    //获取收藏DApp列表
    @GET("/api/v2/dapps/favorites")
    Call<BaseResponseVo<List<DAppFavouriteBean>>> queryCollectedDApps(@Query("page") int page, @Query("per_page") int pageSize);

    //获取热门搜索DApp列表
    @GET("/api/v2/dapps/hot_search_dapps")
    Call<BaseResponseVo<List<DAppSearchBean>>> queryHotSearchDApps();

    //获取最近DApp列表
    @GET("/api/v2/member_recently_dapps")
    Call<BaseResponseVo<List<DAppRecentlyBean>>> queryRecentlyDApps(@Query("page") int page, @Query("per_page") int pageSize);

    //上传最近DApp id
    @POST("/api/v2/member_recently_dapps")
    @FormUrlEncoded
    Call<BaseResponseVo<DAppRecentlyBean>> sendRecentlyDApps(@FieldMap HashMap<String, String> map);

    //收藏DApp
    @POST("/api/v2/dapps/{id}/favorite")
    Call<BaseResponseVo<DAppItemBean>> favoriteDApp(@Path("id") String id);

    //取消收藏DApp
    @POST("/api/v2/dapps/{id}/unfavorite")
    Call<BaseResponseVo<String>> unfavoriteDApp(@Path("id") String id);

    //搜索Dapps
    @GET("/api/v2/dapps/search")
    Call<BaseResponseVo<List<DAppSearchBean>>> searchDApps(@QueryMap HashMap<String, String> map);

    //链分类下的Dapp
    @GET("/api/v2/dapps/category_dapps")
    Call<BaseResponseVo<List<DAppItemBean>>> queryChainDApps(@QueryMap HashMap<String, String> map);
}

