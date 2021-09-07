package com.gammaray.entity;

import java.io.Serializable;

public class EventBusMessageEvent implements Serializable {
    private static final long serialVersionUID = 3752519392059961357L;
    private String mMessage;
    private Object mValue;

    public EventBusMessageEvent(String mMessage, Object mValue) {
        this.mMessage = mMessage;
        this.mValue = mValue;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public Object getmValue() {
        return mValue;
    }

    public void setmValue(Object mValue) {
        this.mValue = mValue;
    }
}
