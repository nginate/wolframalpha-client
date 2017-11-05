package com.github.nginate.wolframalpha.retrofit.converter;

import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;

public class ByteArrayConverter implements Converter<ResponseBody, byte[]> {
    @Override
    public byte[] convert(ResponseBody value) throws IOException {
        return value.bytes();
    }
}
