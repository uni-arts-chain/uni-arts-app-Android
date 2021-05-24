package com.yunhualian.net;

import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.ArtPriceVo;
import com.yunhualian.entity.ArtTypeVo;
import com.yunhualian.entity.BaseResponseVo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GetBaseData {
    DataListener dataListener;

    public static void getPrice(DataListener dataListener) {
        RequestManager.instance().queryPrize(new MinerCallback<BaseResponseVo<List<ArtPriceVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtPriceVo>>> call, Response<BaseResponseVo<List<ArtPriceVo>>> response) {
                if (response.isSuccessful()) {
                    List<ArtPriceVo> pricesVoList = response.body().getBody();
                    if (pricesVoList.size() > 0) {
                        YunApplication.setArtPriceVoList(pricesVoList);
                        dataListener.onResult();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<ArtPriceVo>>> call, Response<BaseResponseVo<List<ArtPriceVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }

    public static void getCategories(DataListener dataListener) {
        RequestManager.instance().queryCategories(new MinerCallback<BaseResponseVo<List<ArtTypeVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtTypeVo>>> call, Response<BaseResponseVo<List<ArtTypeVo>>> response) {
                if (response.isSuccessful()) {
                    List<ArtTypeVo> typeVoList = response.body().getBody();
                    if (typeVoList.size() > 0) {
                        YunApplication.setArtThemeVoList(typeVoList);
                        dataListener.onResult();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<ArtTypeVo>>> call, Response<BaseResponseVo<List<ArtTypeVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    /*
     * 获取作品类型
     * */
    public static void getArtType(DataListener dataListener) {
        RequestManager.instance().queryArtType(new MinerCallback<BaseResponseVo<List<ArtTypeVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtTypeVo>>> call, Response<BaseResponseVo<List<ArtTypeVo>>> response) {
                if (response.isSuccessful()) {
                    List<ArtTypeVo> typeVoList = response.body().getBody();
                    if (typeVoList.size() > 0) {
                        YunApplication.setArtTypelist(typeVoList);
                        dataListener.onResult();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<ArtTypeVo>>> call, Response<BaseResponseVo<List<ArtTypeVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }

    public interface DataListener {
        public void onResult();
    }
}
