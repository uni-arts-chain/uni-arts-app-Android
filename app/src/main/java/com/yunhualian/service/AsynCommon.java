package com.yunhualian.service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.yunhualian.R;
import com.yunhualian.net.DTRequest;
import com.yunhualian.net.ErrorVo;
import com.yunhualian.net.OnResultListener;
import com.yunhualian.net.SuccessVo;

import java.net.ConnectException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Yanmin on 2016/3/12.
 */
public class AsynCommon {
    private Context mContext;
    private OnResultListener mListener;
    private Call restResponse;
    private DTRequest mRequest;
    private Head head;
    private DTResponse dtResponse;

    public static AsynCommon SendRequest(API api, HashMap<String, String> params, boolean silent, boolean showErrorMsg, OnResultListener listener, Context context) {
        DTRequest request = new DTRequest(api, params, silent, showErrorMsg);
        AsynCommon task = new AsynCommon(context, listener, request);
        task.execute();

        return task;
    }

    public AsynCommon(Context context, OnResultListener listener,
                      DTRequest request) {
        this.mRequest = request;
        this.mContext = context;
        this.mListener = listener;
    }

    public DTResponse getDTResponse(Response response) {
        if (head == null)
            head = new Head();

        if (dtResponse == null)
            dtResponse = new DTResponse();

        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(response.body().toString());
            ErrorVo errorVo = new Gson().fromJson(jsonElement, ErrorVo.class);
            SuccessVo successVo = new Gson().fromJson(jsonElement, SuccessVo.class);
            if (errorVo != null && errorVo.getError() != null) {
                head.setSuccess(false);
                head.setMsg(errorVo.getError().getMessage());
                head.setCode(errorVo.getError().getCode());
            } else if (successVo != null && successVo.getSuccess() != null) {
                head.setCode("1000");
                head.setMsg(successVo.getSuccess().getMessage());
                head.setSuccess(true);
            } else {
                head.setCode("1000");
                head.setSuccess(true);
            }
        } catch (Exception e) {
            head.setCode("1000");
            head.setSuccess(true);
        }

        dtResponse.setHead(head);
        dtResponse.setBody(response.body());

        return dtResponse;
    }

    public boolean execute() {
        if (mRequest == null)
            return false;

        restResponse = mRequest.getApi().request(mRequest.getParams());

        restResponse.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                head = new Head();
                Object body = "";

                if (response.raw().code() == 200 || response.raw().code() == 201) {
                    if (!mRequest.getApi().isResponseJson()) {
                        body = response.body();
                        head.setCode("1000");
                        head.setMsg("Success");
                        head.setSuccess(true);
                    } else {
                        if (response.body() == null) {
                            dtResponse = getDTResponse(response);
                        } else {
                            if (response.body().getClass().getSimpleName().equals("DTResponse")) {
                                dtResponse = (DTResponse) response.body();
                            } else {
                                dtResponse = getDTResponse(response);
                            }
                        }
                        head = dtResponse.getHead();
                        if (null == head)
                            head = new Head();
                        try {
                            body = new Gson().fromJson(dtResponse.getBodyToString(),
                                    mRequest.getApi().getEntryType());

                        } catch (Exception e) {
                            body = dtResponse.getBodyToString();
                        }
                    }
                } else if (response.raw().code() == 503) {
                    head.setCode("503");
                    head.setSuccess(false);
                } else {
                    try {
                        JsonReader jsonReader = new JsonReader(response.errorBody().charStream());
                        jsonReader.setLenient(true);
                        dtResponse = new Gson().fromJson(jsonReader, DTResponse.class);
                        if (null == dtResponse) {
                            head.setCode("" + response.raw().code());
                            head.setMsg(response.message());
                        } else {
                            head = dtResponse.getHead();
                            if (null == head)
                                head = new Head();
                            if (head.getCode().equals("401")
                                    || head.getCode().equals("501")) {
                                //todo 失去登录桩体
//                                RxBus.getInstance().post(RxBusConfig.USER_LOGOUT, null);
                                head.setMsg("");
                            }
                        }
                    } catch (Exception e) {
                        head.setCode("" + response.raw().code());
                        head.setMsg(response.message());
                    }

                }

                mListener.OnResult(mRequest, head, body);
                restResponse = null;
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Head head = new Head();
                final Throwable cause = t.getCause() != null ? t.getCause() : t;
                if (cause != null) {
                    if (cause instanceof ConnectException) {
                        head.setCode("5000");

                        if (mContext != null && mContext.getResources() != null)
                            head.setMsg(mContext.getResources().getString(R.string.request_error));
                    } else {
                        head.setCode("4080");

                        if (mContext != null && mContext.getResources() != null)
                            head.setMsg(mContext.getResources().getString(R.string.request_error));
                    }
                }
                mListener.OnResult(mRequest, head, null);
                restResponse = null;
            }
        });
        return true;
    }

    public void cancel() {
        if (restResponse != null)
            restResponse.cancel();
    }
}
