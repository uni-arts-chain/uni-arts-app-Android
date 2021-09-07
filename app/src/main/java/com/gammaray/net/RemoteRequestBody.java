package com.gammaray.net;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;

public class RemoteRequestBody extends RequestBody {

    private static final MediaType mContentType = MediaType.parse("application/json; charset=utf-8");
    private static final Charset mCharset = Util.UTF_8;

    private String mContent;

    public RemoteRequestBody(@NonNull String mContent) {
        this.mContent = mContent;
    }

    public String getContent() {
        return mContent;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return mContentType;
    }

    @Override
    public void writeTo(@NonNull BufferedSink sink) throws IOException {
        byte[] bytes = mContent.getBytes(mCharset);
        if (bytes == null) throw new NullPointerException("content == null");
        Util.checkOffsetAndCount(bytes.length, 0, bytes.length);
        sink.write(bytes, 0, bytes.length);
    }

    public static RequestBody create(@NonNull String content) {
        return new RemoteRequestBody(content);
    }
}
