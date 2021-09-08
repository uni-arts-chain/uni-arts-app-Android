package com.gammaray.eth.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class Transaction implements Parcelable {
    @SerializedName("id")
    public final String id;
    public final String hash;
    public final String block;
    public final long timeStamp;
    public final int nonce;
    public final String from;
    public final String to;
    public final String value;
    public final String gas;
    public final String gasPrice;
    public final String gasUsed;
    public final String input;
    public final TransactionOperation[] operations;
    public final String contract;
    public final String error;
    public final Metadata metadata;
    public final String direction;
    public final String date;
    public final String coin;
    public final String sequence;
    public final String status;

    public Transaction(
            String id, String hash,
            String error,
            String block,
            long timeStamp,
            int nonce,
            String from,
            String to,
            String value,
            String gas,
            String gasPrice,
            String input,
            String gasUsed,
            TransactionOperation[] operations,
            Metadata metadata, String direction, String date, String coin, String sequence, String status,
            String contract) {
        this.id = id;
        this.hash = hash;
        this.error = error;
        this.block = block;
        this.timeStamp = timeStamp;
        this.nonce = nonce;
        this.from = from;
        this.to = to;
        this.value = value;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.input = input;
        this.gasUsed = gasUsed;
        this.operations = operations;
        this.contract = contract;
        this.metadata = metadata;
        this.direction = direction;
        this.date = date;
        this.coin = coin;
        this.sequence = sequence;
        this.status = status;
    }

    protected Transaction(Parcel in) {
        id = in.readString();
        hash = in.readString();
        error = in.readString();
        block = in.readString();
        timeStamp = in.readLong();
        nonce = in.readInt();
        from = in.readString();
        to = in.readString();
        value = in.readString();
        gas = in.readString();
        gasPrice = in.readString();
        input = in.readString();
        gasUsed = in.readString();
        direction = in.readString();
        metadata = in.readParcelable(Metadata.class.getClassLoader());
        Parcelable[] parcelableArray = in.readParcelableArray(TransactionOperation.class.getClassLoader());
        TransactionOperation[] operations = null;
        if (parcelableArray != null) {
            operations = Arrays.copyOf(parcelableArray, parcelableArray.length, TransactionOperation[].class);
        }
        this.operations = operations;
        this.contract = in.readString();
        date = in.readString();
        coin = in.readString();
        sequence = in.readString();
        status = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hash);
        dest.writeString(error);
        dest.writeString(block);
        dest.writeLong(timeStamp);
        dest.writeInt(nonce);
        dest.writeString(from);
        dest.writeString(to);
        dest.writeString(value);
        dest.writeString(gas);
        dest.writeString(gasPrice);
        dest.writeString(input);
        dest.writeString(gasUsed);
        dest.writeParcelableArray(operations, flags);
        dest.writeParcelable(metadata, flags);
        dest.writeString(contract);
        dest.writeString(id);
        dest.writeString(direction);
        dest.writeString(date);
        dest.writeString(coin);
        dest.writeString(sequence);
        dest.writeString(status);
    }
}
