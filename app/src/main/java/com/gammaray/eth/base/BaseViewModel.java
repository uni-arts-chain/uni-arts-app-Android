package com.gammaray.eth.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gammaray.eth.entity.ErrorEnvelope;
import com.gammaray.eth.entity.ServiceException;

import io.reactivex.disposables.Disposable;

public class BaseViewModel extends ViewModel {
    protected final MutableLiveData<ErrorEnvelope> error = new MutableLiveData<>();

    protected final MutableLiveData<String> requestError = new MutableLiveData<>();
    protected final MutableLiveData<Boolean> progress = new MutableLiveData<>();
    protected Disposable disposable;

    @Override
    protected void onCleared() {
        cancel();
    }

    protected void cancel() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public LiveData<ErrorEnvelope> error() {
        return error;
    }
    public LiveData<String> requestError() {
        return requestError;
    }
    public LiveData<Boolean> progress() {
        return progress;
    }
    protected void onError(Throwable throwable) {
        if (throwable instanceof ServiceException) {
            error.postValue(((ServiceException) throwable).error);
        } else {
            error.postValue(new ErrorEnvelope(C.ErrorCode.UNKNOWN, null, throwable));
        }
    }
}
