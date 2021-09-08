package com.gammaray.eth.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Metadata implements Parcelable {
    public String value;
    public String symbol;
    public String decimals;

    public Metadata() {

    }

    private Metadata(Parcel in) {
        value = in.readString();
        symbol = in.readString();
        decimals = in.readString();
    }

    public static final Creator<Metadata> CREATOR = new Creator<Metadata>() {
        @Override
        public Metadata createFromParcel(Parcel in) {
            return new Metadata(in);
        }

        @Override
        public Metadata[] newArray(int size) {
            return new Metadata[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(symbol);
        parcel.writeString(decimals);
        parcel.writeString(value);
    }
}
