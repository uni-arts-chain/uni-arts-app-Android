package com.yunhualian.net;

import com.google.gson.JsonObject;
import com.yunhualian.R;
import com.yunhualian.adapter.UploadCodeBean;
import com.yunhualian.base.YunApplication;
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
import com.yunhualian.entity.AuctionArtVo;
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
import com.yunhualian.entity.OfferPriceBean;
import com.yunhualian.entity.OrderAmountVo;
import com.yunhualian.entity.PayResult;
import com.yunhualian.entity.PayResyltVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.UploadLive2dVo;
import com.yunhualian.entity.UserAggrementVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.entity.WithDrawsBean;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
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
     * 查询用户信息
     */
    public void queryUser(MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().queryUser();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 查询未读消息
     */
    public void queryHasUnReadMessage(MinerCallback<BaseResponseVo<NoRead>> mCallBack) {
        Call<BaseResponseVo<NoRead>> mCall = NetworkManager.instance().getmRemoteService().queryHasUnReadMessage();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }


    /**
     * 出售锁定地址
     */
    public void queryAccountId(MinerCallback<BaseResponseVo<AccountIdVo>> mCallBack) {
        Call<BaseResponseVo<AccountIdVo>> mCall = NetworkManager.instance().getmRemoteService().queryAccountId();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 拍卖锁定地址
     */
    public void queryAuctionAccountId(MinerCallback<BaseResponseVo<AccountIdVo>> mCallBack) {
        Call<BaseResponseVo<AccountIdVo>> mCall = NetworkManager.instance().getmRemoteService().queryAuctionAccountId();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 邮箱登录
     */
    public void feedBack(HashMap<String, String> map, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().feedBack(map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 兑换
     */
    public void exchangeNFT(HashMap<String, String> map, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().exchange(map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 上架卖作品
     */
    public void sellArt(HashMap<String, String> map, MinerCallback<BaseResponseVo<SellingArtVo>> mCallBack) {
        Call<BaseResponseVo<SellingArtVo>> mCall = NetworkManager.instance().getmRemoteService().sellArt(map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }


    /**
     * Banners
     *
     * @param mCallBack
     */
    public void queryBanner(MinerCallback<BaseResponseVo<List<BannersVo>>> mCallBack) {
        Call<BaseResponseVo<List<BannersVo>>> mCall = NetworkManager.instance().getmRemoteService().queryBanner(1);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * Banners
     *
     * @param mCallBack
     */
    public void queryAuction(MinerCallback<BaseResponseVo<List<ArtAuctionVo>>> mCallBack) {
        Call<BaseResponseVo<List<ArtAuctionVo>>> mCall = NetworkManager.instance().getmRemoteService().queryAuction(1);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    //
//    /**
//     * 通知
//     *
//     * @param mCallBack
//     */
    public void queryNotices(int page, MinerCallback<BaseResponseVo<List<MessagesVo>>> mCallBack) {
        Call<BaseResponseVo<List<MessagesVo>>> mCall = NetworkManager.instance().getmRemoteService().queryNotices(page);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*已读*/
    public void readMessage(HashMap<String, String> map, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().readMessage(map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*全部已读*/
    public void readAllMessage(MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().readAllMessage();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }


    /**
     * 通知列表
     *
     * @param mCallBack mCallBack
     */
    public void queryNews(int page, String type, MinerCallback<BaseResponseVo<List<AnnouncementVo>>> mCallBack) {
        Call<BaseResponseVo<List<AnnouncementVo>>> mCall = NetworkManager.instance().getmRemoteService().queryNews(page, type);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 首页流行列表
     *
     * @param mCallBack mCallBack
     */
    public void queryPopular(int page, int limit, MinerCallback<BaseResponseVo<List<SellingArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<SellingArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryPopular(page, limit);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 盲盒列表
     *
     * @param mCallBack mCallBack
     */
    public void queryBlindBoxes(MinerCallback<BaseResponseVo<List<BlindBoxVo>>> mCallBack) {
        Call<BaseResponseVo<List<BlindBoxVo>>> mCall = NetworkManager.instance().getmRemoteService().queryBlindBox();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 我的收藏列表
     *
     * @param mCallBack mCallBack
     */
    public void queryCollect(int page, int limit, MinerCallback<BaseResponseVo<List<CollectArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<CollectArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryCollect(page, limit);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 我的作品列表
     *
     * @param mCallBack mCallBack
     */
    public void queryMine(HashMap<String, String> param, MinerCallback<BaseResponseVo<List<SellingArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<SellingArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryMine(param);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 获取分类数据
     * */
    public void querySelling(HashMap<String, String> params, MinerCallback<BaseResponseVo<List<SellingArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<SellingArtVo>>> mCall = NetworkManager.instance().getmRemoteService().querySelling(params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 获取分类数据
     * */
    public void queryBoxRecord(String id, HashMap<String, String> params, MinerCallback<BaseResponseVo<List<BlindBoxRecordVo>>> mCallBack) {
        Call<BaseResponseVo<List<BlindBoxRecordVo>>> mCall = NetworkManager.instance().getmRemoteService().queryBox(id, params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 获取分类数据
     * */
    public void queryFollowings(HashMap<String, String> params, MinerCallback<BaseResponseVo<List<FollowerVO>>> mCallBack) {
        Call<BaseResponseVo<List<FollowerVO>>> mCall = NetworkManager.instance().getmRemoteService().queryFollowings(params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 获取分类数据
     * */
    public void queryFollowers(HashMap<String, String> params, MinerCallback<BaseResponseVo<List<FollowerVO>>> mCallBack) {
        Call<BaseResponseVo<List<FollowerVO>>> mCall = NetworkManager.instance().getmRemoteService().queryFollowers(params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 查询买入数据
     * */
    public void queryBrought(HashMap<String, String> params, MinerCallback<BaseResponseVo<List<BoughtArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<BoughtArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryBought(params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 查询卖出数据
     * */
    public void querySold(HashMap<String, String> params, MinerCallback<BaseResponseVo<List<BoughtArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<BoughtArtVo>>> mCall = NetworkManager.instance().getmRemoteService().querySold(params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 地址登录
     * */
    public void addressLogin(HashMap<String, String> params, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().addressLogin(params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 搜索作品
     * */
    public void searchArts(HashMap<String, String> params, MinerCallback<BaseResponseVo<List<SellingArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<SellingArtVo>>> mCall = NetworkManager.instance().getmRemoteService().searchArt(params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 搜索拍卖作品
     * */
    public void searchAuctions(HashMap<String, String> params, MinerCallback<BaseResponseVo<List<AuctionArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<AuctionArtVo>>> mCall = NetworkManager.instance().getmRemoteService().searchAuctions(params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 搜索作品
     * */
    public void searchUserArts(String uid, MinerCallback<BaseResponseVo<List<SellingArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<SellingArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryUserArts(uid);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 搜索作品
     * */
    public void queryOrderAmounts(String uid, MinerCallback<BaseResponseVo<List<OrderAmountVo>>> mCallBack) {
        Call<BaseResponseVo<List<OrderAmountVo>>> mCall = NetworkManager.instance().getmRemoteService().queryOrderAmount(uid);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 搜索作品
     * */
    public void searchMemberInfo(String uid, MinerCallback<BaseResponseVo<MemberInfo>> mCallBack) {
        Call<BaseResponseVo<MemberInfo>> mCall = NetworkManager.instance().getmRemoteService().queryMemberInfo(uid);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 通知列表
     *
     * @param mCallBack mCallBack
     */
    public void queryTheme(MinerCallback<BaseResponseVo<List<ArtTopicVo>>> mCallBack) {
        Call<BaseResponseVo<List<ArtTopicVo>>> mCall = NetworkManager.instance().getmRemoteService().queryTheme();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 价格列表
     *
     * @param mCallBack mCallBack
     */
    public void queryPrize(MinerCallback<BaseResponseVo<List<ArtPriceVo>>> mCallBack) {
        Call<BaseResponseVo<List<ArtPriceVo>>> mCall = NetworkManager.instance().getmRemoteService().queryPrize();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * blindBoxOrder
     *
     * @param mCallBack mCallBack
     */
    public void blindBoxOrders(HashMap<String, String> param, MinerCallback<BaseResponseVo<PayResult>> mCallBack) {
        Call<BaseResponseVo<PayResult>> mCall = NetworkManager.instance().getmRemoteService().blindBoxOrders(param);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * artOrder
     *
     * @param mCallBack mCallBack
     */
    public void artOrders(HashMap<String, String> param, MinerCallback<BaseResponseVo<PayResyltVo>> mCallBack) {
        Call<BaseResponseVo<PayResyltVo>> mCall = NetworkManager.instance().getmRemoteService().artOrders(param);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * blindBox_checkOrder
     *
     * @param mCallBack mCallBack
     */
    public void blindBoxOrderCheck(HashMap<String, String> param, MinerCallback<BaseResponseVo<BlindBoxOrderCheck>> mCallBack) {
        Call<BaseResponseVo<BlindBoxOrderCheck>> mCall = NetworkManager.instance().getmRemoteService().orderCheck(param);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * blindBox_Detail
     *
     * @param mCallBack mCallBack
     */
    public void blindBoxDetail(String param, MinerCallback<BaseResponseVo<BlindBoxVo>> mCallBack) {
        Call<BaseResponseVo<BlindBoxVo>> mCall = NetworkManager.instance().getmRemoteService().getBlindBoxDetail(param);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * blindBoxOrder
     *
     * @param mCallBack mCallBack
     */
    public void checkBlindBoxs(HashMap<String, String> param, MinerCallback<BaseResponseVo<List<BlindBoxCheckVO>>> mCallBack) {
        Call<BaseResponseVo<List<BlindBoxCheckVO>>> mCall = NetworkManager.instance().getmRemoteService().blindBoxCheck(param);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void openBlindBoxs(HashMap<String, String> param, MinerCallback<BaseResponseVo<List<BlindBoxOpenVo>>> mCallBack) {
        Call<BaseResponseVo<List<BlindBoxOpenVo>>> mCall = NetworkManager.instance().getmRemoteService().blindBoxOpen(param);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }


    /**
     * 通知作品类型
     *
     * @param mCallBack mCallBack
     */
    public void queryArtType(MinerCallback<BaseResponseVo<List<ArtTypeVo>>> mCallBack) {
        Call<BaseResponseVo<List<ArtTypeVo>>> mCall = NetworkManager.instance().getmRemoteService().queryArtTypes();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }


    /*
     *
     * */
    public void queryCategories(MinerCallback<BaseResponseVo<List<ArtTypeVo>>> mCallBack) {
        Call<BaseResponseVo<List<ArtTypeVo>>> mCall = NetworkManager.instance().getmRemoteService().queryCategories();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }


    /**
     * 绑定手机号
     *
     * @param phone_token 用户输入手机号
     * @param mCallBack   mCallBack
     */
    public void bindPhone(String phone,
                          String phone_token,
                          MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().bindPhone(phone, phone_token);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }


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
    public void uploadIdImages(RequestBody mRequestBody, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().uploadIdImages(mRequestBody);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 上传艺术品*/
    public void uploadArt(RequestBody mRequestBody, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().uploadArts(mRequestBody);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 上传Live2d*/
    public void uploadLive2d(RequestBody mRequestBody, MinerCallback<BaseResponseVo<UploadLive2dVo>> mCallBack) {
        Call<BaseResponseVo<UploadLive2dVo>> mCall = NetworkManager.instance().getmRemoteService().uploadLive2d(mRequestBody);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

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


    public void like(String art_id, HashMap<String, String> map, MinerCallback<BaseResponseVo<SellingArtVo>> mCallBack) {
        Call<BaseResponseVo<SellingArtVo>> mCall = NetworkManager.instance().getmRemoteService().like(art_id, map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void sendCode(HashMap<String, String> map, MinerCallback<BaseResponseVo> mCallBack) {
        Call<BaseResponseVo> mCall = NetworkManager.instance().getmRemoteService().sendCode(map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void followAction(int art_id, HashMap<String, String> map, MinerCallback<BaseResponseVo<MemberInfo>> mCallBack) {
        Call<BaseResponseVo<MemberInfo>> mCall = NetworkManager.instance().getmRemoteService().followAction(art_id, map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void unFollow(int art_id, HashMap<String, String> map, MinerCallback<BaseResponseVo<MemberInfo>> mCallBack) {
        Call<BaseResponseVo<MemberInfo>> mCall = NetworkManager.instance().getmRemoteService().unFollowAction(art_id, map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void changeUserInfo(HashMap<String, String> map, MinerCallback<BaseResponseVo<UserVo>> mCallBack) {
        Call<BaseResponseVo<UserVo>> mCall = NetworkManager.instance().getmRemoteService().changeUserInfo(map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*获取置顶艺术家*/
    public void getTopArtist(MinerCallback<BaseResponseVo<List<ArtistVo>>> mCallBack) {
        Call<BaseResponseVo<List<ArtistVo>>> mCall = NetworkManager.instance().getmRemoteService().getTopArtist();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*获取艺术家列表*/
    public void getTopArtistList(HashMap<String, String> map, MinerCallback<BaseResponseVo<List<ArtistListVo>>> mCallBack) {
        Call<BaseResponseVo<List<ArtistListVo>>> mCall = NetworkManager.instance().getmRemoteService().getArtistList(map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void canclelike(String art_id, HashMap<String, String> map, MinerCallback<BaseResponseVo<SellingArtVo>> mCallBack) {
        Call<BaseResponseVo<SellingArtVo>> mCall = NetworkManager.instance().getmRemoteService().cancleLike(art_id, map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void dislike(String art_id, HashMap<String, String> map, MinerCallback<BaseResponseVo<SellingArtVo>> mCallBack) {
        Call<BaseResponseVo<SellingArtVo>> mCall = NetworkManager.instance().getmRemoteService().disLike(art_id, map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void cancleDislike(String art_id, HashMap<String, String> map, MinerCallback<BaseResponseVo<SellingArtVo>> mCallBack) {
        Call<BaseResponseVo<SellingArtVo>> mCall = NetworkManager.instance().getmRemoteService().cancleDisLike(art_id, map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void collect(String art_id, HashMap<String, String> map, MinerCallback<BaseResponseVo<SellingArtVo>> mCallBack) {
        Call<BaseResponseVo<SellingArtVo>> mCall = NetworkManager.instance().getmRemoteService().collect(art_id, map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void discollect(String art_id, HashMap<String, String> map, MinerCallback<BaseResponseVo<SellingArtVo>> mCallBack) {
        Call<BaseResponseVo<SellingArtVo>> mCall = NetworkManager.instance().getmRemoteService().disCollect(art_id, map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void artInfo(String art_id, MinerCallback<BaseResponseVo<SellingArtVo>> mCallBack) {
        Call<BaseResponseVo<SellingArtVo>> mCall = NetworkManager.instance().getmRemoteService().artInfo(art_id);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void undercarriageArt(HashMap<String, String> map, MinerCallback<BaseResponseVo<SellingArtVo>> mCallBack) {
        Call<BaseResponseVo<SellingArtVo>> mCall = NetworkManager.instance().getmRemoteService().undercarriageArt(map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void getAgreement(MinerCallback<BaseResponseVo<UserAggrementVo>> mCallBack) {
        Call<BaseResponseVo<UserAggrementVo>> mCall = NetworkManager.instance().getmRemoteService().getAgreement();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    //创建拍卖艺术品
    public void startAuction(HashMap<String, String> map, MinerCallback<BaseResponseVo<AuctionArtVo>> mCallBack) {
        Call<BaseResponseVo<AuctionArtVo>> mCall = NetworkManager.instance().getmRemoteService().startAuction(map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 查询账户信息
     */
    public void queryAccount(MinerCallback<BaseResponseVo<List<AccountVo>>> mCallBack) {
        Call<BaseResponseVo<List<AccountVo>>> mCall = NetworkManager.instance().getmRemoteService().queryAccountInfo();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 查询账单明细
     */
    public void queryAccountHistory(int page, MinerCallback<BaseResponseVo<List<HistoriesBean>>> mCallBack) {
        Call<BaseResponseVo<List<HistoriesBean>>> mCall = NetworkManager.instance().getmRemoteService().queryAccountHistory(page);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 上传微信，支付宝图片
     *
     * @param mCallBack
     */
    public void uploadQrCode(RequestBody mRequestBody, MinerCallback<BaseResponseVo<UploadCodeBean>> mCallBack) {
        Call<BaseResponseVo<UploadCodeBean>> mCall = NetworkManager.instance().getmRemoteService().uploadQrCodeImg(mRequestBody);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /**
     * 上传微信，支付宝图片
     *
     * @param mCallBack
     */
    public void updateQrCode(RequestBody mRequestBody, MinerCallback<BaseResponseVo<UploadCodeBean>> mCallBack) {
        Call<BaseResponseVo<UploadCodeBean>> mCall = NetworkManager.instance().getmRemoteService().updateQrCodeImg(mRequestBody);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 获取拍卖列表
     * */
    public void queryAuctions(HashMap<String, String> params, MinerCallback<BaseResponseVo<List<AuctionArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<AuctionArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryAuctions(params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 提现
     * */
    public void withdraws(HashMap<String, String> params, MinerCallback<BaseResponseVo<WithDrawsBean>> mCallBack) {
        Call<BaseResponseVo<WithDrawsBean>> mCall = NetworkManager.instance().getmRemoteService().withdraws(params);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    /*
     * 获取首页拍卖精品
     * */
    public void queryHomePageAuctions(int page, int limit, MinerCallback<BaseResponseVo<List<AuctionArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<AuctionArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryHomePageAuctions(page, limit);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void auctionLike(String art_id, HashMap<String, String> map, MinerCallback<BaseResponseVo<AuctionArtVo>> mCallBack) {
        Call<BaseResponseVo<AuctionArtVo>> mCall = NetworkManager.instance().getmRemoteService().auctionLike(art_id, map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void auctionCancelLike(String art_id, HashMap<String, String> map, MinerCallback<BaseResponseVo<AuctionArtVo>> mCallBack) {
        Call<BaseResponseVo<AuctionArtVo>> mCall = NetworkManager.instance().getmRemoteService().auctionCancleLike(art_id, map);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void auctionArtInfo(String art_id, MinerCallback<BaseResponseVo<AuctionArtVo>> mCallBack) {
        Call<BaseResponseVo<AuctionArtVo>> mCall = NetworkManager.instance().getmRemoteService().auctionArtInfo(art_id);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void queryOfferPriceList(String art_id, int page, int page_size, MinerCallback<BaseResponseVo<List<OfferPriceBean>>> mCallBack) {
        Call<BaseResponseVo<List<OfferPriceBean>>> mCall = NetworkManager.instance().getmRemoteService().queryOfferPriceList(art_id, page, page_size);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void payForDeposit(HashMap<String, String> param, MinerCallback<BaseResponseVo<PayResyltVo>> mCallBack) {
        Call<BaseResponseVo<PayResyltVo>> mCall = NetworkManager.instance().getmRemoteService().payForDeposit(param);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void cancelAuction(String art_id, MinerCallback<BaseResponseVo<AuctionArtVo>> mCallBack) {
        Call<BaseResponseVo<AuctionArtVo>> mCall = NetworkManager.instance().getmRemoteService().cancelAuction(art_id);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void offerPrice(String art_id, HashMap<String, String> param, MinerCallback<BaseResponseVo<OfferPriceBean>> mCallBack) {
        Call<BaseResponseVo<OfferPriceBean>> mCall = NetworkManager.instance().getmRemoteService().offerPrice(art_id, param);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void queryMyAuctions(MinerCallback<BaseResponseVo<List<AuctionArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<AuctionArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryMyAuctions();
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void queryAttendAuctions(int page, int page_size, MinerCallback<BaseResponseVo<List<AuctionArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<AuctionArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryAttendAuctions(page, page_size);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void queryBidAuctions(int page, int page_size, MinerCallback<BaseResponseVo<List<AuctionArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<AuctionArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryBidAuctions(page, page_size);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void queryWinAuctions(int page, int page_size, MinerCallback<BaseResponseVo<List<AuctionArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<AuctionArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryWinAuctions(page, page_size);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }

    public void queryFinishAuctions(int page, int page_size, MinerCallback<BaseResponseVo<List<AuctionArtVo>>> mCallBack) {
        Call<BaseResponseVo<List<AuctionArtVo>>> mCall = NetworkManager.instance().getmRemoteService().queryFinishAuctions(page, page_size);
        NetworkManager.instance().postReq(mCallBack, mCall);
    }
}
